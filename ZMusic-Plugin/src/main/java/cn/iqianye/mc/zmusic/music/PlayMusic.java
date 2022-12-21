package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.data.PlayerData;
import cn.iqianye.mc.zmusic.language.Lang;
import cn.iqianye.mc.zmusic.music.searchSource.BiliBiliMusic;
import cn.iqianye.mc.zmusic.music.searchSource.KuwoMusic;
import cn.iqianye.mc.zmusic.music.searchSource.NeteaseCloudMusic;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class PlayMusic {

    static String[] errMsg;
    static String musicID;
    static String musicName;
    static String musicSinger;
    static String musicFullName;
    static String musicUrl;
    static JsonObject musicLyric;
    static long musicMaxTime;
    static String searchSourceName;
    static JsonObject json;


    /**
     * 播放音乐
     *
     * @param searchKey 搜索词
     * @param source    搜索源 [qq(QQ音乐)163|netease(网易云音乐)kugou(酷狗音乐))
     * @param player    玩家
     * @param type      类型 [all(全体),self(个人)music(点歌)
     * @param players   玩家列表 [类型为all传入，非all可传入null]
     */
    public static void play(String searchKey, String source, Object player, String type, List<Object> players) {
        try {
            long time = System.currentTimeMillis();
            ZMusic.message.sendNormalMessage(Lang.searching, player);
            switch (source) {
                case "163":
                case "netease":
                    json = NeteaseCloudMusic.getMusicUrl(searchKey);
                    searchSourceName = "网易云音乐";
                    break;
                case "kuwo":
                    json = KuwoMusic.getMusicUrl(searchKey);
                    searchSourceName = "酷我音乐";
                    break;
                case "bilibili":
                    if (ZMusic.bilibiliIsVIP) {
                        ZMusic.message.sendNormalMessage("哔哩哔哩音乐需要在插件服务器将M4A转换为MP3。", player);
                        ZMusic.message.sendNormalMessage("第一次搜索将会耗时很久，如有其他用户使用过，将会返回缓存文件。", player);
                        ZMusic.message.sendNormalMessage("请耐心等待。。。。", player);
                        json = BiliBiliMusic.getMusic(searchKey);
                        searchSourceName = "哔哩哔哩音乐";
                        break;
                    } else {
                        ZMusic.message.sendErrorMessage("错误,本服务器未授权.", player);
                        return;
                    }
                case "qq":
                    ZMusic.message.sendErrorMessage("由于不可抗力因素。", player);
                    ZMusic.message.sendErrorMessage("QQ音乐搜索源已于2.5.0版本移除, API服务已关闭。", player);
                    return;
                default:
                    ZMusic.message.sendErrorMessage("错误：未知的搜索源", player);
                    return;
            }
            boolean supportId = source.equalsIgnoreCase("163") ||
                    source.equalsIgnoreCase("netease") ||
                    source.equalsIgnoreCase("qq") ||
                    source.equalsIgnoreCase("bilibili");
            if (json != null) {
                if (supportId) {
                    musicID = json.get("id").getAsString();
                }
                musicName = json.get("name").getAsString();
                musicSinger = json.get("singer").getAsString();
                musicFullName = musicName + " - " + musicSinger;
                musicUrl = json.get("url").getAsString();
                musicLyric = OtherUtils.formatLyric(json.get("lyric").getAsString(), json.get("lyricTr").getAsString());
                musicMaxTime = json.get("time").getAsInt();
                errMsg = json.get("error").getAsString().split("\n");
            } else {
                ZMusic.message.sendPlayError(player, searchKey);
                return;
            }
            switch (type) {
                case "all":
                    play(null, players, Lang.playAllSource
                            .replaceAll("%player%", ZMusic.player.getName(player)), time);
                    break;
                case "self":
                    play(player, new ArrayList<>(), "搜索", time);
                    break;
                case "music":
                    String s = Lang.musicMessage;
                    String prefix = "§a" + s.split("%fullName%")[0];
                    TextComponent message = new TextComponent(Config.prefix + prefix
                            .replaceAll("%player%", ZMusic.player.getName(player))
                            .replaceAll("%source%", searchSourceName));
                    TextComponent music = new TextComponent("§r[§e" + musicFullName + "§r]");
                    music.setColor(ChatColor.YELLOW);
                    if (supportId) {
                        music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm play " + source + " -id:" + musicID));
                    } else {
                        music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm play " + source + " " + musicName));
                    }
                    music.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b" + Lang.clickPlayText).create()));
                    message.addExtra(music);
                    String suffix = s.split("%fullName%")[1];
                    message.addExtra(suffix);
                    for (Object p : players) {
                        ZMusic.message.sendJsonMessage(message, p);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZMusic.message.sendPlayError(player, searchKey);
        }
    }

    private static void play(Object player, List<Object> players, String src, long time) {
        if (player != null) {
            players.add(player);
        }
        for (Object p : players) {
            OtherUtils.resetPlayerStatus(p);
            PlayListPlayer plp = PlayerData.getPlayerPlayListPlayer(p);
            if (plp != null) {
                plp.isStop = true;
                PlayerData.setPlayerPlayListPlayer(p, null);
            }
            LyricSender lyricSender = PlayerData.getPlayerLyricSender(p);
            if (lyricSender != null) {
                lyricSender.stopThis();
            }
            lyricSender = new LyricSender();
            PlayerData.setPlayerLyricSender(p, lyricSender);
            lyricSender.player = p;
            lyricSender.lyric = musicLyric;
            lyricSender.maxTime = musicMaxTime;
            lyricSender.name = musicName;
            lyricSender.singer = musicSinger;
            lyricSender.fullName = musicFullName;
            lyricSender.platform = searchSourceName;
            lyricSender.src = src;
            lyricSender.url = musicUrl;
            lyricSender.init();
            ZMusic.runTask.runAsync(lyricSender);
            for (String msg : errMsg) {
                if (!msg.isEmpty()) {
                    ZMusic.message.sendErrorMessage(msg, p);
                }
            }
            ZMusic.music.play(musicUrl, p);
            time = System.currentTimeMillis() - time;
            ZMusic.message.sendNormalMessage(Lang.playSuccess
                    .replaceAll("%source%", searchSourceName)
                    .replaceAll("%fullName%", musicFullName)
                    .replaceAll("%time%", String.valueOf(time)), p);
            String title = "§a" + Lang.playing + "\n§e" + musicFullName;
            OtherUtils.sendAdv(p, title);
        }
    }

}
