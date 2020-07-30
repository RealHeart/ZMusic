package cn.iqianye.MinecraftPlugins.ZMusic.Music;

import cn.iqianye.MinecraftPlugins.ZMusic.Config.Config;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.KuGouMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.KuwoMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.NeteaseCloudMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.MessageUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class SearchMusic {
    static String musicID;
    static String musicName;
    static String searchSourceName;
    static JsonArray json;

    public static void sendList(String searchKey, String source, Player player) {
        MessageUtils.sendNormalMessage("正在搜索中...", player);
        switch (source) {
            case "163":
            case "netease":
                json = NeteaseCloudMusic.getMusicList(searchKey);
                searchSourceName = "网易云音乐";
                break;
            case "kugou":
                json = KuGouMusic.getMusicList(searchKey);
                searchSourceName = "酷狗音乐";
                break;
            case "kuwo":
                json = KuwoMusic.getMusicList(searchKey);
                searchSourceName = "酷我音乐";
                break;
            default:
                MessageUtils.sendErrorMessage("错误：未知的搜索源", player);
                return;
        }
        if (json != null) {
            MessageUtils.sendNormalMessage("§6=========================================", player);
            MessageUtils.sendNormalMessage("在" + searchSourceName + "搜索到以下结果", player);
            int i = 1;
            for (JsonElement j : json) {
                if (source.equalsIgnoreCase("163") || source.equalsIgnoreCase("netease")) {
                    musicID = j.getAsJsonObject().get("id").getAsString();
                }
                musicName = j.getAsJsonObject().get("name").getAsString() + "(" + j.getAsJsonObject().get("singer").getAsString() + ")";
                TextComponent message = new TextComponent(Config.prefix + "§a" + i + "." + musicName);
                i++;
                TextComponent play = new TextComponent("§r[§e播放§r]§r");
                if (source.equalsIgnoreCase("163") || source.equalsIgnoreCase("netease")) {
                    play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm play " + source + " " + musicID));
                } else {
                    play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm play " + source + " " + musicName));
                }
                play.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放").create()));
                TextComponent music = new TextComponent("§r[§e点歌§r]§r");
                if (source.equalsIgnoreCase("163") || source.equalsIgnoreCase("netease")) {
                    music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm music " + source + " " + musicID));
                } else {
                    music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm music " + source + " " + musicName));
                }
                music.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击点歌").create()));
                message.addExtra(" ");
                message.addExtra(play);
                message.addExtra(" ");
                message.addExtra(music);
                player.spigot().sendMessage(message);
            }
            MessageUtils.sendNormalMessage("§6=========================================", player);
        } else {
            MessageUtils.sendErrorMessage("搜索§r[§e" + searchKey + "§r]§c失败，可能为以下问题.", player);
            MessageUtils.sendErrorMessage("1.搜索的音乐不存在或已下架", player);
            MessageUtils.sendErrorMessage("2.搜索的音乐为付费音乐", player);
            MessageUtils.sendErrorMessage("3.搜索的音乐为试听音乐", player);
            MessageUtils.sendErrorMessage("4.服务器网络异常", player);
            return;
        }
    }
}
