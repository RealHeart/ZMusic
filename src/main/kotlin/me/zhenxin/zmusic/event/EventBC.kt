package me.zhenxin.zmusic.event

import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

internal class EventBC : Listener {
    @EventHandler
    fun onPostLoginEvent(event: PostLoginEvent) {
        EventEx.onJoin(event.player)
    }
}
