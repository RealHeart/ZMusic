package cn.iqianye.mc.zmusic.event;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.utils.OtherUtils;

public class Event {

    public static void onJoin(Object player) {
        ZMusic.runTask.runAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isAdmin = ZMusic.player.hasPermission(player, "zmusic.admin");
            if (isAdmin) {
                OtherUtils.checkUpdate(player);
            }
        });
    }
}
