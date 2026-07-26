package me.zhenxin.zmusic.velocity

import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.PostLoginEvent
import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.PlayerListener
import me.zhenxin.zmusic.platform.entity.ZPlayer

/**
 * Velocity 玩家查询实现。
 *
 * @property server Velocity 代理服务
 * @author 真心
 * @since 5.0.0
 */
class VelocityPlayerGateway(
    private val server: ProxyServer,
    private val plugin: Any
) : PlayerGateway {
    private var listener: PlayerListener? = null

    /**
     * 返回代理端在线玩家。
     *
     * @return 在线玩家集合
     */
    override fun onlinePlayers(): Collection<ZPlayer> {
        return server.allPlayers.map(::VelocityPlayer)
    }

    /**
     * 按名称查询代理端玩家。
     *
     * @param name 玩家名
     * @return 找到的玩家；不存在时返回 null
     */
    override fun findByName(name: String): ZPlayer? {
        return server.getPlayer(name).orElse(null)?.let(::VelocityPlayer)
    }

    /** 注册 Velocity 玩家上下线监听。 */
    override fun registerListener(listener: PlayerListener) {
        if (this.listener == null) {
            server.eventManager.register(plugin, this)
        }
        this.listener = listener
    }

    /** 注销 Velocity 玩家上下线监听。 */
    override fun unregisterListener() {
        listener = null
        server.eventManager.unregisterListener(plugin, this)
    }

    /** @param event 玩家登录事件 */
    @Subscribe
    fun onJoin(event: PostLoginEvent) {
        listener?.onJoin(VelocityPlayer(event.player))
    }

    /** @param event 玩家退出事件 */
    @Subscribe
    fun onQuit(event: DisconnectEvent) {
        listener?.onQuit(VelocityPlayer(event.player))
    }
}
