package me.zhenxin.zmusic.event

import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptPlayer

/**
 * Bukkit 事件监听
 *
 * @author 真心
 * @since 2021/8/16 18:01
 * @email qgzhenxin@qq.com
 */

object BukkitEvent {
    @SubscribeEvent
    fun onPlayerJoin(event: PlayerJoinEvent) = EventEx.onPlayerJoin(adaptPlayer(event.player))
}