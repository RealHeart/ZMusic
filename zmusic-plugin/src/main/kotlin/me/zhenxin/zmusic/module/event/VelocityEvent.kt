package me.zhenxin.zmusic.module.event

import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.PostLoginEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptPlayer

/**
 * Velocity 事件监听
 *
 * @author 真心
 * @since 2021/9/27 21:14
 * @email qgzhenxin@qq.com
 */
@Suppress("unused")
object VelocityEvent {
    @SubscribeEvent
    fun onPlayerJoin(event: PostLoginEvent) = EventEx.onPlayerJoin(adaptPlayer(event.player))

    @SubscribeEvent
    fun onPlayerQuit(event: DisconnectEvent) = EventEx.onPlayerQuit(adaptPlayer(event.player))
}