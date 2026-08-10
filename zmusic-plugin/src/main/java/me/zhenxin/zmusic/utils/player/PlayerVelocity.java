package me.zhenxin.zmusic.utils.player;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import me.zhenxin.zmusic.ZMusicVelocity;

import java.util.ArrayList;
import java.util.List;

public class PlayerVelocity implements me.zhenxin.zmusic.utils.player.Player {

    @Override
    public boolean hasPermission(Object playerObj, String permission) {
        if (playerObj instanceof Player) {
            return ((Player) playerObj).hasPermission(permission);
        }
        return false;
    }

    @Override
    public List<Object> getOnlinePlayerList() {
        return new ArrayList<>(ZMusicVelocity.server.getAllPlayers());
    }

    @Override
    public boolean isOnline(Object playerObj) {
        if (playerObj instanceof Player) {
            return ((Player) playerObj).isActive();
        }
        return false;
    }

    @Override
    public boolean isPlayer(Object sender) {
        return sender instanceof Player;
    }

    @Override
    public String getName(Object sender) {
        if (sender instanceof Player) {
            return ((Player) sender).getUsername();
        } else if (sender instanceof CommandSource) {
            return "Console";
        }
        return "Unknown";
    }

    @Override
    public String getUniqueId(Object playerObj) {
        return ((Player) playerObj).getUniqueId().toString();
    }
}
