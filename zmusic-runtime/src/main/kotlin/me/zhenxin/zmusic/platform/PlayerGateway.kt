package me.zhenxin.zmusic.platform

import me.zhenxin.zmusic.platform.entity.ZPlayer

/**
 * 玩家查询网关。
 *
 * @author 真心
 * @since 5.0.0
 */
interface PlayerGateway {
    /**
     * 查询当前在线玩家。
     *
     * @return 在线玩家集合
     */
    fun onlinePlayers(): Collection<ZPlayer>

    /**
     * 按名称查找玩家。
     *
     * @param name 玩家名
     * @return 找到的玩家；不存在时返回 null
     */
    fun findByName(name: String): ZPlayer?
}
