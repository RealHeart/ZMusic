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
import me.zhenxin.zmusic.provider.AccountBindingChallenge
import me.zhenxin.zmusic.provider.MusicProviderService
import me.zhenxin.zmusic.provider.ProviderAccount
import me.zhenxin.zmusic.provider.ProviderDescriptor
import me.zhenxin.zmusic.provider.ProviderError
import me.zhenxin.zmusic.provider.ProviderProtocol
import me.zhenxin.zmusic.provider.ProviderResult
import me.zhenxin.zmusic.provider.ProviderSearchResult
import me.zhenxin.zmusic.provider.PendingProviderRequest
import me.zhenxin.zmusic.provider.PendingRequestRegistry
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
import java.util.concurrent.ConcurrentHashMap
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
    private val playback: PlaybackService,
    private val requestTimeoutSeconds: Long = 15
) : MusicProviderService {
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
    private val pendingRequests = PendingRequestRegistry()
    private val playerSessions = ConcurrentHashMap<UUID, Long>()
    private val playerSessionSequence = AtomicLong()
    private val lifecycleSequence = AtomicLong()

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
        lifecycleSequence.incrementAndGet()
        heartbeatTask?.cancel(false)
        heartbeatTask = null
        client?.close(1000, "plugin shutdown")
        client = null
        failPendingRequests(ProviderError("connection_closed", "ZMusic API 连接已关闭。", true))
        executor.shutdownNow()
    }

    override fun providers(player: ZPlayer, callback: (ProviderResult<List<ProviderDescriptor>>) -> Unit) {
        request(player, "plugin.provider_list", JsonObject(), ProviderProtocol::providers, callback)
    }

    override fun accounts(player: ZPlayer, callback: (ProviderResult<List<ProviderAccount>>) -> Unit) {
        request(player, "plugin.account_list", JsonObject(), ProviderProtocol::accounts, callback)
    }

    override fun beginBinding(
        player: ZPlayer,
        providerId: String,
        callback: (ProviderResult<AccountBindingChallenge>) -> Unit
    ) {
        if (!validProviderId(providerId)) {
            failValidation(player, callback, "平台标识格式无效。")
            return
        }
        request(player, "plugin.account_bind", JsonObject().apply {
            addProperty("providerId", providerId)
        }, { data ->
            when (val result = ProviderProtocol.binding(data)) {
                is ProviderResult.Failure -> result
                is ProviderResult.Success -> {
                    require(result.value.providerId == providerId) { "Provider mismatch" }
                    result
                }
            }
        }, callback)
    }

    override fun unbind(player: ZPlayer, providerId: String, callback: (ProviderResult<Unit>) -> Unit) {
        if (!validProviderId(providerId)) {
            failValidation(player, callback, "平台标识格式无效。")
            return
        }
        request(player, "plugin.account_unbind", JsonObject().apply {
            addProperty("providerId", providerId)
        }, ProviderProtocol::unit, callback)
    }

    override fun search(
        player: ZPlayer,
        providerId: String?,
        query: String,
        limit: Int,
        callback: (ProviderResult<ProviderSearchResult>) -> Unit
    ) {
        if ((providerId != null && !validProviderId(providerId)) ||
            query.isBlank() || query.length > 128 || query.any(Char::isISOControl) || limit !in 1..20) {
            failValidation(player, callback, "搜索参数无效。")
            return
        }
        request(player, "plugin.search", JsonObject().apply {
            providerId?.let { addProperty("providerId", it) }
            addProperty("query", query)
            addProperty("limit", limit.coerceIn(1, 20))
        }, { data ->
            when (val result = ProviderProtocol.search(data)) {
                is ProviderResult.Failure -> result
                is ProviderResult.Success -> {
                    require(result.value.query == query) { "Search query mismatch" }
                    require(result.value.tracks.size <= limit.coerceIn(1, 20)) { "Too many search results" }
                    require(providerId == null || result.value.tracks.all { it.providerId == providerId }) {
                        "Search Provider mismatch"
                    }
                    result
                }
            }
        }, callback)
    }

    override fun play(
        player: ZPlayer,
        trackToken: String,
        providerId: String,
        callback: (ProviderResult<Unit>) -> Unit
    ) {
        if (trackToken.isBlank() || trackToken.length > 2048 || trackToken.any(Char::isISOControl) ||
            !validProviderId(providerId)) {
            failValidation(player, callback, "歌曲引用无效。")
            return
        }
        val requestId = UUID.randomUUID()
        val pending = PendingProviderRequest(
            playerId = player.uniqueId,
            playerSession = playerSession(player.uniqueId),
            expectedResponseType = "api.provider_play_result",
            complete = complete@ { data ->
                val parsed = runCatching { ProviderProtocol.playback(data) }.getOrElse {
                    callback(ProviderResult.Failure(INVALID_RESPONSE))
                    return@complete
                }
                when (parsed) {
                    is ProviderResult.Failure -> callback(parsed)
                    is ProviderResult.Success -> {
                        val source = parsed.value.getAsJsonObject("song")?.string("source").orEmpty()
                        if (source != providerId) {
                            callback(ProviderResult.Failure(INVALID_RESPONSE))
                            return@complete
                        }
                        if (!states.state(player).hasMod) {
                            callback(ProviderResult.Failure(ProviderError(
                                "mod_not_ready",
                                "客户端 Mod 尚未完成 ZMusic 协议握手。"
                            )))
                            return@complete
                        }
                        val delivered = runCatching { playback.play(player, parsed.value) }
                            .onFailure {
                                context.logger.warn("Failed to deliver Provider playback to ${player.name}: ${it.message}")
                            }
                            .getOrElse {
                                callback(ProviderResult.Failure(ProviderError(
                                    "delivery_failed",
                                    "播放请求投递失败，请稍后重试。",
                                    true
                                )))
                                return@complete
                            }
                        if (delivered) {
                            callback(ProviderResult.Success(Unit))
                        } else {
                            callback(ProviderResult.Failure(ProviderError(
                                "invalid_response",
                                "音乐服务返回了无效播放数据。"
                            )))
                        }
                    }
                }
            },
            fail = callback
        )
        sendRequest(requestId, player, "plugin.provider_play", JsonObject().apply {
            addProperty("trackToken", trackToken)
            addProperty("providerId", providerId)
        }, pending)
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
    fun publishPlayerJoined(player: ZPlayer) {
        playerSessions[player.uniqueId] = playerSessionSequence.incrementAndGet()
        sendPlayerUpsert(player)
    }

    /**
     * 立即向 API 上报玩家离线。
     *
     * @param player 已离线玩家
     */
    fun publishPlayerLeft(player: ZPlayer) {
        playerSessions.remove(player.uniqueId)
        pendingRequests.removePlayer(player.uniqueId)
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
                failPendingRequests(ProviderError("connection_closed", "ZMusic API 连接已断开。", true))
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
                    "command.play", "command.stop", "mod.packet.v1",
                    "providers.v1", "accounts.v1", "search.v1", "provider.play.v1"
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
            "api.provider_list_result", "api.account_list_result", "api.account_bind_result",
            "api.account_unbind_result", "api.search_result", "api.provider_play_result" ->
                completeProviderRequest(message.type, message.data)
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

    private fun <T> request(
        player: ZPlayer,
        type: String,
        data: JsonObject,
        parser: (JsonObject) -> ProviderResult<T>,
        callback: (ProviderResult<T>) -> Unit
    ) {
        val requestId = UUID.randomUUID()
        sendRequest(requestId, player, type, data, PendingProviderRequest(
            playerId = player.uniqueId,
            playerSession = playerSession(player.uniqueId),
            expectedResponseType = responseType(type),
            complete = complete@ { response ->
                val result = runCatching { parser(response) }.getOrElse {
                    callback(ProviderResult.Failure(INVALID_RESPONSE))
                    return@complete
                }
                callback(result)
            },
            fail = callback
        ))
    }

    private fun sendRequest(
        requestId: UUID,
        player: ZPlayer,
        type: String,
        data: JsonObject,
        pending: PendingProviderRequest
    ) {
        data.addProperty("requestId", requestId.toString())
        data.addProperty("playerUuid", player.uniqueId.toString())
        pendingRequests.register(requestId, pending)
        val timeout = runCatching { executor.schedule({
            pendingRequests.take(requestId, pending.expectedResponseType)?.let { expired ->
                failIfOnline(expired, ProviderError(
                    "request_timeout",
                    "音乐服务请求超时，请稍后重试。",
                    true
                ))
            }
        }, requestTimeoutSeconds, TimeUnit.SECONDS) }.getOrNull()
        if (timeout == null) {
            if (pendingRequests.remove(requestId, pending)) {
                failIfOnline(pending, SERVICE_UNAVAILABLE)
            }
            return
        }
        pending.attachTimeout(timeout)
        val sent = runCatching { send(type, data) }
            .onFailure { context.logger.warn("Failed to send Provider request $requestId: ${it.message}") }
            .getOrDefault(false)
        if (!sent) {
            if (pendingRequests.remove(requestId, pending)) {
                failIfOnline(pending, SERVICE_UNAVAILABLE)
            }
            return
        }
    }

    private fun completeProviderRequest(responseType: String, data: JsonObject) {
        val requestId = runCatching { UUID.fromString(data.string("requestId")) }.getOrNull()
            ?: throw IllegalArgumentException("Invalid Provider requestId")
        val pending = pendingRequests.take(requestId, responseType) ?: return
        val lifecycle = lifecycleSequence.get()
        context.scheduler.main {
            if (lifecycleSequence.get() != lifecycle) return@main
            if (!isPlayerSessionOnline(pending)) return@main
            runCatching { pending.complete(data) }
                .onFailure {
                    context.logger.warn("Provider response callback failed for $requestId: ${it.message}")
                }
        }
    }

    private fun failPendingRequests(error: ProviderError) {
        pendingRequests.drain().forEach { failIfOnline(it, error) }
    }

    private fun failIfOnline(pending: PendingProviderRequest, error: ProviderError) {
        context.scheduler.main {
            if (isPlayerSessionOnline(pending)) {
                runCatching { pending.fail(ProviderResult.Failure(error)) }
                    .onFailure { context.logger.warn("Provider failure callback failed: ${it.message}") }
            }
        }
    }

    private fun playerSession(playerId: UUID): Long {
        return playerSessions.computeIfAbsent(playerId) { playerSessionSequence.incrementAndGet() }
    }

    private fun isPlayerSessionOnline(pending: PendingProviderRequest): Boolean {
        return playerSessions[pending.playerId] == pending.playerSession &&
            context.players.onlinePlayers().any { it.uniqueId == pending.playerId }
    }

    private fun validProviderId(providerId: String): Boolean = PROVIDER_ID.matches(providerId)

    private fun <T> failValidation(
        player: ZPlayer,
        callback: (ProviderResult<T>) -> Unit,
        message: String
    ) {
        val session = playerSession(player.uniqueId)
        context.scheduler.main {
            if (playerSessions[player.uniqueId] != session ||
                context.players.onlinePlayers().none { it.uniqueId == player.uniqueId }) return@main
            runCatching { callback(ProviderResult.Failure(ProviderError("invalid_request", message))) }
                .onFailure { context.logger.warn("Provider validation callback failed: ${it.message}") }
        }
    }

    private fun responseType(requestType: String): String = when (requestType) {
        "plugin.provider_list" -> "api.provider_list_result"
        "plugin.account_list" -> "api.account_list_result"
        "plugin.account_bind" -> "api.account_bind_result"
        "plugin.account_unbind" -> "api.account_unbind_result"
        "plugin.search" -> "api.search_result"
        else -> throw IllegalArgumentException("Unsupported Provider request type: $requestType")
    }

    private fun send(type: String, data: JsonObject): Boolean {
        val active = client ?: return false
        if (!active.isOpen) return false
        active.send(gson.toJson(RealtimeEnvelope(
            id = UUID.randomUUID().toString(), type = type, protocolVersion = PROTOCOL_VERSION,
            timestamp = System.currentTimeMillis(), data = data
        )))
        return true
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
        val SERVICE_UNAVAILABLE = ProviderError(
            "service_unavailable",
            "ZMusic API 当前不可用。",
            true
        )
        val INVALID_RESPONSE = ProviderError(
            "invalid_response",
            "音乐服务返回了无效响应。"
        )
        val PROVIDER_ID = Regex("^[a-z0-9][a-z0-9._-]{0,63}$")
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
