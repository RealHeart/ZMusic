package me.zhenxin.zmusic.velocity

import com.velocitypowered.api.proxy.ProxyServer
import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.entity.ZPlayer

/**
 * Velocity 玩家查询实现。
 *
 * @property server Velocity 代理服务
 * @author 真心
 * @since 5.0.0
 */
class VelocityPlayerGateway(private val server: ProxyServer) : PlayerGateway {
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
}
