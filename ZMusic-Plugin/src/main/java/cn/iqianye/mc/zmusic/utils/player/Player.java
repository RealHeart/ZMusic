package cn.iqianye.mc.zmusic.utils.player;

import java.util.List;

public interface Player {

    boolean hasPermission(Object playerObj, String permission);

    List<Object> getOnlinePlayerList();

    boolean isOnline(Object playerObj);
}
