package me.zhenxin.zmusic.module.event

import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * Bukkit 事件监听
 *
 * @author 真心
 * @since 2021/8/16 18:01
 * @email qgzhenxin@qq.com
 */

object BukkitEvent {
    @SubscribeEvent
    fun onPlayerJoin(event: PlayerJoinEvent) {
        println(event.player.name)
    }
}