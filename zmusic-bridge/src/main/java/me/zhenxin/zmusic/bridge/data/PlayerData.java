package me.zhenxin.zmusic.bridge.data;

import me.zhenxin.zmusic.bridge.entity.MusicInfo;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2022/07/24 12:20
 */
public class PlayerData {
    private static final Map<Player, MusicInfo> musicInfo = new HashMap<>();

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

    public static Integer getCurrentTime(Player player) {
        MusicInfo info = getMusicInfo(player);
        return info.getCurrentTime();
    }

    public static Integer getMaxTime(Player player) {
        MusicInfo info = getMusicInfo(player);
        return info.getMaxTime();
    }

    public static void setName(Player player, String name) {
        MusicInfo info = getMusicInfo(player);
        info.setName(name);
        setMusicInfo(player, info);
    }

    public static void setSinger(Player player, String singer) {
        MusicInfo info = getMusicInfo(player);
        info.setSinger(singer);
        setMusicInfo(player, info);
    }

    public static void setLyric(Player player, String lyric) {
        MusicInfo info = getMusicInfo(player);
        info.setLyric(lyric);
        setMusicInfo(player, info);
    }

    public static void setCurrentTime(Player player, Integer currentTime) {
        MusicInfo info = getMusicInfo(player);
        info.setCurrentTime(currentTime);
        setMusicInfo(player, info);
    }

    public static void setMaxTime(Player player, Integer maxTime) {
        MusicInfo info = getMusicInfo(player);
        info.setMaxTime(maxTime);
        setMusicInfo(player, info);
    }

    public static void resetData(Player player) {
        MusicInfo info = getMusicInfo(player);
        info.setName(null);
        info.setSinger(null);
        info.setLyric(null);
        info.setCurrentTime(null);
        info.setMaxTime(null);
        setMusicInfo(player, info);
    }

    private static MusicInfo getMusicInfo(Player player) {
        MusicInfo info = musicInfo.get(player);
        if (info == null) {
            info = new MusicInfo();
            setMusicInfo(player, info);
        }
        return info;
    }

    private static void setMusicInfo(Player player, MusicInfo info) {
        musicInfo.put(player, info);
    }
}
