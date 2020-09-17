package cn.iqianye.mc.zmusic.utils.player;

import cn.iqianye.mc.zmusic.ZMusicBukkit;

import java.util.ArrayList;
import java.util.List;

public class PlayerBukkit implements Player {

    @Override
    public boolean hasPermission(Object playerObj, String permission) {
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) playerObj;
        return player.hasPermission(permission);
    }

    @Override
    public List<Object> getOnlinePlayerList() {
        return new ArrayList<>(ZMusicBukkit.plugin.getServer().getOnlinePlayers());
    }

    @Override
    public boolean isOnline(Object playerObj) {
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) playerObj;
        return player.isOnline();
    }

}
