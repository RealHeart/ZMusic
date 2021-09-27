package me.zhenxin.zmusic.module.event

import com.velocitypowered.api.event.connection.PostLoginEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptPlayer

/**
 * Bukkit 事件监听
 *
 * @author 真心
 * @since 2021/8/16 18:01
 * @email qgzhenxin@qq.com
 */

object VelocityEvent {
    @SubscribeEvent
    fun onPlayerJoin(event: PostLoginEvent) = EventEx.onPlayerJoin(adaptPlayer(event.player))
}