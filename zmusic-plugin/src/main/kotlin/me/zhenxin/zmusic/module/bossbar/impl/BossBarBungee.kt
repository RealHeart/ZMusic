package me.zhenxin.zmusic.module.bossbar.impl

import me.zhenxin.zmusic.module.bossbar.BossBar
import net.md_5.bungee.api.connection.ProxiedPlayer
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit
import java.util.*

/**
 * BossBar Bungee 实现
 *
 * @author 真心
 * @since 2022/7/6 10:57
 * @email qgzhenxin@qq.com
 */
class BossBarBungee(player: ProxyPlayer) : BossBar(player) {

    private val uuid = UUID.randomUUID()
    private var seconds = 0F

    override fun start() {
        if (player.isOnline()) {
            val packet = getPacket(0)
            packet.color = 1
            packet.division = 4
            packet.health = 0F
            submit(async = true) {
                val step = 1F / seconds
                var progress = 0F
                while (progress >= 0 || progress <= 1) {
                    progress += step
                    if (progress > 1) break
                    setProgress(progress)
                    Thread.sleep(1000)
                }
                remove()
            }
        }
    }

    override fun stop() {
        remove()
    }

    override fun setTime(seconds: Float) {
        this.seconds = seconds
    }

    override fun setTitle(title: String) {
        val paket = getPacket(3)
        paket.title = title
        player.cast<ProxiedPlayer>().unsafe().sendPacket(paket)
    }

    private fun setProgress(progress: Float) {
        val paket = getPacket(2)
        paket.health = progress
        player.cast<ProxiedPlayer>().unsafe().sendPacket(paket)
    }

    private fun remove() {
        val paket = getPacket(1)
        player.cast<ProxiedPlayer>().unsafe().sendPacket(paket)
    }

    private fun getPacket(action: Int) = net.md_5.bungee.protocol.packet.BossBar(uuid, action)
}