package me.zhenxin.zmusic.module.bossbar.impl

import me.zhenxin.zmusic.module.bossbar.BossBar
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit
import taboolib.platform.BukkitPlugin

/**
 * BossBar Bukkit实现
 *
 * @author 真心
 * @since 2022/7/5 14:05
 * @email qgzhenxin@qq.com
 */
@PlatformImplementation(Platform.BUKKIT)
class BossBarBukkit(player: ProxyPlayer) : BossBar(player) {
    private val bukkitPlugin = BukkitPlugin.getInstance()

    private val bar = bukkitPlugin.server.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_20)
    private var seconds = 0F

    override fun start() {
        if (player.isOnline()) {
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
                    if (bar.isVisible) {
                        Thread.sleep(1000)
                    } else {
                        break
                    }
                }
                stop()
            }
        }
    }

    override fun stop() {
        bar.isVisible = false
        bar.removeAll()
    }

    override fun setTime(seconds: Float) {
        this.seconds = seconds
    }

    override fun setTitle(title: String) {
        bar.setTitle(title)
    }
}