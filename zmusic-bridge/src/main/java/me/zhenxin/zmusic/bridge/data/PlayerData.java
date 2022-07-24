package me.zhenxin.zmusic.bridge.data;

import me.zhenxin.zmusic.bridge.entity.MusicInfo;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2022/07/24 12:20
 */
public class PlayerData {
    private static Map<Player, MusicInfo> musicInfo;

    public static String getName(Player player) {
        MusicInfo info = getMusicInfo(player);
        return info.getName();
    }

    public static String getSinger(Player player) {
        MusicInfo info = getMusicInfo(player);
        return info.getSinger();
    }

    public static String getLyric(Player player) {
        MusicInfo info = getMusicInfo(player);
        return info.getLyric();
    }

    public static Long getCurrentTime(Player player) {
        MusicInfo info = getMusicInfo(player);
        return info.getCurrentTime();
    }

    public static Long getMaxTime(Player player) {
        MusicInfo info = getMusicInfo(player);
        return info.getMaxTime();
    }

    public static void setName(Player player, String name) {
        MusicInfo info = getMusicInfo(player);
        info.setName(name);
    }

    public static void setSinger(Player player, String singer) {
        MusicInfo info = getMusicInfo(player);
        info.setSinger(singer);
    }

    public static void setLyric(Player player, String lyric) {
        MusicInfo info = getMusicInfo(player);
        info.setLyric(lyric);
    }

    public static void setCurrentTime(Player player, Long currentTime) {
        MusicInfo info = getMusicInfo(player);
        info.setCurrentTime(currentTime);
    }

    public static void setMaxTime(Player player, Long maxTime) {
        MusicInfo info = getMusicInfo(player);
        info.setMaxTime(maxTime);
    }

    private static MusicInfo getMusicInfo(Player player) {
        MusicInfo info = musicInfo.get(player);
        if (info == null) {
            info = new MusicInfo();
        }
        return info;
    }
}
