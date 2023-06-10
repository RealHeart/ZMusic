package me.zhenxin.zmusic.utils.player;

import me.zhenxin.zmusic.ZMusicBC;
import net.md_5.bungee.api.CommandSender;
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

    @Override
    public boolean isPlayer(Object sender) {
        return sender instanceof ProxiedPlayer;
    }

    @Override
    public String getName(Object sender) {
        return ((CommandSender) sender).getName();
    }
}
