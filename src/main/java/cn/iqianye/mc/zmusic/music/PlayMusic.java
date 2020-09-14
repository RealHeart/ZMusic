package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.Main;
import cn.iqianye.mc.zmusic.api.AdvancementAPI;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.music.searchSource.*;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.MessageUtils;
import cn.iqianye.mc.zmusic.utils.MusicUtils;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

    public static void init() {

    }


    /**
     * 播放音乐
     *
     * @param searchKey 搜索词
     * @param source    搜索源 [qq(QQ音乐)163|netease(网易云音乐)kugou(酷狗音乐))
     * @param player    玩家
     * @param type      类型 [all(全体),self(个人)music(点歌)
     * @param players   玩家列表 [类型为all传入，非all可传入null]
     */
    public static void play(String searchKey, String source, Player player, String type, List<Player> players) {
        try {
            MessageUtils.sendNormalMessage("正在搜索中...", player);
            switch (source) {
                case "163":
                case "netease":
                    json = NeteaseCloudMusic.getMusicUrl(searchKey);
                    searchSourceName = "网易云音乐";
                    break;
                case "qq":
                    json = QQMusic.getMusicUrl(searchKey);
                    searchSourceName = "QQ音乐";
                    break;
                case "kugou":
                    json = KuGouMusic.getMusicUrl(searchKey);
                    searchSourceName = "酷狗音乐";
                    break;
                case "kuwo":
                    json = KuwoMusic.getMusicUrl(searchKey);
                    searchSourceName = "酷我音乐";
                    break;
                case "bilibili":
                    if (Val.bilibiliIsVIP) {
                        MessageUtils.sendNormalMessage("哔哩哔哩音乐需要在插件服务器将M4A转换为MP3。", player);
                        MessageUtils.sendNormalMessage("第一次搜索将会耗时很久，如有其他用户使用过，将会返回缓存文件。", player);
                        MessageUtils.sendNormalMessage("请耐心等待。。。。", player);
                        json = BiliBiliMusic.getMusic(searchKey);
                        searchSourceName = "哔哩哔哩音乐";
                        break;
                    } else {
                        MessageUtils.sendErrorMessage("错误,本服务器未授权.", player);
                        return;
                    }
                default:
                    MessageUtils.sendErrorMessage("错误：未知的搜索源", player);
                    return;
            }
            if (json != null) {
                if (source.equalsIgnoreCase("163") || source.equalsIgnoreCase("netease")) {
                    musicID = json.get("id").getAsString();
                }
                musicName = json.get("name").getAsString();
                musicSinger = json.get("singer").getAsString();
                musicFullName = musicName + "(" + musicSinger + ")";
                musicUrl = json.get("url").getAsString();
                musicLyric = OtherUtils.formatLyric(json.get("lyric").getAsString(), json.get("lyricTr").getAsString());
                musicMaxTime = json.get("time").getAsInt();
                errMsg = json.get("error").getAsString().split("\n");
            } else {
                MessageUtils.sendErrorMessage("搜索§r[§e" + searchKey + "§r]§c失败，可能为以下问题.", player);
                MessageUtils.sendErrorMessage("1.搜索的音乐不存在或已下架", player);
                MessageUtils.sendErrorMessage("2.搜索的音乐为付费音乐", player);
                MessageUtils.sendErrorMessage("3.搜索的音乐为试听音乐", player);
                MessageUtils.sendErrorMessage("4.服务器网络异常", player);
                return;
            }
            switch (type) {
                case "all":
                    if (players != null) {
                        MusicUtils.stopAll(players);
                        MusicUtils.playAll(musicUrl, players);
                        OtherUtils.resetPlayerStatusAll(players);
                        for (Player p : players) {
                            PlayListPlayer plp = PlayerStatus.getPlayerPlayListPlayer(p);
                            if (plp != null) {
                                plp.isStop = true;
                                PlayerStatus.setPlayerPlayListPlayer(p, null);
                            }
                            PlayerStatus.setPlayerPlayStatus(p, true);
                            PlayerStatus.setPlayerMusicName(p, musicName);
                            PlayerStatus.setPlayerMusicSinger(p, musicSinger);
                            PlayerStatus.setPlayerPlatform(p, searchSourceName);
                            PlayerStatus.setPlayerPlaySource(p, "搜索");
                            PlayerStatus.setPlayerMaxTime(p, musicMaxTime);
                            PlayerStatus.setPlayerCurrentTime(p, 0L);
                            LyricSender lyricSender = PlayerStatus.getPlayerLyricSender(player);
                            if (lyricSender != null) {
                                lyricSender = new LyricSender();
                                PlayerStatus.setPlayerLyricSender(player, lyricSender);
                            } else {
                                lyricSender = new LyricSender();
                                PlayerStatus.setPlayerLyricSender(player, lyricSender);
                            }
                            lyricSender.player = p;
                            lyricSender.lyric = musicLyric;
                            lyricSender.maxTime = musicMaxTime;
                            lyricSender.name = musicName;
                            lyricSender.singer = musicSinger;
                            lyricSender.fullName = musicFullName;
                            lyricSender.url = musicUrl;
                            lyricSender.runTaskAsynchronously(JavaPlugin.getPlugin(Main.class));
                            for (String msg : errMsg) {
                                if (!msg.isEmpty()) {
                                    MessageUtils.sendErrorMessage(msg, p);
                                }
                            }
                            MessageUtils.sendNormalMessage("在" + searchSourceName + "播放§r[§e" + musicFullName + "§r]§a成功!", p);
                        }
                    }
                    break;
                case "self":
                    if (player != null) {
                        PlayListPlayer plp = PlayerStatus.getPlayerPlayListPlayer(player);
                        if (plp != null) {
                            plp.isStop = true;
                            PlayerStatus.setPlayerPlayListPlayer(player, null);
                            OtherUtils.resetPlayerStatusSelf(player);
                        }
                        MusicUtils.stopSelf(player);
                        MusicUtils.playSelf(musicUrl, player);
                        OtherUtils.resetPlayerStatusSelf(player);
                        PlayerStatus.setPlayerPlayStatus(player, true);
                        PlayerStatus.setPlayerMusicName(player, musicName);
                        PlayerStatus.setPlayerMusicSinger(player, musicSinger);
                        PlayerStatus.setPlayerPlatform(player, searchSourceName);
                        PlayerStatus.setPlayerPlaySource(player, "搜索");
                        PlayerStatus.setPlayerMaxTime(player, musicMaxTime);
                        PlayerStatus.setPlayerCurrentTime(player, 0L);
                        LyricSender lyricSender = PlayerStatus.getPlayerLyricSender(player);
                        if (lyricSender != null) {
                            lyricSender = new LyricSender();
                            PlayerStatus.setPlayerLyricSender(player, lyricSender);
                        } else {
                            lyricSender = new LyricSender();
                            PlayerStatus.setPlayerLyricSender(player, lyricSender);
                        }
                        lyricSender.player = player;
                        lyricSender.lyric = musicLyric;
                        lyricSender.maxTime = musicMaxTime;
                        lyricSender.name = musicName;
                        lyricSender.singer = musicSinger;
                        lyricSender.fullName = musicFullName;
                        lyricSender.url = musicUrl;
                        lyricSender.runTaskAsynchronously(JavaPlugin.getPlugin(Main.class));
                        JavaPlugin plugin = JavaPlugin.getPlugin(Main.class);
                        for (String msg : errMsg) {
                            if (!msg.isEmpty()) {
                                MessageUtils.sendErrorMessage(msg, player);
                            }
                        }
                        MessageUtils.sendNormalMessage("在" + searchSourceName + "播放§r[§e" + musicFullName + "§r]§a成功!", player);
                        if (Config.realSupportAdvancement) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi toast " + player.getName() + " -t:task &a正在播放\n&e" + musicFullName);
                        } else {
                            new AdvancementAPI(new NamespacedKey(plugin, String.valueOf(System.currentTimeMillis())), "§a正在播放\n§e" + musicFullName, plugin).sendAdvancement((player));
                        }
                    }
                    break;
                case "music":
                    TextComponent message = new TextComponent(Config.prefix + "§a玩家§d" + player.getName() + "§a在" + searchSourceName + "点了一首§r[");
                    TextComponent music = new TextComponent(musicName);
                    music.setColor(ChatColor.YELLOW);
                    if (source.equalsIgnoreCase("163") || source.equalsIgnoreCase("netease")) {
                        music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm play " + source + " " + musicID));
                    } else {
                        music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm play " + source + " " + musicName));
                    }

                    music.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放").create()));
                    message.addExtra(music);
                    message.addExtra("§r]§a点击歌名播放!");
                    for (Player p : players) {
                        p.spigot().sendMessage(message);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.sendErrorMessage("搜索§r[§e" + searchKey + "§r]§c失败，可能为以下问题.", player);
            MessageUtils.sendErrorMessage("1.搜索的音乐不存在或已下架", player);
            MessageUtils.sendErrorMessage("2.搜索的音乐为付费音乐", player);
            MessageUtils.sendErrorMessage("3.搜索的音乐为试听音乐", player);
            MessageUtils.sendErrorMessage("4.服务器网络异常", player);
        }
    }
}
