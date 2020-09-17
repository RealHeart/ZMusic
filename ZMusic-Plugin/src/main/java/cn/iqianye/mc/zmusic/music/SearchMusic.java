package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.music.searchSource.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
            case "qq":
                json = QQMusic.getMusicList(searchKey);
                searchSourceName = "QQ音乐";
                break;
            case "kugou":
                json = KuGouMusic.getMusicList(searchKey);
                searchSourceName = "酷狗音乐";
                break;
            case "kuwo":
                json = KuwoMusic.getMusicList(searchKey);
                searchSourceName = "酷我音乐";
                break;
            case "bilibili":
                json = BiliBiliMusic.getMusicList(searchKey);
                searchSourceName = "哔哩哔哩音乐";
                break;
            default:
                ZMusic.message.sendErrorMessage("错误：未知的搜索源", player);
                return;
        }
        if (json != null) {
            ZMusic.message.sendNormalMessage("§6=========================================", player);
            ZMusic.message.sendNormalMessage("在" + searchSourceName + "搜索到以下结果", player);
            int i = 1;
            for (JsonElement j : json) {

                musicName = j.getAsJsonObject().get("name").getAsString();
                musicSinger = j.getAsJsonObject().get("singer").getAsString();
                musicFullName = musicName + " - " + musicSinger;
                TextComponent message = new TextComponent(Config.prefix + "§a" + i + "." + musicFullName);
                i++;
                TextComponent play = new TextComponent("§r[§e播放§r]§r");
                TextComponent music = new TextComponent("§r[§e点歌§r]§r");
                if (source.equalsIgnoreCase("163") ||
                        source.equalsIgnoreCase("netease") ||
                        source.equalsIgnoreCase("qq") ||
                        source.equalsIgnoreCase("bilibili")) {
                    musicID = j.getAsJsonObject().get("id").getAsString();
                    play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm play " + source + " -id:" + musicID));
                    music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm music " + source + " -id:" + musicID));
                } else {
                    play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm play " + source + " " + musicName));
                    music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm music " + source + " " + musicName));
                }
                play.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放").create()));
                music.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击点歌").create()));
                message.addExtra(" ");
                message.addExtra(play);
                message.addExtra(" ");
                message.addExtra(music);
                ZMusic.message.sendJsonMessage(message, player);
            }
            ZMusic.message.sendNormalMessage("§6=========================================", player);
        } else {
            ZMusic.message.sendErrorMessage("搜索§r[§e" + searchKey + "§r]§c失败，可能为以下问题.", player);
            ZMusic.message.sendErrorMessage("1.搜索的音乐不存在或已下架", player);
            ZMusic.message.sendErrorMessage("2.搜索的音乐为付费音乐", player);
            ZMusic.message.sendErrorMessage("3.搜索的音乐为试听音乐", player);
            ZMusic.message.sendErrorMessage("4.服务器网络异常", player);
        }
    }
}
