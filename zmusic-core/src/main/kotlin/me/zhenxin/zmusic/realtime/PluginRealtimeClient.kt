package me.zhenxin.zmusic.realtime

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.zhenxin.zmusic.ZMusicInfo
import me.zhenxin.zmusic.config.APIConfig
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.entity.ZPlayer
import me.zhenxin.zmusic.playback.PlaybackService
import me.zhenxin.zmusic.playback.PlayerStateRegistry
import me.zhenxin.zmusic.playback.string
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake
import org.java_websocket.protocols.Protocol
import java.net.URI
import java.util.Collections
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * 维护 Plugin 到 ZMusic API 的 WebSocket 连接，并桥接实时玩家与播放事件。
 *
 * @property context 平台能力
 * @property config API 连接配置
 * @property states 玩家状态表
 * @property playback Mod 播放控制服务
 * @author 真心
 * @since 5.0.0
 */
class PluginRealtimeClient(
    private val context: PlatformContext,
    private val config: APIConfig,
    private val states: PlayerStateRegistry,
    private val playback: PlaybackService
) {
    private val gson = Gson()
    private val instanceId = UUID.randomUUID().toString()
    private val startedAt = System.currentTimeMillis()
    private val sequence = AtomicLong()
    private val executor = Executors.newSingleThreadScheduledExecutor { runnable ->
        Thread(runnable, "zmusic-realtime").apply { isDaemon = true }
    }
    @Volatile private var client: WebSocketClient? = null
    @Volatile private var running = false
    private var reconnectAttempt = 0
    private var heartbeatTask: ScheduledFuture<*>? = null
    private val seenMessages = LinkedHashSet<String>()

    /**
     * 启动连接；未启用或缺少 token 时只记录状态，不创建网络线程。
     */
    fun start() {
        if (!config.enabled) {
            context.logger.info("ZMusic API realtime connection is disabled.")
            return
        }
        if (config.deviceToken.isBlank()) {
            context.logger.warn("ZMusic API realtime connection is enabled but device-token is empty.")
            return
        }
        val uri = runCatching { URI(config.webSocketUrl) }.getOrNull()
        if (uri == null || uri.scheme !in setOf("ws", "wss") || uri.host.isNullOrBlank()) {
            context.logger.warn("Invalid ZMusic API websocket-url: ${config.webSocketUrl}")
            return
        }
        running = true
        scheduleConnect(0)
    }

    /**
     * 停止重连、心跳和当前 WebSocket 连接。
     */
    fun stop() {
        running = false
        heartbeatTask?.cancel(false)
        heartbeatTask = null
        client?.close(1000, "plugin shutdown")
        client = null
        executor.shutdownNow()
    }

    /**
     * 接收 Mod 消息的归一化结果并推送到 API。
     *
     * @param player 消息来源玩家
     * @param type 原始 Mod 消息类型
     * @param data 已归一化数据
     */
    fun publishClientEvent(player: ZPlayer, type: String, data: JsonObject) {
        when (type) {
            "client.hello" -> sendPlayerUpsert(player)
            "client.status", "client.progress" -> sendSequenced("plugin.playback", data)
            "client.error" -> send("plugin.error", data)
        }
    }

    /**
     * 立即向 API 上报玩家上线。
     *
     * @param player 已上线玩家
     */
    fun publishPlayerJoined(player: ZPlayer) = sendPlayerUpsert(player)

    /**
     * 立即向 API 上报玩家离线。
     *
     * @param player 已离线玩家
     */
    fun publishPlayerLeft(player: ZPlayer) {
        sendSequenced("plugin.player_left", JsonObject().apply {
            addProperty("playerUuid", player.uniqueId.toString())
            addProperty("reason", "disconnect")
        })
    }

    private fun scheduleConnect(delaySeconds: Long) {
        if (!running || executor.isShutdown) return
        executor.schedule({ connect() }, delaySeconds, TimeUnit.SECONDS)
    }

    private fun connect() {
        if (!running) return
        val uri = URI(config.webSocketUrl)
        val headers = mapOf("Authorization" to "Bearer ${config.deviceToken}")
        val draft = Draft_6455(Collections.emptyList(), listOf(Protocol(SUBPROTOCOL)))
        val next = object : WebSocketClient(uri, draft, headers, 10_000) {
            override fun onWebsocketHandshakeReceivedAsClient(
                conn: WebSocket,
                request: ClientHandshake,
                response: ServerHandshake
            ) {
                super.onWebsocketHandshakeReceivedAsClient(conn, request, response)
                if (response.httpStatus.toInt() == 401 || response.httpStatus.toInt() == 403) {
                    running = false
                    context.logger.error(
                        "ZMusic API rejected device authorization (${response.httpStatus}); automatic reconnect stopped.",
                        null
                    )
                }
            }

            override fun onOpen(handshakedata: ServerHandshake) {
                if (handshakedata.getFieldValue("Sec-WebSocket-Protocol") != SUBPROTOCOL) {
                    close(1002, "required subprotocol was not negotiated")
                    return
                }
                reconnectAttempt = 0
                context.logger.info("Connected to ZMusic API realtime service.")
                sendHello()
            }

            override fun onMessage(message: String) {
                runCatching { handleAPIMessage(parse(message)) }
                    .onFailure { context.logger.warn("Rejected ZMusic API realtime message: ${it.message}") }
            }

            override fun onClose(code: Int, reason: String, remote: Boolean) {
                heartbeatTask?.cancel(false)
                heartbeatTask = null
                if (client === this) client = null
                if (!running) return
                if (code == 1002 || code == 4001 || code == 4002) {
                    running = false
                    context.logger.error(
                        "ZMusic API realtime connection closed permanently ($code): $reason",
                        null
                    )
                    return
                }
                val delay = RECONNECT_DELAYS[reconnectAttempt.coerceAtMost(RECONNECT_DELAYS.lastIndex)]
                reconnectAttempt++
                context.logger.warn("ZMusic API realtime connection closed ($code): $reason; retrying in ${delay}s.")
                scheduleConnect(delay)
            }

            override fun onError(exception: Exception) {
                context.logger.warn("ZMusic API realtime connection error: ${exception.message}")
            }
        }
        next.connectionLostTimeout = 40
        client = next
        next.connect()
    }

    private fun sendHello() {
        val data = JsonObject().apply {
            addProperty("pluginVersion", ZMusicInfo.VERSION)
            addProperty("platform", context.platform.name.lowercase())
            addProperty("platformVersion", context.platformVersion)
            addProperty("instanceId", instanceId)
            addProperty("startedAt", startedAt)
            add("capabilities", JsonArray().apply {
                listOf(
                    "players.snapshot", "players.events", "playback.events",
                    "command.play", "command.stop", "mod.packet.v1"
                ).forEach(::add)
            })
        }
        send("plugin.hello", data)
    }

    private fun handleAPIMessage(message: RealtimeEnvelope) {
        if (!rememberMessage(message.id)) return
        when (message.type) {
            "api.hello" -> {
                context.scheduler.main { sendSnapshot() }
                heartbeatTask?.cancel(false)
                val interval = message.data.get("heartbeatIntervalMillis")?.asLong?.coerceIn(10_000, 120_000) ?: 20_000
                heartbeatTask = executor.scheduleAtFixedRate(
                    { context.scheduler.main { sendSnapshot(); sendHeartbeat() } },
                    interval,
                    interval,
                    TimeUnit.MILLISECONDS
                )
            }
            "api.resync" -> context.scheduler.main { sendSnapshot() }
            "api.play" -> dispatchCommand(message.data, true)
            "api.stop" -> dispatchCommand(message.data, false)
            "api.heartbeat" -> Unit
            "api.error" -> context.logger.warn(
                "ZMusic API protocol error ${message.data.string("code")}: ${message.data.string("message")}"
            )
            else -> throw IllegalArgumentException("Unsupported API message: ${message.type}")
        }
    }

    private fun dispatchCommand(data: JsonObject, play: Boolean) {
        val commandId = data.string("commandId")
        if (runCatching { UUID.fromString(commandId) }.isFailure) return
        context.scheduler.main {
            val (targets, missing) = selectTargets(data)
            val results = JsonArray()
            var accepted = 0
            targets.forEach { player ->
                val delivered = runCatching {
                    if (play) playback.play(player, data) else playback.stop(player, data)
                }.getOrElse {
                    context.logger.warn("Failed to deliver realtime command to ${player.name}: ${it.message}")
                    false
                }
                if (delivered) accepted++
                results.add(JsonObject().apply {
                    addProperty("playerUuid", player.uniqueId.toString())
                    addProperty("status", if (delivered) "accepted" else "rejected")
                    if (!delivered) {
                        addProperty("code", if (states.state(player).hasMod) "delivery_failed" else "mod_not_ready")
                    }
                })
            }
            missing.forEach { playerId ->
                results.add(JsonObject().apply {
                    addProperty("playerUuid", playerId.toString())
                    addProperty("status", "rejected")
                    addProperty("code", "player_offline")
                })
            }
            val rejected = targets.size + missing.size - accepted
            val status = when {
                accepted == 0 -> "rejected"
                rejected == 0 -> "accepted"
                else -> "partially_accepted"
            }
            send("plugin.command_result", JsonObject().apply {
                addProperty("commandId", commandId)
                addProperty("status", status)
                addProperty("accepted", accepted)
                addProperty("rejected", rejected)
                add("results", results)
            })
        }
    }

    private fun selectTargets(data: JsonObject): Pair<List<ZPlayer>, List<UUID>> {
        val online = context.players.onlinePlayers().associateBy { it.uniqueId }
        if (data.string("scope") == "global") return online.values.toList() to emptyList()
        val requested = data.getAsJsonArray("playerUuids")
            ?.mapNotNull { runCatching { UUID.fromString(it.asString) }.getOrNull() }
            ?.toSet()
            ?: emptySet()
        return requested.mapNotNull(online::get) to requested.filterNot(online::containsKey)
    }

    private fun sendSnapshot() {
        val players = context.players.onlinePlayers()
        states.retainOnline(players)
        sendSequenced("plugin.snapshot", JsonObject().apply {
            add("players", JsonArray().apply { players.forEach { add(playerJSON(it)) } })
        })
    }

    private fun sendPlayerUpsert(player: ZPlayer) {
        sendSequenced("plugin.player_joined", JsonObject().apply { add("player", playerJSON(player)) })
    }

    private fun sendHeartbeat() {
        sendSequenced("plugin.heartbeat", JsonObject().apply {
            addProperty("onlinePlayers", context.players.onlinePlayers().size)
        })
    }

    private fun playerJSON(player: ZPlayer): JsonObject {
        val state = states.state(player)
        return JsonObject().apply {
            addProperty("uuid", player.uniqueId.toString())
            addProperty("name", player.name)
            addProperty("connectedAt", state.connectedAt)
            addProperty("hasMod", state.hasMod)
            if (state.modVersion.isNotBlank()) addProperty("modVersion", state.modVersion)
            if (state.minecraftVersion.isNotBlank()) addProperty("minecraftVersion", state.minecraftVersion)
            if (state.loader.isNotBlank()) addProperty("loader", state.loader)
            add("playback", state.playback?.deepCopy())
        }
    }

    private fun sendSequenced(type: String, data: JsonObject) {
        data.addProperty("sequence", sequence.incrementAndGet())
        send(type, data)
    }

    private fun send(type: String, data: JsonObject) {
        val active = client ?: return
        if (!active.isOpen) return
        active.send(gson.toJson(RealtimeEnvelope(
            id = UUID.randomUUID().toString(), type = type, protocolVersion = PROTOCOL_VERSION,
            timestamp = System.currentTimeMillis(), data = data
        )))
    }

    private fun parse(raw: String): RealtimeEnvelope {
        if (raw.toByteArray(Charsets.UTF_8).size > MAX_MESSAGE_BYTES) {
            throw IllegalArgumentException("Realtime message exceeds $MAX_MESSAGE_BYTES bytes")
        }
        val root = JsonParser.parseString(raw).asJsonObject
        val envelope = RealtimeEnvelope(
            id = root.get("id").asString,
            type = root.get("type").asString,
            protocolVersion = root.get("protocolVersion").asInt,
            timestamp = root.get("timestamp").asLong,
            data = root.getAsJsonObject("data")
        )
        UUID.fromString(envelope.id)
        require(envelope.protocolVersion == PROTOCOL_VERSION) { "Unsupported realtime protocol version" }
        return envelope
    }

    private fun rememberMessage(id: String): Boolean = synchronized(seenMessages) {
        if (!seenMessages.add(id)) return@synchronized false
        if (seenMessages.size > 256) {
            seenMessages.remove(seenMessages.first())
        }
        true
    }

    private companion object {
        const val PROTOCOL_VERSION = 1
        const val SUBPROTOCOL = "zmusic.plugin.v1"
        const val MAX_MESSAGE_BYTES = 65536
        val RECONNECT_DELAYS = longArrayOf(1, 2, 4, 8, 16, 30)
    }
}

/**
 * Plugin WebSocket 的统一 JSON envelope。
 *
 * @property id 消息 UUID
 * @property type 消息类型
 * @property protocolVersion 业务协议版本
 * @property timestamp Unix 毫秒时间戳
 * @property data 业务对象
 * @author 真心
 * @since 5.0.0
 */
data class RealtimeEnvelope(
    val id: String,
    val type: String,
    val protocolVersion: Int,
    val timestamp: Long,
    val data: JsonObject
)
