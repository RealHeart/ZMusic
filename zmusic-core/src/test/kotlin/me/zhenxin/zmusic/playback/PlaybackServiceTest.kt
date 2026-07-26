package me.zhenxin.zmusic.playback

import com.google.gson.JsonObject
import me.zhenxin.zmusic.platform.CommandRegistry
import me.zhenxin.zmusic.platform.Platform
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.PlatformLogger
import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.PluginMessageListener
import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.Scheduler
import me.zhenxin.zmusic.platform.entity.ZPlayer
import me.zhenxin.zmusic.protocol.PacketCodec
import me.zhenxin.zmusic.protocol.PacketEnvelope
import me.zhenxin.zmusic.protocol.PacketProtocol
import java.io.File
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * [PlaybackService] 的客户端包协议映射测试。
 *
 * @author 真心
 * @since 5.0.0
 */
class PlaybackServiceTest {
    @Test
    fun `maps websocket admin stop reason to packet command reason`() {
        val fixture = Fixture()
        fixture.states.markReady(fixture.player, validHello())

        assertTrue(fixture.service.stop(fixture.player, JsonObject().apply { addProperty("reason", "admin_stop") }))

        val message = PacketCodec.decode(fixture.messenger.sent.single())
        assertEquals("server.stop", message.type)
        assertEquals("command", message.data.get("reason").asString)
    }

    @Test
    fun `returns invalid payload with reply id for malformed hello`() {
        val fixture = Fixture()
        val messageId = UUID.randomUUID().toString()
        val hello = validHello().apply { addProperty("playerClientId", "invalid") }

        fixture.receive(PacketEnvelope(messageId, "client.hello", 1, hello))

        fixture.assertError(messageId, "invalid_payload")
    }

    @Test
    fun `returns unsupported protocol only for unsupported version`() {
        val fixture = Fixture()
        val messageId = UUID.randomUUID().toString()
        val hello = validHello().apply { addProperty("protocolVersion", PacketProtocol.VERSION + 1) }

        fixture.receive(PacketEnvelope(messageId, "client.hello", 1, hello))

        fixture.assertError(messageId, "unsupported_protocol")
    }

    @Test
    fun `returns unsupported message with reply id`() {
        val fixture = Fixture()
        val messageId = UUID.randomUUID().toString()

        fixture.receive(PacketEnvelope(messageId, "client.unknown", 1, JsonObject()))

        fixture.assertError(messageId, "unsupported_message")
    }

    private class Fixture {
        val player = FakePlayer()
        val messenger = FakeMessenger()
        val states = PlayerStateRegistry()
        val service = PlaybackService(FakeContext(messenger, player), states)

        init {
            service.start(PacketProtocol.CHANNEL) { _, _, _ -> }
        }

        fun receive(envelope: PacketEnvelope) {
            messenger.listener.onMessage(player, PacketCodec.encode(envelope))
        }

        fun assertError(replyTo: String, code: String) {
            val message = PacketCodec.decode(messenger.sent.single())
            assertEquals("server.error", message.type)
            assertEquals(replyTo, message.data.get("replyTo").asString)
            assertEquals(code, message.data.get("code").asString)
        }
    }
}

private fun validHello() = JsonObject().apply {
    addProperty("protocolVersion", PacketProtocol.VERSION)
    addProperty("modVersion", "5.0.0")
    addProperty("minecraftVersion", "1.21.1")
    addProperty("loader", "fabric")
    addProperty("playerClientId", UUID.randomUUID().toString())
}

private class FakeMessenger : PluginMessenger {
    lateinit var listener: PluginMessageListener
    val sent = mutableListOf<ByteArray>()

    override fun registerChannel(channel: String, listener: PluginMessageListener) {
        this.listener = listener
    }

    override fun unregisterChannel(channel: String) = Unit

    override fun send(player: ZPlayer, channel: String, payload: ByteArray) {
        sent += payload
    }
}

private class FakeContext(
    override val pluginMessenger: PluginMessenger,
    private val player: ZPlayer
) : PlatformContext {
    override val platform = Platform.BUKKIT
    override val platformVersion = "test"
    override val dataFolder = File("build/test-data")
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
    override val scheduler = object : Scheduler {
        override fun async(task: () -> Unit) = task()
        override fun main(task: () -> Unit) = task()
    }
}

private class FakePlayer : ZPlayer {
    override val uniqueId = UUID.randomUUID()
    override val name = "TestPlayer"
    override fun hasPermission(permission: String) = true
    override fun sendMessage(message: String) = Unit
}
