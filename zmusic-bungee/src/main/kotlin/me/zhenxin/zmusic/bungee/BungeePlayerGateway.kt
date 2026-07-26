package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.PlayerListener
import me.zhenxin.zmusic.platform.entity.ZPlayer
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler

/**
 * BungeeCord 玩家查询实现。
 *
 * @author 真心
 * @since 5.0.0
 */
class BungeePlayerGateway(private val plugin: Plugin) : PlayerGateway, Listener {
    private var listener: PlayerListener? = null

    /**
     * 返回代理端在线玩家。
     *
     * @return 在线玩家集合
     */
    override fun onlinePlayers(): Collection<ZPlayer> {
        return ProxyServer.getInstance().players.map(::BungeePlayer)
    }

    /**
     * 按名称查询代理端玩家。
     *
     * @param name 玩家名
     * @return 找到的玩家；不存在时返回 null
     */
    override fun findByName(name: String): ZPlayer? {
        return ProxyServer.getInstance().getPlayer(name)?.let(::BungeePlayer)
    }

    /** 注册 BungeeCord 玩家上下线监听。 */
    override fun registerListener(listener: PlayerListener) {
        if (this.listener == null) {
            plugin.proxy.pluginManager.registerListener(plugin, this)
        }
        this.listener = listener
    }

    /** 注销 BungeeCord 玩家上下线监听。 */
    override fun unregisterListener() {
        listener = null
        plugin.proxy.pluginManager.unregisterListener(this)
    }

    /** @param event 玩家登录事件 */
    @EventHandler
    fun onJoin(event: PostLoginEvent) {
        listener?.onJoin(BungeePlayer(event.player))
    }

    /** @param event 玩家退出事件 */
    @EventHandler
    fun onQuit(event: PlayerDisconnectEvent) {
        listener?.onQuit(BungeePlayer(event.player))
    }
}
