package me.zhenxin.zmusic.realtime

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.zhenxin.zmusic.config.APIConfig
import me.zhenxin.zmusic.platform.CommandRegistry
import me.zhenxin.zmusic.platform.Platform
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.PlatformLogger
import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.PluginMessageListener
import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.Scheduler
import me.zhenxin.zmusic.platform.entity.ZPlayer
import me.zhenxin.zmusic.playback.PlaybackService
import me.zhenxin.zmusic.playback.PlayerStateRegistry
import me.zhenxin.zmusic.protocol.PacketCodec
import me.zhenxin.zmusic.provider.AccountBindingChallenge
import me.zhenxin.zmusic.provider.AccountBindingStatus
import me.zhenxin.zmusic.provider.ProviderAccount
import me.zhenxin.zmusic.provider.ProviderDescriptor
import me.zhenxin.zmusic.provider.ProviderResult
import me.zhenxin.zmusic.provider.ProviderSearchResult
import org.java_websocket.WebSocket
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.protocols.Protocol
import org.java_websocket.server.WebSocketServer
import java.io.File
import java.net.InetSocketAddress
import java.util.UUID
import java.util.Collections
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * [PluginRealtimeClient] 的未连接和生命周期失败路径测试。
 *
 * @author 真心
 * @since 5.0.0
 */
class PluginRealtimeClientTest {
    @Test
    fun `completes all provider workflows over websocket protocol`() {
        val server = TestProviderServer()
        server.start()
        assertTrue(server.started.await(5, TimeUnit.SECONDS))
        val fixture = Fixture(APIConfig(
            true,
            "ws://127.0.0.1:${server.port}/plugin",
            "device-token"
        ))
        fixture.states.markReady(fixture.player, JsonObject())
        fixture.client.start()
        try {
            assertTrue(server.connected.await(5, TimeUnit.SECONDS))
            assertTrue(server.helloReceived.await(5, TimeUnit.SECONDS))
            val provider = awaitResult<ProviderDescriptor> { callback ->
                fixture.client.providers(fixture.player) { result ->
                    callback(result.mapSingle())
                }
            }
            assertEquals("netease", provider.id)
            val account = awaitResult<ProviderAccount> { callback ->
                fixture.client.accounts(fixture.player) { result ->
                    callback(result.mapSingle())
                }
            }
            assertEquals(AccountBindingStatus.BOUND, account.status)
            val binding = awaitResult<AccountBindingChallenge> { callback ->
                fixture.client.beginBinding(fixture.player, "netease", callback)
            }
            assertEquals("https://account.example.test/bind", binding.verificationUri)
            awaitResult<Unit> { callback -> fixture.client.unbind(fixture.player, "netease", callback) }
            val searchResult = AtomicReference<ProviderResult<ProviderSearchResult>>()
            val searchCompleted = CountDownLatch(1)

            fixture.client.search(fixture.player, "netease", "测试", 10) {
                searchResult.set(it)
                searchCompleted.countDown()
            }

            assertTrue(searchCompleted.await(5, TimeUnit.SECONDS))
            val rawSearchResult = searchResult.get()
            val search = assertIs<ProviderResult.Success<ProviderSearchResult>>(
                rawSearchResult,
                "Expected successful search, got $rawSearchResult"
            ).value
            assertEquals("opaque-token", search.tracks.single().trackToken)
            val playResult = AtomicReference<ProviderResult<Unit>>()
            val playCompleted = CountDownLatch(1)

            fixture.client.play(fixture.player, search.tracks.single().trackToken, "netease") {
                playResult.set(it)
                playCompleted.countDown()
            }

            assertTrue(playCompleted.await(5, TimeUnit.SECONDS))
            assertIs<ProviderResult.Success<Unit>>(playResult.get())
            assertEquals("Bearer device-token", server.authorization.get())
            assertEquals("opaque-token", server.playedToken.get())
            val packet = PacketCodec.decode(fixture.context.sentPackets.single())
            assertEquals("server.play", packet.type)
            assertEquals("https://media.example.test/song.mp3",
                packet.data.getAsJsonObject("audio").get("url").asString)
            assertTrue(server.sentWrongResponse)
        } finally {
            fixture.client.stop()
            server.stop(1_000)
        }
    }

    private fun <T> awaitResult(request: ((ProviderResult<T>) -> Unit) -> Unit): T {
        val result = AtomicReference<ProviderResult<T>>()
        val completed = CountDownLatch(1)
        request {
            result.set(it)
            completed.countDown()
        }
        assertTrue(completed.await(5, TimeUnit.SECONDS))
        return assertIs<ProviderResult.Success<T>>(result.get()).value
    }

    @Test
    fun `all provider operations fail once when api is unavailable`() {
        val fixture = Fixture()
        val results = mutableListOf<ProviderResult<*>>()

        fixture.client.providers(fixture.player, results::add)
        fixture.client.accounts(fixture.player, results::add)
        fixture.client.beginBinding(fixture.player, "netease", results::add)
        fixture.client.unbind(fixture.player, "netease", results::add)
        fixture.client.search(fixture.player, "netease", "测试", 10, results::add)
        fixture.client.play(fixture.player, "opaque-token", "netease", results::add)

        assertEquals(6, results.size)
        results.forEach { result ->
            val failure = assertIs<ProviderResult.Failure>(result)
            assertEquals("service_unavailable", failure.error.code)
        }
        assertEquals(6, fixture.context.mainCalls)
        fixture.client.stop()
    }

    @Test
    fun `request after stop fails once without throwing`() {
        val fixture = Fixture()
        fixture.client.stop()
        val results = mutableListOf<ProviderResult<*>>()

        fixture.client.search(fixture.player, null, "测试", 10, results::add)

        val failure = assertIs<ProviderResult.Failure>(results.single())
        assertEquals("service_unavailable", failure.error.code)
        assertTrue(failure.error.retryable)
    }

    @Test
    fun `callback exception is swallowed without second invocation`() {
        val fixture = Fixture()
        var invocations = 0

        fixture.client.search(fixture.player, null, "测试", 10) {
            invocations++
            throw IllegalStateException("sender disconnected")
        }

        assertEquals(1, invocations)
        fixture.client.stop()
    }

    @Test
    fun `rejects invalid provider inputs before transport`() {
        val fixture = Fixture()
        val results = mutableListOf<ProviderResult<*>>()

        fixture.client.beginBinding(fixture.player, "Net Ease", results::add)
        fixture.client.unbind(fixture.player, "../netease", results::add)
        fixture.client.search(fixture.player, null, "", 10, results::add)
        fixture.client.search(fixture.player, "netease", "测试", 21, results::add)
        fixture.client.play(fixture.player, "token\nforged", "netease", results::add)

        assertEquals(5, results.size)
        results.forEach { result ->
            assertEquals("invalid_request", assertIs<ProviderResult.Failure>(result).error.code)
        }
        assertEquals(5, fixture.context.mainCalls)
        fixture.client.stop()
    }

    @Test
    fun `does not deliver queued callback to reconnected player session`() {
        val fixture = Fixture(runMainImmediately = false)
        var invocations = 0

        fixture.client.search(fixture.player, null, "测试", 10) { invocations++ }
        fixture.client.publishPlayerLeft(fixture.player)
        fixture.client.publishPlayerJoined(fixture.player)
        fixture.context.runQueuedMainTasks()

        assertEquals(0, invocations)
        fixture.client.stop()
        fixture.context.runQueuedMainTasks()
    }

    @Test
    fun `does not deliver queued response after client stops`() {
        val server = TestProviderServer()
        server.start()
        assertTrue(server.started.await(5, TimeUnit.SECONDS))
        val fixture = Fixture(APIConfig(
            true,
            "ws://127.0.0.1:${server.port}/plugin",
            "device-token"
        ), runMainImmediately = false)
        fixture.client.start()
        try {
            assertTrue(server.helloReceived.await(5, TimeUnit.SECONDS))
            var invocations = 0
            fixture.client.search(fixture.player, null, "测试", 10) { invocations++ }
            assertTrue(fixture.context.awaitQueuedMainTask())

            fixture.client.stop()
            fixture.context.runQueuedMainTasks()

            assertEquals(0, invocations)
        } finally {
            fixture.client.stop()
            server.stop(1_000)
        }
    }

    private class Fixture(
        config: APIConfig = APIConfig(false, "wss://api.example.test/plugin", ""),
        runMainImmediately: Boolean = true
    ) {
        val player = TestPlayer()
        val context = TestContext(player, runMainImmediately)
        val states = PlayerStateRegistry()
        val client = PluginRealtimeClient(
            context,
            config,
            states,
            PlaybackService(context, states)
        )
    }
}

private fun <T> ProviderResult<List<T>>.mapSingle(): ProviderResult<T> = when (this) {
    is ProviderResult.Failure -> this
    is ProviderResult.Success -> ProviderResult.Success(value.single())
}

private class TestPlayer : ZPlayer {
    override val uniqueId = UUID.randomUUID()
    override val name = "Player"
    override fun hasPermission(permission: String) = true
    override fun sendMessage(message: String) = Unit
}

private class TestContext(
    private val player: ZPlayer,
    private val runMainImmediately: Boolean = true
) : PlatformContext {
    var mainCalls = 0
    val sentPackets = mutableListOf<ByteArray>()
    private val mainTasks = mutableListOf<() -> Unit>()
    override val platform = Platform.BUKKIT
    override val platformVersion = "test"
    override val dataFolder = File("build/test-realtime-data")
    override val logger = object : PlatformLogger {
        override fun info(message: String) = Unit
        override fun warn(message: String) = Unit
        override fun error(message: String, throwable: Throwable?) = Unit
    }
    override val commandRegistry = CommandRegistry { }
    override val players = object : PlayerGateway {
        override fun onlinePlayers() = listOf(player)
        override fun findByName(name: String) = player.takeIf { it.name == name }
    }
    override val pluginMessenger = object : PluginMessenger {
        override fun registerChannel(channel: String, listener: PluginMessageListener) = Unit
        override fun unregisterChannel(channel: String) = Unit
        override fun send(player: ZPlayer, channel: String, payload: ByteArray) {
            synchronized(sentPackets) { sentPackets += payload }
        }
    }
    override val scheduler = object : Scheduler {
        override fun async(task: () -> Unit) = task()
        override fun main(task: () -> Unit) {
            mainCalls++
            if (runMainImmediately) task() else synchronized(mainTasks) { mainTasks += task }
        }
    }

    fun runQueuedMainTasks() {
        val tasks = synchronized(mainTasks) { mainTasks.toList().also { mainTasks.clear() } }
        tasks.forEach { it() }
    }

    fun awaitQueuedMainTask(): Boolean {
        repeat(100) {
            if (synchronized(mainTasks) { mainTasks.isNotEmpty() }) return true
            Thread.sleep(10)
        }
        return false
    }
}

private class TestProviderServer : WebSocketServer(
    InetSocketAddress("127.0.0.1", 0),
    listOf(Draft_6455(Collections.emptyList(), listOf(Protocol("zmusic.plugin.v1"))))
) {
    val started = CountDownLatch(1)
    val connected = CountDownLatch(1)
    val helloReceived = CountDownLatch(1)
    val authorization = AtomicReference<String>()
    val playedToken = AtomicReference<String>()
    @Volatile var sentWrongResponse = false
    private val gson = Gson()

    override fun onStart() {
        started.countDown()
    }

    override fun onOpen(connection: WebSocket, handshake: ClientHandshake) {
        authorization.set(handshake.getFieldValue("Authorization"))
        connected.countDown()
    }

    override fun onMessage(connection: WebSocket, raw: String) {
        val message = JsonParser.parseString(raw).asJsonObject
        val type = message.get("type").asString
        val data = message.getAsJsonObject("data")
        when (type) {
            "plugin.hello" -> helloReceived.countDown()
            "plugin.provider_list" -> sendResponse(connection, "api.provider_list_result", JsonObject().apply {
                addProperty("requestId", data.get("requestId").asString)
                addProperty("status", "ok")
                add("providers", JsonArray().apply {
                    add(JsonObject().apply {
                        addProperty("id", "netease")
                        addProperty("name", "网易云音乐")
                        add("capabilities", JsonArray().apply { add("search"); add("playback") })
                        addProperty("available", true)
                    })
                })
            })
            "plugin.account_list" -> sendResponse(connection, "api.account_list_result", JsonObject().apply {
                addProperty("requestId", data.get("requestId").asString)
                addProperty("status", "ok")
                add("accounts", JsonArray().apply {
                    add(JsonObject().apply {
                        addProperty("providerId", "netease")
                        addProperty("providerName", "网易云音乐")
                        addProperty("status", "bound")
                        addProperty("accountName", "测试用户")
                    })
                })
            })
            "plugin.account_bind" -> sendResponse(connection, "api.account_bind_result", JsonObject().apply {
                addProperty("requestId", data.get("requestId").asString)
                addProperty("status", "ok")
                addProperty("providerId", data.get("providerId").asString)
                addProperty("verificationUri", "https://account.example.test/bind")
                addProperty("userCode", "ABCD-EFGH")
                addProperty("expiresAt", System.currentTimeMillis() + 60_000)
            })
            "plugin.account_unbind" -> sendResponse(connection, "api.account_unbind_result", JsonObject().apply {
                addProperty("requestId", data.get("requestId").asString)
                addProperty("status", "ok")
            })
            "plugin.search" -> {
                val requestId = data.get("requestId").asString
                sendResponse(connection, "api.account_list_result", JsonObject().apply {
                    addProperty("requestId", requestId)
                    addProperty("status", "ok")
                    add("accounts", JsonArray())
                })
                sentWrongResponse = true
                sendResponse(connection, "api.search_result", JsonObject().apply {
                    addProperty("requestId", requestId)
                    addProperty("status", "ok")
                    addProperty("query", data.get("query").asString)
                    add("tracks", JsonArray().apply {
                        add(JsonObject().apply {
                            addProperty("trackToken", "opaque-token")
                            addProperty("providerId", "netease")
                            addProperty("providerName", "网易云音乐")
                            addProperty("title", "测试歌曲")
                            add("artists", JsonArray().apply { add("测试歌手") })
                            addProperty("playable", true)
                        })
                    })
                })
            }
            "plugin.provider_play" -> {
                playedToken.set(data.get("trackToken").asString)
                check(data.get("providerId").asString == "netease")
                sendResponse(connection, "api.provider_play_result", JsonObject().apply {
                    addProperty("requestId", data.get("requestId").asString)
                    addProperty("status", "ok")
                    add("playback", JsonObject().apply {
                        addProperty("requestId", UUID.randomUUID().toString())
                        add("song", JsonObject().apply {
                            addProperty("id", "track-1")
                            addProperty("source", "netease")
                            addProperty("title", "测试歌曲")
                            add("artists", JsonArray().apply { add("测试歌手") })
                        })
                        add("audio", JsonObject().apply {
                            addProperty("url", "https://media.example.test/song.mp3")
                        })
                    })
                })
            }
        }
    }

    override fun onClose(connection: WebSocket, code: Int, reason: String, remote: Boolean) = Unit

    override fun onError(connection: WebSocket?, exception: Exception) = Unit

    private fun sendResponse(connection: WebSocket, type: String, data: JsonObject) {
        connection.send(gson.toJson(JsonObject().apply {
            addProperty("id", UUID.randomUUID().toString())
            addProperty("type", type)
            addProperty("protocolVersion", 1)
            addProperty("timestamp", System.currentTimeMillis())
            add("data", data)
        }))
    }
}
