package me.zhenxin.zmusic.data

import me.zhenxin.zmusic.bossbar.BossBar
import me.zhenxin.zmusic.bossbar.impl.BossBarBukkit
import taboolib.common.platform.ProxyPlayer

/**
 * 玩家状态
 *
 * @author 真心
 * @since 2022/7/5 14:30
 * @email qgzhenxin@qq.com
 */
object PlayerState {
    val BOSS_BAR = mutableMapOf<ProxyPlayer, BossBar>()
}