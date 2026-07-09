package me.zhenxin.zmusic.bukkit

import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.entity.ZPlayer
import org.bukkit.plugin.java.JavaPlugin

/**
 * Bukkit 插件消息实现。
 *
 * @property plugin Bukkit 插件实例
 * @author 真心
 * @since 5.0.0
 */
class BukkitPluginMessenger(private val plugin: JavaPlugin) : PluginMessenger {
    /**
     * 注册出站和入站通道。
     *
     * @param channel 通道名
     */
    override fun registerChannel(channel: String) {
        plugin.server.messenger.registerOutgoingPluginChannel(plugin, channel)
        plugin.server.messenger.registerIncomingPluginChannel(plugin, channel) { _, _, _ -> }
    }

    /**
     * 注销出站和入站通道。
     *
     * @param channel 通道名
     */
    override fun unregisterChannel(channel: String) {
        plugin.server.messenger.unregisterOutgoingPluginChannel(plugin, channel)
        plugin.server.messenger.unregisterIncomingPluginChannel(plugin, channel)
    }

    /**
     * 向 Bukkit 玩家发送插件消息。
     *
     * @param player 目标玩家
     * @param channel 通道名
     * @param payload 消息内容
     */
    override fun send(player: ZPlayer, channel: String, payload: ByteArray) {
        val bukkitPlayer = (player as? BukkitPlayer)?.platformPlayer() ?: return
        bukkitPlayer.sendPluginMessage(plugin, channel, payload)
    }
}
