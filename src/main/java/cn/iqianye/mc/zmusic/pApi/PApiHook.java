package cn.iqianye.mc.zmusic.pApi;

import cn.iqianye.mc.zmusic.Main;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * PlaceholderAPI 扩展类
 */
public class PApiHook extends PlaceholderExpansion {
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
        return "1.0.0";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        // 音乐名称
        if (identifier.equalsIgnoreCase("playing_name")) {
            String musicName = PlayerStatus.getPlayerMusicName(player);
            if (musicName != null) {
                return musicName;
            } else {
                return "无";
            }

        }
        // 音乐平台
        if (identifier.equalsIgnoreCase("playing_platform")) {
            String musicName = PlayerStatus.getPlayerPlatform(player);
            if (musicName != null) {
                return musicName;
            } else {
                return "无";
            }
        }
        // 音乐来源
        if (identifier.equalsIgnoreCase("playing_source")) {
            String musicName = PlayerStatus.getPlayerPlaySource(player);
            if (musicName != null) {
                return musicName;
            } else {
                return "无";
            }
        }
        // 歌词
        if (identifier.equalsIgnoreCase("playing_lyric")) {
            String lyric = PlayerStatus.getPlayerLyric(player);
            if (lyric != null) {
                return lyric;
            } else {
                return "无";
            }
        }
        // 当前播放时间
        if (identifier.equalsIgnoreCase("time_current")) {
            Long currentTime = PlayerStatus.getPlayerCurrentTime(player);
            return formatTime(currentTime);
        }
        // 最大播放时间
        if (identifier.equalsIgnoreCase("time_max")) {
            Long maxTime = PlayerStatus.getPlayerMaxTime(player);
            return formatTime(maxTime);
        }
        // 版本号
        if (identifier.equalsIgnoreCase("version")) {
            String ver = JavaPlugin.getPlugin(Main.class).getDescription().getVersion();
            if (ver.contains("dev")) {
                return "§c" + ver;
            } else return ver;
        }
        return null;
    }

    private String formatTime(Long time) {
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
}
