package me.zhenxin.zmusic.event;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventBC implements Listener {

    @EventHandler
    public void onPostLoginEvent(PostLoginEvent event) {
        Event.onJoin(event.getPlayer());
    }
}
