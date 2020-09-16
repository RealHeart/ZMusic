package cn.iqianye.mc.zmusic.papi;

import cn.iqianye.mc.zmusic.ZMusicBukkit;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

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
        // 音乐歌手
        if (identifier.equalsIgnoreCase("playing_singer")) {
            String musicName = PlayerStatus.getPlayerMusicSinger(player);
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
            return OtherUtils.formatTime(currentTime);
        }
        // 最大播放时间
        if (identifier.equalsIgnoreCase("time_max")) {
            Long maxTime = PlayerStatus.getPlayerMaxTime(player);
            return OtherUtils.formatTime(maxTime);
        }
        // 版本号
        if (identifier.equalsIgnoreCase("version")) {
            String ver = ZMusicBukkit.plugin.getDescription().getVersion();
            if (ver.contains("dev")) {
                return "§c" + ver;
            } else return ver;
        }
        return null;
    }


}
