package me.zhenxin.zmusic.playback

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.entity.ZPlayer
import me.zhenxin.zmusic.protocol.PacketCodec
import me.zhenxin.zmusic.protocol.PacketProtocol
import java.net.InetAddress
import java.net.URI
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
     * 将 API 的播放命令映射为 `server.play`。
     *
     * @param player 目标玩家
     * @param commandData `api.play.data`
     * @return true 表示已向就绪 Mod 投递
     */
    fun play(player: ZPlayer, commandData: JsonObject): Boolean {
        if (!states.state(player).hasMod) return false
        val requestId = commandData.requiredString("requestId", 36) ?: return false
        if (runCatching { UUID.fromString(requestId) }.isFailure) return false
        val song = normalizeSong(commandData.objectField("song") ?: return false) ?: return false
        val audioUrl = commandData.objectField("audio")?.requiredString("url", MAX_URL_LENGTH) ?: return false
        if (!isHttpsUrl(audioUrl)) return false
        val lyricsInput = commandData.get("lyrics")?.takeUnless { it.isJsonNull }
        val lyricsUrl = if (lyricsInput == null) {
            null
        } else {
            if (!lyricsInput.isJsonObject) return false
            lyricsInput.asJsonObject.requiredString("url", MAX_URL_LENGTH)?.takeIf(::isHttpsUrl) ?: return false
        }

        val data = JsonObject().apply {
            addProperty("requestId", requestId)
            addProperty("mode", "replace")
            add("song", song)
            add("audio", JsonObject().apply {
                addProperty("type", "url")
                addProperty("url", audioUrl)
            })
            lyricsUrl?.let { url ->
                add("lyrics", JsonObject().apply {
                    addProperty("type", "url")
                    addProperty("format", "lrc")
                    addProperty("url", url)
                })
            }
        }
        context.pluginMessenger.send(player, channel, PacketCodec.encode("server.play", data))
        states.markRequested(player, requestId, song)
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

private fun normalizeSong(input: JsonObject): JsonObject? {
    val id = input.requiredString("id", 256) ?: return null
    val source = input.requiredString("source", 64)?.takeIf(PROVIDER_ID::matches) ?: return null
    val title = input.requiredString("title", 256) ?: return null
    val artistsInput = input.get("artists")?.takeIf { it.isJsonArray }?.asJsonArray ?: return null
    if (artistsInput.size() > MAX_ARTISTS) return null
    val artists = artistsInput.map { element ->
        if (!element.isJsonPrimitive || !element.asJsonPrimitive.isString) return null
        element.asString.takeIf {
            it.isNotBlank() && it.length <= 128 && it.none(Char::isISOControl)
        } ?: return null
    }
    val album = input.optionalString("album", 256) ?: if (input.has("album") && !input.get("album").isJsonNull) return null else null
    return JsonObject().apply {
        addProperty("id", id)
        addProperty("source", source)
        addProperty("title", title)
        add("artists", JsonArray().apply { artists.forEach(::add) })
        album?.let { addProperty("album", it) }
    }
}

private fun isHttpsUrl(value: String): Boolean {
    val uri = runCatching { URI(value) }.getOrNull()
    if (uri == null || !uri.scheme.equals("https", ignoreCase = true) ||
        uri.host.isNullOrBlank() || uri.userInfo != null) return false
    val host = uri.host.removePrefix("[").removeSuffix("]").lowercase()
    if (host == "localhost" || host.endsWith(".localhost")) return false
    if (host.matches(Regex("^0x[0-9a-f]+$")) || host.all(Char::isDigit)) return false
    val dottedNumeric = host.takeIf { it.matches(Regex("^\\d+(?:\\.\\d+){3}$")) }?.split('.')
    if (dottedNumeric != null && dottedNumeric.any {
            it.length > 1 && it.startsWith('0') || it.toIntOrNull() !in 0..255
        }) return false
    val address = parseIpLiteral(host) ?: return true
    if (address.isAnyLocalAddress || address.isLoopbackAddress || address.isLinkLocalAddress ||
        address.isSiteLocalAddress || address.isMulticastAddress) return false
    val bytes = address.address
    if (bytes.size == 4 && bytes[0].toInt() and 0xff == 100 && bytes[1].toInt() and 0xc0 == 64) return false
    return bytes.size != 16 || bytes[0].toInt() and 0xfe != 0xfc
}

private fun parseIpLiteral(host: String): InetAddress? {
    val ipv4 = host.matches(Regex("^\\d{1,3}(?:\\.\\d{1,3}){3}$"))
    if (!ipv4 && ':' !in host) return null
    if (ipv4) {
        val parts = host.split('.')
        val octets = parts.map { it.toIntOrNull() ?: return null }
        if (octets.any { it !in 0..255 }) return null
        return InetAddress.getByAddress(octets.map(Int::toByte).toByteArray())
    }
    return runCatching { InetAddress.getByName(host) }.getOrNull()
}

private fun JsonObject.objectField(name: String): JsonObject? =
    get(name)?.takeIf { it.isJsonObject }?.asJsonObject

private fun JsonObject.requiredString(name: String, maximumLength: Int): String? =
    optionalString(name, maximumLength)?.takeUnless(String::isBlank)

private fun JsonObject.optionalString(name: String, maximumLength: Int): String? {
    val element = get(name) ?: return null
    if (element.isJsonNull || !element.isJsonPrimitive || !element.asJsonPrimitive.isString) return null
    return element.asString.takeIf { it.length <= maximumLength && it.none(Char::isISOControl) }
}

private fun JsonObject.stringField(name: String): String? {
    val element = get(name) ?: return null
    return if (element.isJsonPrimitive && element.asJsonPrimitive.isString) element.asString else null
}

private val PROVIDER_ID = Regex("^[a-z0-9][a-z0-9._-]{0,63}$")
private const val MAX_ARTISTS = 10
private const val MAX_URL_LENGTH = 2048
