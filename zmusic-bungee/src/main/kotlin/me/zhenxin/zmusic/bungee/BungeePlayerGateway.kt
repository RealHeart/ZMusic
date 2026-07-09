package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.entity.ZPlayer
import net.md_5.bungee.api.ProxyServer

/**
 * BungeeCord 玩家查询实现。
 *
 * @author 真心
 * @since 5.0.0
 */
class BungeePlayerGateway : PlayerGateway {
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
}
