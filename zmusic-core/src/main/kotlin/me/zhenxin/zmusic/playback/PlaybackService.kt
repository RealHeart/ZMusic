package me.zhenxin.zmusic.playback

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.zhenxin.zmusic.music.Song
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.entity.ZPlayer
import me.zhenxin.zmusic.protocol.PacketCodec
import me.zhenxin.zmusic.protocol.PacketProtocol
import java.util.UUID

/**
 * 在 Plugin 与客户端 Mod 之间桥接握手、播放控制和状态回报。
 *
 * @property context 平台能力
 * @property states 玩家通信状态表
 * @author 真心
 * @since 5.0.0
 */
class PlaybackService(
    private val context: PlatformContext,
    private val states: PlayerStateRegistry
) {
    private var channel = PacketProtocol.CHANNEL
    private var eventSink: ((ZPlayer, String, JsonObject) -> Unit)? = null

    /**
     * 注册插件消息通道和客户端消息处理器。
     *
     * @param channel 实际通道名
     * @param eventSink Mod 状态回报出口
     */
    fun start(channel: String, eventSink: (ZPlayer, String, JsonObject) -> Unit) {
        this.channel = channel
        this.eventSink = eventSink
        context.pluginMessenger.registerChannel(channel) { player, payload ->
            runCatching { handleClientMessage(player, payload) }
                .onFailure { context.logger.warn("Rejected ZMusic packet from ${player.name}: ${it.message}") }
        }
    }

    /**
     * 注销当前插件消息通道。
     */
    fun stop() {
        context.pluginMessenger.unregisterChannel(channel)
        eventSink = null
    }

    /**
     * 在配置重载时切换插件消息通道。
     *
     * @param nextChannel 新通道名
     */
    fun changeChannel(nextChannel: String) {
        if (nextChannel == channel) return
        val sink = eventSink ?: return
        context.pluginMessenger.unregisterChannel(channel)
        start(nextChannel, sink)
    }

    /**
     * 向玩家客户端发送本地命令搜索得到的播放指令。
     *
     * @param player 目标玩家
     * @param song 目标歌曲
     * @return true 表示玩家 Mod 已握手且消息已投递
     */
    fun play(player: ZPlayer, song: Song): Boolean {
        val songJson = JsonObject().apply {
            addProperty("id", song.id)
            addProperty("source", song.source)
            addProperty("title", song.title)
            add("artists", JsonArray().also { array -> song.artists.forEach(array::add) })
        }
        val data = JsonObject().apply {
            addProperty("requestId", UUID.randomUUID().toString())
            add("song", songJson)
            add("audio", JsonObject().apply { addProperty("url", song.audioUrl.orEmpty()) })
        }
        return play(player, data)
    }

    /**
     * 将 API 的播放命令映射为 `server.play`。
     *
     * @param player 目标玩家
     * @param commandData `api.play.data`
     * @return true 表示已向就绪 Mod 投递
     */
    fun play(player: ZPlayer, commandData: JsonObject): Boolean {
        if (!states.state(player).hasMod) return false
        val requestId = commandData.string("requestId")
        val song = commandData.getAsJsonObject("song") ?: return false
        val audioInput = commandData.getAsJsonObject("audio") ?: return false
        if (requestId.isBlank() || audioInput.string("url").isBlank()) return false

        val data = JsonObject().apply {
            addProperty("requestId", requestId)
            addProperty("mode", "replace")
            add("song", song.deepCopy())
            add("audio", audioInput.deepCopy().apply { addProperty("type", "url") })
            commandData.get("lyrics")?.takeUnless { it.isJsonNull }?.let { lyricsElement ->
                add("lyrics", lyricsElement.asJsonObject.deepCopy().apply {
                    addProperty("type", "url")
                    addProperty("format", "lrc")
                })
            }
        }
        states.markRequested(player, requestId, song)
        context.pluginMessenger.send(player, channel, PacketCodec.encode("server.play", data))
        return true
    }

    /**
     * 向玩家客户端发送停止指令。
     *
     * @param player 目标玩家
     * @return true 表示已向就绪 Mod 投递
     */
    fun stop(player: ZPlayer): Boolean = stop(player, JsonObject())

    /**
     * 将 API 的停止命令映射为 `server.stop`。
     *
     * @param player 目标玩家
     * @param commandData `api.stop.data`
     * @return true 表示已向就绪 Mod 投递
     */
    fun stop(player: ZPlayer, commandData: JsonObject): Boolean {
        if (!states.state(player).hasMod) return false
        val requestedReason = commandData.string("reason")
        val reason = requestedReason.takeIf {
            it in setOf("command", "replace", "disconnect", "server_shutdown", "permission_revoked")
        } ?: "command"
        val data = JsonObject().apply {
            addProperty("requestId", UUID.randomUUID().toString())
            commandData.get("targetRequestId")?.takeUnless { it.isJsonNull }?.let { add("targetRequestId", it) }
            addProperty("reason", reason)
        }
        context.pluginMessenger.send(player, channel, PacketCodec.encode("server.stop", data))
        return true
    }

    private fun handleClientMessage(player: ZPlayer, payload: ByteArray) {
        val message = PacketCodec.decode(payload)
        val state = states.state(player)
        if (!state.rememberMessage(message.id) || !state.allowMessage(message.type)) return
        when (message.type) {
            "client.hello" -> handleHello(player, message.id, message.data)
            "client.status" -> handleStatus(player, message.data)
            "client.progress" -> handleProgress(player, message.data)
            "client.error" -> eventSink?.invoke(player, message.type, message.data.deepCopy())
            else -> sendError(
                player,
                message.id,
                "protocol",
                "unsupported_message",
                "Unsupported client message type: ${message.type}"
            )
        }
    }

    private fun handleHello(player: ZPlayer, messageId: String, data: JsonObject) {
        val protocolVersion = data.get("protocolVersion")?.let { element ->
            runCatching { element.takeIf { it.isJsonPrimitive && it.asJsonPrimitive.isNumber }?.asInt }.getOrNull()
        }
        if (protocolVersion != null && protocolVersion != PacketProtocol.VERSION) {
            sendError(
                player,
                messageId,
                "handshake",
                "unsupported_protocol",
                "Business protocol version is not supported",
                JsonObject().apply {
                    add("supportedProtocols", JsonArray().apply { add(PacketProtocol.VERSION) })
                }
            )
            return
        }

        val playerClientId = data.stringField("playerClientId")
        val modVersion = data.stringField("modVersion")
        val minecraftVersion = data.stringField("minecraftVersion")
        val loader = data.stringField("loader")
        val validClientId = runCatching { UUID.fromString(playerClientId) }.isSuccess
        if (protocolVersion == null || !validClientId || modVersion.isNullOrBlank() || modVersion.length > 64 ||
            minecraftVersion.isNullOrBlank() || minecraftVersion.length > 64 ||
            loader !in setOf("fabric", "forge", "neoforge")) {
            sendError(player, messageId, "handshake", "invalid_payload", "Invalid client hello payload")
            return
        }
        states.markReady(player, data)
        val response = JsonObject().apply {
            addProperty("protocolVersion", PacketProtocol.VERSION)
            addProperty("serverVersion", me.zhenxin.zmusic.ZMusicInfo.VERSION)
            addProperty("platform", context.platform.name.lowercase())
            add("capabilities", JsonArray().apply {
                listOf("play.url", "stop", "status", "progress", "lyrics.url").forEach(::add)
            })
            add("limits", JsonObject().apply {
                addProperty("maxPacketBytes", PacketProtocol.MAX_PACKET_BYTES)
                addProperty("maxJsonBytes", PacketProtocol.MAX_JSON_BYTES)
                addProperty("maxLyricsBytes", 262144)
            })
        }
        context.pluginMessenger.send(player, channel, PacketCodec.encode("server.hello", response))
        eventSink?.invoke(player, "client.hello", data.deepCopy())
    }

    private fun sendError(
        player: ZPlayer,
        replyTo: String,
        phase: String,
        code: String,
        message: String,
        details: JsonObject? = null
    ) {
        val error = JsonObject().apply {
            addProperty("replyTo", replyTo)
            addProperty("phase", phase)
            addProperty("code", code)
            addProperty("message", message)
            addProperty("retryable", false)
            details?.let { add("details", it) }
        }
        context.pluginMessenger.send(player, channel, PacketCodec.encode("server.error", error))
    }

    private fun handleStatus(player: ZPlayer, data: JsonObject) {
        val state = states.state(player)
        val requestId = data.string("requestId")
        val playbackState = data.string("state")
        if (requestId.isBlank() || requestId != state.currentRequestId || state.currentSong == null ||
            playbackState !in setOf("loading", "playing", "stopped", "ended", "failed")) return
        val playback = JsonObject().apply {
            addProperty("playerUuid", player.uniqueId.toString())
            addProperty("requestId", requestId)
            addProperty("state", playbackState)
            addProperty("positionMillis", 0)
            addProperty("durationMillis", data.get("durationMillis")?.asLong ?: -1L)
            add("song", state.currentSong!!.deepCopy())
        }
        states.updatePlayback(player, playback)
        eventSink?.invoke(player, "client.status", playback)
    }

    private fun handleProgress(player: ZPlayer, data: JsonObject) {
        val state = states.state(player)
        val requestId = data.string("requestId")
        val position = data.get("positionMillis")?.asLong ?: return
        val duration = data.get("durationMillis")?.asLong ?: return
        if (requestId.isBlank() || requestId != state.currentRequestId || state.currentSong == null ||
            position < 0 || duration < -1) return
        val playback = JsonObject().apply {
            addProperty("playerUuid", player.uniqueId.toString())
            addProperty("requestId", requestId)
            addProperty("state", "playing")
            addProperty("positionMillis", position)
            addProperty("durationMillis", duration)
            add("song", state.currentSong!!.deepCopy())
            data.getAsJsonObject("lyrics")?.takeIf { it.string("state") == "ready" }?.let { lyrics ->
                add("lyrics", JsonObject().apply {
                    addProperty("lineIndex", lyrics.get("lineIndex")?.asLong ?: -1L)
                    addProperty("text", lyrics.string("text"))
                    lyrics.get("translation")?.takeUnless { it.isJsonNull }?.let { add("translation", it) }
                })
            }
        }
        states.updatePlayback(player, playback)
        eventSink?.invoke(player, "client.progress", playback)
    }
}

private fun JsonObject.stringField(name: String): String? {
    val element = get(name) ?: return null
    return if (element.isJsonPrimitive && element.asJsonPrimitive.isString) element.asString else null
}
