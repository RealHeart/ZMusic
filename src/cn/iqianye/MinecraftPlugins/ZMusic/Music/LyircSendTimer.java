package cn.iqianye.MinecraftPlugins.ZMusic.Music;

import cn.iqianye.MinecraftPlugins.ZMusic.Player.PlayerStatus;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class LyircSendTimer extends TimerTask {
    public Player player;
    public List<Map<Long, String>> list;
    public int maxTime;
    int time = 0;

    @Override
    public void run() {
        time++;
        if (PlayerStatus.getPlayerStatus(player)) {
            if (time != maxTime) {
                for (Map<Long, String> map : list) {
                    for (Map.Entry<Long, String> entry : map.entrySet()) {
                        if (entry.getKey() == time) {
                            if (!entry.getValue().isEmpty()) {
                                ActionBarAPI.sendActionBar(player, "§b§l" + entry.getValue());
                            }
                        }
                    }
                }
            } else {
                PlayerStatus.setPlayerStatus(player, false);
                cancel();
            }
        } else {
            cancel();
        }
    }

}