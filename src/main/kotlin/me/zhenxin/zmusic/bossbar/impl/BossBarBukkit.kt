package me.zhenxin.zmusic.bossbar.impl

import me.zhenxin.zmusic.platform.bukkitPlugin
import me.zhenxin.zmusic.bossbar.BossBar
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit

/**
 * BossBar Bukkit实现
 *
 * @author 真心
 * @since 2022/7/5 14:05
 * @email qgzhenxin@qq.com
 */
@PlatformImplementation(Platform.BUKKIT)
class BossBarBukkit(player: ProxyPlayer) : BossBar(player) {

    private val bar = bukkitPlugin.server.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_20)

    override fun show(title: String, seconds: Float) {
        if (player.isOnline()) {
            updateTitle(title)
            bar.isVisible = true
            bar.progress = 0.0
            bar.addPlayer(player.cast())
            submit(async = true) {
                val step = 1F / seconds
                var progress = bar.progress
                while (progress >= 0 || progress <= 1) {
                    progress += step
                    if (progress > 1) break
                    bar.progress = progress
                    Thread.sleep(1000)
                }
                bar.isVisible = false
            }
        }
    }

    override fun updateTitle(title: String) {
        bar.setTitle(title)
    }
}