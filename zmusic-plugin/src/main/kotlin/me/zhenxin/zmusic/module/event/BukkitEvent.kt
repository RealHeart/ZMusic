package me.zhenxin.zmusic.module.event

import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptPlayer

/**
 * Bukkit 事件监听
 *
 * @author 真心
 * @since 2021/8/16 18:01
 * @email qgzhenxin@qq.com
 */

@Suppress("unused")
@PlatformSide([Platform.BUKKIT])
object BukkitEvent {
    @SubscribeEvent
    fun onPlayerJoin(event: PlayerJoinEvent) = EventEx.onPlayerJoin(adaptPlayer(event.player))

    @SubscribeEvent
    fun onPlayerQuit(event: PlayerQuitEvent) = EventEx.onPlayerQuit(adaptPlayer(event.player))
}