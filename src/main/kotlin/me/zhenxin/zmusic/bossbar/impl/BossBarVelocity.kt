package me.zhenxin.zmusic.bossbar.impl

import com.velocitypowered.api.proxy.Player
import me.zhenxin.zmusic.bossbar.BossBar
import net.kyori.adventure.text.Component
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit

/**
 * BossBar Velocity 实现
 *
 * @author 真心
 * @since 2022/7/6 11:09
 * @email qgzhenxin@qq.com
 */
class BossBarVelocity(player: ProxyPlayer) : BossBar(player) {

    private val bar = net.kyori.adventure.bossbar.BossBar.bossBar(
        Component.text(),
        0F,
        net.kyori.adventure.bossbar.BossBar.Color.BLUE,
        net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_20
    )

    override fun show(title: String, seconds: Float) {
        updateTitle(title)
        val player = player.cast<Player>()
        player.showBossBar(bar)
        submit(async = true) {
            val step = 1F / seconds
            var progress = bar.progress()
            while (progress >= 0 || progress <= 1) {
                progress += step
                if (progress > 1) break
                bar.progress(progress)
                Thread.sleep(1000)
            }
            player.hideBossBar(bar)
        }
    }

    override fun updateTitle(title: String) {
        bar.name(Component.text(title))
    }
}