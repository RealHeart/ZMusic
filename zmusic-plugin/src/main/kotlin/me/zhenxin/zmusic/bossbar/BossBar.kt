package me.zhenxin.zmusic.bossbar

import taboolib.common.platform.ProxyPlayer

/**
 * BossBar 跨平台实现
 *
 * @author 真心
 * @since 2022/7/5 14:01
 * @email qgzhenxin@qq.com
 */
abstract class BossBar(
    var player: ProxyPlayer
) {

    /**
     * 开始 BossBar
     */
    abstract fun start()

    /**
     * 停止 BossBar
     */
    abstract fun stop()

    /**
     * 设置 BossBar 时间
     */
    abstract fun setTime(seconds: Float)

    /**
     * 设置 BossBar 标题
     */
    abstract fun setTitle(title: String)
}