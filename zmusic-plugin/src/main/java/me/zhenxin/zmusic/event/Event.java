package me.zhenxin.zmusic.event;

import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.utils.OtherUtils;

public class Event {

    public static void onJoin(Object player) {
        ZMusic.runTask.runAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            boolean isAdmin = ZMusic.player.hasPermission(player, "zmusic.admin");
            if (isAdmin) {
                if (ZMusic.notice != null) {
                    ZMusic.notice.sendUnread(player);
                }
                OtherUtils.checkUpdate(player, true);
            }
        });
    }
}
