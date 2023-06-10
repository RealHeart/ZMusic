package me.zhenxin.zmusic.utils.player;

import java.util.List;

public interface Player {

    boolean hasPermission(Object playerObj, String permission);

    List<Object> getOnlinePlayerList();

    boolean isOnline(Object playerObj);

    boolean isPlayer(Object sender);

    String getName(Object sender);
}
