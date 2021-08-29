package me.zhenxin.zmusic.module.taboolib.pluginmessage

import io.netty.buffer.Unpooled
import taboolib.common.platform.ProxyPlayer

/**
 * 插件消息多平台实现
 *
 * @author 真心
 * @since 2021/8/16 17:11
 * @email qgzhenxin@qq.com
 */
interface PluginMessage {

    fun registerChannel(channel: String)

    fun sendMessage(sender: ProxyPlayer, channel: String, data: ByteArray)

    fun encodeBytes(bytes: ByteArray): ByteArray {
        val buf = Unpooled.buffer(bytes.size + 1)
        buf.writeByte(666)
        buf.writeBytes(bytes)
        return buf.array()
    }
}