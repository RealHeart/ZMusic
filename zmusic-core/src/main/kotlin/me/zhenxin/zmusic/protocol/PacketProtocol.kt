package me.zhenxin.zmusic.protocol

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.nio.charset.StandardCharsets
import java.util.UUID

/**
 * ZMusic Minecraft 自定义负载协议常量。
 *
 * @author 真心
 * @since 5.0.0
 */
object PacketProtocol {
    const val CHANNEL = "zmusic:packet"
    const val VERSION = 1
    const val MAX_PACKET_BYTES = 32760
    const val MAX_JSON_BYTES = MAX_PACKET_BYTES - 5
    val MAGIC = byteArrayOf('Z'.code.toByte(), 'M'.code.toByte(), 'P'.code.toByte(), 'K'.code.toByte())
}

/**
 * Minecraft 通信包的统一 JSON envelope。
 *
 * @property id 消息 UUID
 * @property type 消息类型
 * @property timestamp Unix 毫秒时间戳
 * @property data 业务对象
 * @author 真心
 * @since 5.0.0
 */
data class PacketEnvelope(
    val id: String,
    val type: String,
    val timestamp: Long,
    val data: JsonObject
)

/**
 * 编解码 `ZMPK + version + JSON` 帧。
 *
 * @author 真心
 * @since 5.0.0
 */
object PacketCodec {
    private val gson = Gson()

    /**
     * 创建并编码一条协议消息。
     *
     * @param type 消息类型
     * @param data 业务数据
     * @return 完整二进制帧
     * @throws ProtocolException 消息超限时抛出
     */
    fun encode(type: String, data: JsonObject): ByteArray {
        return encode(PacketEnvelope(UUID.randomUUID().toString(), type, System.currentTimeMillis(), data))
    }

    /**
     * 编码统一 envelope。
     *
     * @param envelope 待编码消息
     * @return 完整二进制帧
     * @throws ProtocolException 消息字段或大小无效时抛出
     */
    fun encode(envelope: PacketEnvelope): ByteArray {
        validateEnvelope(envelope)
        val json = gson.toJson(envelope).toByteArray(StandardCharsets.UTF_8)
        if (json.size > PacketProtocol.MAX_JSON_BYTES) {
            throw ProtocolException("JSON payload exceeds ${PacketProtocol.MAX_JSON_BYTES} bytes")
        }
        return PacketProtocol.MAGIC + byteArrayOf(PacketProtocol.VERSION.toByte()) + json
    }

    /**
     * 解码并校验一条完整协议帧。
     *
     * @param payload 原始二进制帧
     * @return 已校验 envelope
     * @throws ProtocolException 帧结构或 JSON 无效时抛出
     */
    fun decode(payload: ByteArray): PacketEnvelope {
        if (payload.size < 5 || payload.size > PacketProtocol.MAX_PACKET_BYTES) {
            throw ProtocolException("Invalid packet length: ${payload.size}")
        }
        if (!payload.copyOfRange(0, 4).contentEquals(PacketProtocol.MAGIC)) {
            throw ProtocolException("Invalid packet magic")
        }
        if (payload[4].toInt() and 0xff != PacketProtocol.VERSION) {
            throw ProtocolException("Unsupported frame version: ${payload[4].toInt() and 0xff}")
        }
        val root = try {
            JsonParser.parseString(String(payload, 5, payload.size - 5, StandardCharsets.UTF_8)).asJsonObject
        } catch (exception: RuntimeException) {
            throw ProtocolException("Invalid packet JSON", exception)
        }
        val envelope = try {
            PacketEnvelope(
                id = root.get("id").asString,
                type = root.get("type").asString,
                timestamp = root.get("timestamp").asLong,
                data = root.getAsJsonObject("data")
            )
        } catch (exception: RuntimeException) {
            throw ProtocolException("Packet envelope is missing required fields", exception)
        }
        validateEnvelope(envelope)
        return envelope
    }

    private fun validateEnvelope(envelope: PacketEnvelope) {
        runCatching { UUID.fromString(envelope.id) }
            .getOrElse { throw ProtocolException("Packet id must be a UUID", it) }
        if (envelope.type.isBlank() || envelope.type.length > 128) {
            throw ProtocolException("Invalid packet type")
        }
    }
}

/**
 * 表示可安全拒绝的通信包校验错误。
 *
 * @author 真心
 * @since 5.0.0
 */
class ProtocolException(message: String, cause: Throwable? = null) : IllegalArgumentException(message, cause)
