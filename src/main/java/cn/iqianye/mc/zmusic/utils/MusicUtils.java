package cn.iqianye.mc.zmusic.utils;

import cn.iqianye.mc.zmusic.api.NettyVersion;
import cn.iqianye.mc.zmusic.api.Version;
import cn.iqianye.mc.zmusic.mod.Send;
import cn.iqianye.mc.zmusic.other.Val;
import org.bukkit.entity.Player;

import java.util.List;

public class MusicUtils {

    static Version version = new Version();
    static NettyVersion nettyVersion = new NettyVersion();

    /**
     * 播放音乐(个人)
     *
     * @param url    音乐地址
     * @param player 玩家
     */
    public static void playSelf(String url, Player player) {
        Play(url, player);
    }

    /**
     * 播放音乐(全体)
     *
     * @param url        音乐地址
     * @param playerList 玩家列表
     */
    public static void playAll(String url, List<Player> playerList) {
        for (Player player : playerList) {
            Play(url, player);
        }
    }

    /**
     * 停止播放音乐(个人)
     *
     * @param player 玩家
     */
    public static void stopSelf(Player player) {
        Stop(player);
    }

    /**
     * 停止播放音乐(全体)
     *
     * @param playerList 玩家列表
     */
    public static void stopAll(List<Player> playerList) {
        for (Player player : playerList) {
            Stop(player);
        }
    }

    private static void Play(String url, Player player) {
        if (version.isHigherThan("1.12")) {
            Send.sendAM(player, "[Play]" + url);
        } else {
            if (Val.isViaVer) {
                if (nettyVersion.isHigherThan(340, player)) {
                    Send.sendAM(player, "[Play]" + url);
                } else {
                    Send.sendABF(player, "[Net]" + url);
                }
            } else {
                Send.sendABF(player, "[Net]" + url);
            }
        }
    }

    private static void Stop(Player player) {
        if (version.isHigherThan("1.12")) {
            Send.sendAM(player, "[Stop]");
        } else {
            if (Val.isViaVer) {
                if (nettyVersion.isHigherThan(340, player)) {
                    Send.sendAM(player, "[Stop]");
                } else {
                    Send.sendABF(player, "[Stop]");
                }
            } else {
                Send.sendABF(player, "[Stop]");
            }
        }
    }

}
