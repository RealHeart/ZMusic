package me.zhenxin.zmusic.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.zhenxin.zmusic.data.PlayerData;
import org.bukkit.entity.Player;


@SuppressWarnings({"unused", "AlibabaClassMustHaveAuthor", "AlibabaClassNamingShouldBeCamel", "NullableProblems", "AlibabaUndefineMagicConstant"})
public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "zmusic";
    }

    @Override
    public String getAuthor() {
        return "ZhenXin";
    }

    @Override
    public String getVersion() {
        return "3.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        switch (params) {
            case "status_name":
                String name = PlayerData.getName(player);
                if (name != null) {
                    return name;
                } else {
                    return "None";
                }
            case "status_singer":
                String singer = PlayerData.getSinger(player);
                if (singer != null) {
                    return singer;
                } else {
                    return "None";
                }
            case "status_lyric":
                String lyric = PlayerData.getLyric(player);
                if (lyric != null) {
                    return lyric;
                } else {
                    return "None";
                }
            case "status_current_time":
                Integer currentTime = PlayerData.getCurrentTime(player);
                return formatTime(currentTime);
            case "status_max_time":
                Integer maxTime = PlayerData.getMaxTime(player);
                return formatTime(maxTime);
            default:
                return null;
        }
    }

    private String formatTime(Integer time) {
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
            return "00:00";
        }
    }
}