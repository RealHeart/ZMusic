package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import com.locydragon.abf.api.AudioBufferAPI;
import org.bukkit.entity.Player;

import java.util.List;

public class MusicUtils {
    /**
     * 播放音乐(个人)
     *
     * @param url    音乐地址
     * @param player 玩家
     */
    public static void playSelf(String url, Player player) {
       AudioBufferAPI.playForByParam(player, "[Net]" + url);
    }

    /**
     * 播放音乐(全体)
     *
     * @param url        音乐地址
     * @param playerList 玩家列表
     */
    public static void playAll(String url, List<Player> playerList) {
        for (Player player : playerList) {
            if (!AudioBufferAPI.playForByParam(player, "[Net]" + url)) {
                MessageUtils.sendErrorMessage("播放失败.", player);
                OtherUtils.resetPlayerStatus(player);
            }
        }
    }

    /**
     * 停止播放音乐(个人)
     *
     * @param player 玩家
     */
    public static void stopSelf(Player player) {
        AudioBufferAPI.stopPlaying(player);
    }

    /**
     * 停止播放音乐(全体)
     *
     * @param playerList 玩家列表
     */
    public static void stopAll(List<Player> playerList) {
        for (Player player : playerList) {
            AudioBufferAPI.stopPlaying(player);
        }
    }

}
