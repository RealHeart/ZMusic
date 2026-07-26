package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.PluginMessageListener
import me.zhenxin.zmusic.platform.entity.ZPlayer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.api.ProxyServer

/**
 * BungeeCord 插件消息实现。
 *
 * @author 真心
 * @since 5.0.0
 */
class BungeePluginMessenger(private val plugin: Plugin) : PluginMessenger, Listener {
    private val listeners = mutableMapOf<String, PluginMessageListener>()
    private var eventListenerRegistered = false
    /**
     * 注册代理端插件消息通道。
     *
     * @param channel 通道名
     */
    override fun registerChannel(channel: String, listener: PluginMessageListener) {
        ProxyServer.getInstance().registerChannel(channel)
        listeners[channel] = listener
        if (!eventListenerRegistered) {
            plugin.proxy.pluginManager.registerListener(plugin, this)
            eventListenerRegistered = true
        }
    }

    /**
     * 注销代理端插件消息通道。
     *
     * @param channel 通道名
     */
    override fun unregisterChannel(channel: String) {
        ProxyServer.getInstance().unregisterChannel(channel)
        listeners.remove(channel)
    }

    /**
     * 通过代理持有的玩家连接直接发送插件消息。
     *
     * @param player 目标玩家
     * @param channel 通道名
     * @param payload 消息内容
     */
    override fun send(player: ZPlayer, channel: String, payload: ByteArray) {
        val bungeePlayer = (player as? BungeePlayer)?.platformPlayer() ?: return
        bungeePlayer.sendData(channel, payload)
    }

    /**
     * 接收客户端发往代理端的 ZMusic 插件消息。
     *
     * @param event BungeeCord 插件消息事件
     */
    @EventHandler
    fun onPluginMessage(event: PluginMessageEvent) {
        val listener = listeners[event.tag] ?: return
        val player = event.sender as? ProxiedPlayer ?: return
        event.isCancelled = true
        listener.onMessage(BungeePlayer(player), event.data)
    }
}
