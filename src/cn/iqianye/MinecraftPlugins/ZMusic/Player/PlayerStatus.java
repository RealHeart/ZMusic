package cn.iqianye.MinecraftPlugins.ZMusic.Player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatus {

    private static Map<Player, Boolean> playingMap = new HashMap<>();


    public static boolean getPlayerStatus(Player player) {
        return playingMap.get(player);
    }

    public static void setPlayerStatus(Player player, boolean playing) {
        playingMap.put(player, playing);
    }
}
