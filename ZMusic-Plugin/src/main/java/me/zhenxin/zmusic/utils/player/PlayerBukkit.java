package me.zhenxin.zmusic.utils.player;

import me.zhenxin.zmusic.ZMusicBukkit;
import org.bukkit.command.CommandSender;

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

    @Override
    public boolean isPlayer(Object sender) {
        return sender instanceof org.bukkit.entity.Player;
    }

    @Override
    public String getName(Object sender) {
        return ((CommandSender) sender).getName();
    }

}
