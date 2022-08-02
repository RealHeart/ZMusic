package me.zhenxin.zmusic.module.bossbar.impl

import me.zhenxin.zmusic.module.bossbar.BossBar
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.chat.ComponentSerializer
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
class BossBarBungee(private val player: ProxyPlayer) : BossBar() {

    private val uuid = UUID.randomUUID()
    private var seconds = 0F

    private val bungeePlayer = player.cast<ProxiedPlayer>()

    override fun start() {
        if (player.isOnline()) {
            val packet = getPacket(0)
            packet.title = ComponentSerializer.toString(TextComponent(""))
            packet.color = 1
            packet.division = 4
            packet.health = 0F
            sendPacket(packet)
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
        val packet = getPacket(3)
        packet.title = ComponentSerializer.toString(TextComponent(title))
        sendPacket(packet)
    }

    private fun setProgress(progress: Float) {
        val packet = getPacket(2)
        packet.health = progress
        sendPacket(packet)
    }

    private fun remove() {
        val packet = getPacket(1)
        sendPacket(packet)
    }

    private fun getPacket(action: Int) = net.md_5.bungee.protocol.packet.BossBar(uuid, action)

    private fun sendPacket(packet: net.md_5.bungee.protocol.packet.BossBar) = bungeePlayer.unsafe().sendPacket(packet)
}