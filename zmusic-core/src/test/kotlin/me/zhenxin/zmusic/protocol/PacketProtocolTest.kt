package me.zhenxin.zmusic.protocol

import com.google.gson.JsonObject
import java.nio.charset.StandardCharsets
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * [PacketCodec] 的帧兼容性与边界测试。
 *
 * @author 真心
 * @since 5.0.0
 */
class PacketProtocolTest {
    @Test
    fun `round trips valid envelope`() {
        val envelope = PacketEnvelope(
            UUID.randomUUID().toString(),
            "client.hello",
            1234,
            JsonObject().apply { addProperty("protocolVersion", 1) }
        )

        assertEquals(envelope, PacketCodec.decode(PacketCodec.encode(envelope)))
    }

    @Test
    fun `rejects invalid magic`() {
        val packet = PacketCodec.encode("client.hello", JsonObject())
        packet[0] = 'X'.code.toByte()

        assertFailsWith<ProtocolException> { PacketCodec.decode(packet) }
    }

    @Test
    fun `rejects unsupported frame version`() {
        val packet = PacketCodec.encode("client.hello", JsonObject())
        packet[4] = 2

        assertFailsWith<ProtocolException> { PacketCodec.decode(packet) }
    }

    @Test
    fun `rejects oversized JSON payload`() {
        val data = JsonObject().apply { addProperty("value", "x".repeat(PacketProtocol.MAX_JSON_BYTES)) }

        assertFailsWith<ProtocolException> { PacketCodec.encode("client.error", data) }
    }

    @Test
    fun `rejects envelope with missing data`() {
        val json = """{"id":"${UUID.randomUUID()}","type":"client.hello","timestamp":1234}"""
            .toByteArray(StandardCharsets.UTF_8)
        val packet = PacketProtocol.MAGIC + byteArrayOf(PacketProtocol.VERSION.toByte()) + json

        assertFailsWith<ProtocolException> { PacketCodec.decode(packet) }
    }
}
