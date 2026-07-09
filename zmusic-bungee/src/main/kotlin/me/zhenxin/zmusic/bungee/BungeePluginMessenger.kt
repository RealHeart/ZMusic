package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.entity.ZPlayer
import net.md_5.bungee.api.ProxyServer

/**
 * BungeeCord 插件消息实现。
 *
 * @author 真心
 * @since 5.0.0
 */
class BungeePluginMessenger : PluginMessenger {
    /**
     * 注册代理端插件消息通道。
     *
     * @param channel 通道名
     */
    override fun registerChannel(channel: String) {
        ProxyServer.getInstance().registerChannel(channel)
    }

    /**
     * 注销代理端插件消息通道。
     *
     * @param channel 通道名
     */
    override fun unregisterChannel(channel: String) {
        ProxyServer.getInstance().unregisterChannel(channel)
    }

    /**
     * 通过玩家当前连接的后端服务器发送插件消息。
     *
     * @param player 目标玩家
     * @param channel 通道名
     * @param payload 消息内容
     */
    override fun send(player: ZPlayer, channel: String, payload: ByteArray) {
        val bungeePlayer = (player as? BungeePlayer)?.platformPlayer() ?: return
        bungeePlayer.server?.sendData(channel, payload)
    }
}
