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

    /**
     * 注册玩家上下线监听器。
     *
     * @param listener 玩家生命周期监听器
     */
    fun registerListener(listener: PlayerListener) = Unit

    /** 注销此前注册的玩家上下线监听器。 */
    fun unregisterListener() = Unit
}

/**
 * 玩家上下线事件回调。
 *
 * @author 真心
 * @since 5.0.0
 */
interface PlayerListener {
    /**
     * 玩家完成登录。
     *
     * @param player 已上线玩家
     */
    fun onJoin(player: ZPlayer)

    /**
     * 玩家断开连接。
     *
     * @param player 已离线玩家
     */
    fun onQuit(player: ZPlayer)
}
