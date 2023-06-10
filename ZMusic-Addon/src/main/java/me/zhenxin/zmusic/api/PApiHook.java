package me.zhenxin.zmusic.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.zhenxin.zmusic.data.PlayerData;
import me.zhenxin.zmusic.data.StaticData;
import org.bukkit.entity.Player;

/**
 * PlaceholderAPI 扩展类
 */
public class PApiHook extends PlaceholderExpansion {
    private static String formatTime(Long time) {
        if (time != null) {
            if (time < 60) {
                return "00" + ":" + String.format("%02d", time);
            } else if (time < 3600) {
                long m = time / 60;
                long s = time % 60;
                return String.format("%02d", m) + ":" + String.format("%02d", s);
            } else {
                long h = time / 3600;
                long m = (time % 3600) / 60;
                long s = (time % 3600) % 60;
                return String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s);
            }
        } else {
            return "--:--";
        }
    }

    @Override
    public String getIdentifier() {
        return "zmusic";
    }

    @Override
    public String getAuthor() {
        return "真心";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        // 音乐名称
        if (identifier.equalsIgnoreCase("playing_name")) {
            String musicName = PlayerData.getPlayerMusicName(player);
            if (musicName != null) {
                return musicName;
            } else {
                return "无";
            }

        }
        // 音乐歌手
        if (identifier.equalsIgnoreCase("playing_singer")) {
            String musicName = PlayerData.getPlayerMusicSinger(player);
            if (musicName != null) {
                return musicName;
            } else {
                return "无";
            }

        }
        // 音乐平台
        if (identifier.equalsIgnoreCase("playing_platform")) {
            String musicName = PlayerData.getPlayerPlatform(player);
            if (musicName != null) {
                return musicName;
            } else {
                return "无";
            }
        }
        // 音乐来源
        if (identifier.equalsIgnoreCase("playing_source")) {
            String musicName = PlayerData.getPlayerPlaySource(player);
            if (musicName != null) {
                return musicName;
            } else {
                return "无";
            }
        }
        // 歌词
        if (identifier.equalsIgnoreCase("playing_lyric")) {
            String lyric = PlayerData.getPlayerLyric(player);
            if (lyric != null) {
                return lyric;
            } else {
                return "无";
            }
        }
        // 当前播放时间
        if (identifier.equalsIgnoreCase("time_current")) {
            Long currentTime = PlayerData.getPlayerCurrentTime(player);
            return formatTime(currentTime);
        }
        // 最大播放时间
        if (identifier.equalsIgnoreCase("time_max")) {
            Long maxTime = PlayerData.getPlayerMaxTime(player);
            return formatTime(maxTime);
        }
        // 版本号
        if (identifier.equalsIgnoreCase("version")) {
            String ver = StaticData.version;
            if (ver.contains("dev")) {
                return "§c" + ver;
            } else return ver;
        }
        return null;
    }

}