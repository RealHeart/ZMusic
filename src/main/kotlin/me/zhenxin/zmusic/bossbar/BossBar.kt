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
     * 显示 BossBar
     */
    abstract fun show(title: String, seconds: Float)

    /**
     * 更新 BossBar 标题
     */
    abstract fun updateTitle(title: String)
}