package cn.iqianye.mc.zmusic.utils.player;

import cn.iqianye.mc.zmusic.ZMusicBC;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerBC implements Player {

    @Override
    public boolean hasPermission(Object playerObj, String permission) {
        ProxiedPlayer player = (ProxiedPlayer) playerObj;
        return player.hasPermission(permission);
    }

    @Override
    public List<Object> getOnlinePlayerList() {
        return new ArrayList<>(ZMusicBC.plugin.getProxy().getPlayers());
    }

    @Override
    public boolean isOnline(Object playerObj) {
        ProxiedPlayer player = (ProxiedPlayer) playerObj;
        return player.isConnected();
    }
}
