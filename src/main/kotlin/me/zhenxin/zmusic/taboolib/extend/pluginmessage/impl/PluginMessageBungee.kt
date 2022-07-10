package me.zhenxin.zmusic.taboolib.extend.pluginmessage.impl

import me.zhenxin.zmusic.platform.bungeePlugin
import me.zhenxin.zmusic.taboolib.extend.pluginmessage.PluginMessage
import net.md_5.bungee.api.connection.ProxiedPlayer
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyPlayer

/**
 * 插件消息 Bungee 实现
 *
 * @author 真心
 * @since 2021/8/16 17:21
 * @email qgzhenxin@qq.com
 */

@Suppress("unused")
@PlatformImplementation(Platform.BUNGEE)
class PluginMessageBungee : PluginMessage {
    private val plugin by lazy { bungeePlugin }
    override fun registerChannel(channel: String) {
        plugin.proxy.registerChannel(channel)
    }

    override fun sendMessage(sender: ProxyPlayer, channel: String, data: ByteArray) {
        val player by lazy { sender.cast<ProxiedPlayer>() }
        player.sendData(channel, encodeBytes(data))
    }
}