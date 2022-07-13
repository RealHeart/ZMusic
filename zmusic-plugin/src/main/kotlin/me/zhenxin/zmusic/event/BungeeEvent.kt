package me.zhenxin.zmusic.event

import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptPlayer

/**
 * Bungee 事件监听
 *
 * @author 真心
 * @since 2021/9/27 21:14
 * @email qgzhenxin@qq.com
 */
@Suppress("unused")
object BungeeEvent {
    @SubscribeEvent
    fun onPlayerJoin(event: PostLoginEvent) = EventEx.onPlayerJoin(adaptPlayer(event.player))

    @SubscribeEvent
    fun onPlayerQuit(event: PlayerDisconnectEvent) = EventEx.onPlayerQuit(adaptPlayer(event.player))
}