package me.zhenxin.zmusic.music;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.component.ZClickEvent;
import me.zhenxin.zmusic.component.ZComponent;
import me.zhenxin.zmusic.component.ZHoverEvent;
import me.zhenxin.zmusic.component.ZTextComponent;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.language.Lang;
import me.zhenxin.zmusic.music.searchSource.BiliBiliMusic;
import me.zhenxin.zmusic.music.searchSource.KuwoMusic;
import me.zhenxin.zmusic.music.searchSource.NeteaseCloudMusic;

public class SearchMusic {

    static String musicID;
    static String musicName;
    static String musicSinger;
    static String musicFullName;
    static String searchSourceName;
    static JsonArray json;

    public static void sendList(String searchKey, String source, Object player) {
        ZMusic.message.sendNormalMessage("正在搜索中...", player);
        switch (source) {
            case "163":
            case "netease":
                json = NeteaseCloudMusic.getMusicList(searchKey);
                searchSourceName = "网易云音乐";
                break;
            case "kuwo":
                json = KuwoMusic.getMusicList(searchKey);
                searchSourceName = "酷我音乐";
                break;
            case "bilibili":
                json = BiliBiliMusic.getMusicList(searchKey);
                searchSourceName = "哔哩哔哩视频";
                break;
            default:
                ZMusic.message.sendErrorMessage("错误：未知的搜索源", player);
                return;
        }
        if (json != null) {
            ZMusic.message.sendNormalMessage("§6=========================================", player);
            int i = 1;
            for (JsonElement j : json) {

                musicName = j.getAsJsonObject().get("name").getAsString();
                musicSinger = j.getAsJsonObject().get("singer").getAsString();
                musicFullName = musicName + " - " + musicSinger;
                ZComponent message = ZTextComponent.of(Config.prefix + "§a" + i + "." + musicFullName);
                i++;
                ZComponent play = ZTextComponent.of("§r[§e" + Lang.clickPlay + "§r]§r");
                ZComponent music = ZTextComponent.of("§r[§e" + Lang.clickMusic + "§r]§r");
                if (source.equalsIgnoreCase("163") ||
                    source.equalsIgnoreCase("netease") ||
                    source.equalsIgnoreCase("bilibili")) {
                    musicID = j.getAsJsonObject().get("id").getAsString();
                    play.setClickEvent(ZClickEvent.runCommand("/zm play " + source + " -id:" + musicID));
                    music.setClickEvent(ZClickEvent.runCommand("/zm music " + source + " -id:" + musicID));
                } else {
                    play.setClickEvent(ZClickEvent.runCommand("/zm play " + source + " " + musicName));
                    music.setClickEvent(ZClickEvent.runCommand("/zm music " + source + " " + musicName));
                }
                play.setHoverEvent(ZHoverEvent.showText("§b" + Lang.clickPlayText));
                music.setHoverEvent(ZHoverEvent.showText("§b" + Lang.clickMusicText));
                message.addChild(ZTextComponent.of(" "));
                message.addChild(play);
                message.addChild(ZTextComponent.of(" "));
                message.addChild(music);
                ZMusic.message.sendJsonMessage(message, player);
            }
            ZMusic.message.sendNormalMessage("§6=========================================", player);
        } else {
            ZMusic.message.sendPlayError(player, musicName);
        }
    }
}
