package me.zhenxin.zmusic.bukkit

import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.entity.ZPlayer
import org.bukkit.Bukkit

/**
 * Bukkit 玩家查询实现。
 *
 * @author 真心
 * @since 5.0.0
 */
class BukkitPlayerGateway : PlayerGateway {
    /**
     * 返回 Bukkit 当前在线玩家。
     *
     * @return 在线玩家集合
     */
    override fun onlinePlayers(): Collection<ZPlayer> {
        return Bukkit.getOnlinePlayers().map(::BukkitPlayer)
    }

    /**
     * 按精确名称查询 Bukkit 玩家。
     *
     * @param name 玩家名
     * @return 找到的玩家；不存在时返回 null
     */
    override fun findByName(name: String): ZPlayer? {
        return Bukkit.getPlayerExact(name)?.let(::BukkitPlayer)
    }
}
