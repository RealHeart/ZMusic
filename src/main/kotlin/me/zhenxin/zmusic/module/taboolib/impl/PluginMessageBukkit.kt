package me.zhenxin.zmusic.module.taboolib.impl

import io.netty.buffer.Unpooled
import me.zhenxin.zmusic.module.taboolib.PluginMessage
import org.bukkit.entity.Player
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyPlayer
import taboolib.platform.BukkitPlugin


/**
 * 插件消息 Bukkit 实现
 *
 * @author 真心
 * @since 2021/8/16 17:20
 * @email qgzhenxin@qq.com
 */

@Suppress("unused")
@PlatformImplementation(Platform.BUKKIT)
class PluginMessageBukkit : PluginMessage {
    private val plugin by lazy { BukkitPlugin.getInstance() }
    override fun registerChannel(channel: String) {
        plugin.server.messenger.registerOutgoingPluginChannel(plugin, channel)
    }

    override fun sendMessage(sender: ProxyPlayer, channel: String, data: ByteArray) {
        val player by lazy { sender.cast<Player>() }
        player.sendPluginMessage(plugin, channel, encodeBytes(data))
    }
}