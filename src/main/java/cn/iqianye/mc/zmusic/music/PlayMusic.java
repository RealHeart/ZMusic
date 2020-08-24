package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.music.searchSource.*;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.MessageUtils;
import cn.iqianye.mc.zmusic.utils.MusicUtils;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Timer;

public class PlayMusic {

    static String musicID;
    static String musicName;
    static String musicUrl;
    static List<Map<Integer, String>> musicLyric;
    static List<Map<Integer, String>> musicLyricTr;
    static int musicMaxTime;
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
                    MessageUtils.sendNormalMessage("QQ音乐需要在服务端获取音乐长度，可能搜索较慢，请耐心等待。", player);
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
                musicName = json.get("name").getAsString() + "(" + json.get("singer").getAsString() + ")";
                musicUrl = json.get("url").getAsString();
                musicLyric = OtherUtils.formatLyric(json.get("lyric").getAsString());
                musicLyricTr = OtherUtils.formatLyric(json.get("lyricTr").getAsString());
                musicMaxTime = json.get("time").getAsInt();
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
                            PlayerStatus.setPlayerPlayStatus(p, true);
                            PlayerStatus.setPlayerMusicName(p, musicName);
                            PlayerStatus.setPlayerMaxTime(p, musicMaxTime);
                            PlayerStatus.setPlayerCurrentTime(p, 0);
                            LyricSendTimer lyricSendTimer = new LyricSendTimer();
                            lyricSendTimer.player = p;
                            lyricSendTimer.lyric = musicLyric;
                            lyricSendTimer.lyricTr = musicLyricTr;
                            if (musicLyric.isEmpty()) {
                                if (source.equalsIgnoreCase("kuwo")) {
                                    MessageUtils.sendErrorMessage("酷我音乐暂不支持歌词显示", p);
                                } else if (source.equalsIgnoreCase("bilibili")) {
                                    MessageUtils.sendErrorMessage("哔哩哔哩音乐暂不支持歌词显示", p);
                                } else {
                                    MessageUtils.sendErrorMessage("未找到歌词信息", p);
                                }
                            }
                            if (musicLyricTr.isEmpty()) {
                                if (source.equalsIgnoreCase("163") || source.equalsIgnoreCase("netease")) {
                                    MessageUtils.sendErrorMessage("网易云音乐暂不支持显示歌词翻译", p);
                                } else if (source.equalsIgnoreCase("kuwo")) {
                                    MessageUtils.sendErrorMessage("酷我音乐暂不支持显示歌词翻译", p);
                                } else if (source.equalsIgnoreCase("kugou")) {
                                    MessageUtils.sendErrorMessage("酷狗音乐暂不支持显示歌词翻译", p);
                                } else if (source.equalsIgnoreCase("bilibili")) {
                                    MessageUtils.sendErrorMessage("哔哩哔哩音乐暂不支持显示歌词翻译", p);
                                } else {
                                    MessageUtils.sendErrorMessage("未找到歌词翻译", p);
                                }
                            }
                            lyricSendTimer.maxTime = musicMaxTime;
                            lyricSendTimer.name = musicName;
                            lyricSendTimer.url = musicUrl;
                            lyricSendTimer.isActionBar = Config.supportActionBar;
                            lyricSendTimer.isBoosBar = Config.supportBossBar;
                            lyricSendTimer.isTitle = Config.supportTitle;
                            lyricSendTimer.isChat = Config.supportChat;
                            Timer timer = PlayerStatus.getPlayerTimer(p);
                            if (timer != null) {
                                timer.cancel();
                                timer = new Timer();
                                timer.schedule(lyricSendTimer, 1000L, 1000L);
                                PlayerStatus.setPlayerTimer(p, timer);
                            } else {
                                timer = new Timer();
                                timer.schedule(lyricSendTimer, 1000L, 1000L);
                                PlayerStatus.setPlayerTimer(p, timer);
                            }
                            MessageUtils.sendNormalMessage("在" + searchSourceName + "播放§r[§e" + musicName + "§r]§a成功!", p);
                        }
                    }
                    break;
                case "self":
                    if (player != null) {
                        MusicUtils.stopSelf(player);
                        MusicUtils.playSelf(musicUrl, player);
                        OtherUtils.resetPlayerStatus(player);
                        PlayerStatus.setPlayerPlayStatus(player, true);
                        PlayerStatus.setPlayerMusicName(player, musicName);
                        PlayerStatus.setPlayerMaxTime(player, musicMaxTime);
                        PlayerStatus.setPlayerCurrentTime(player, 0);
                        LyricSendTimer lyricSendTimer = new LyricSendTimer();
                        lyricSendTimer.player = player;
                        lyricSendTimer.lyric = musicLyric;
                        lyricSendTimer.lyricTr = musicLyricTr;
                        if (musicLyric.isEmpty()) {
                            if (source.equalsIgnoreCase("kuwo")) {
                                MessageUtils.sendErrorMessage("酷我音乐暂不支持歌词显示", player);
                            } else if (source.equalsIgnoreCase("bilibili")) {
                                MessageUtils.sendErrorMessage("哔哩哔哩音乐暂不支持歌词显示", player);
                            } else {
                                MessageUtils.sendErrorMessage("未找到歌词信息", player);
                            }
                        }
                        if (musicLyricTr.isEmpty()) {
                            if (source.equalsIgnoreCase("163") || source.equalsIgnoreCase("netease")) {
                                MessageUtils.sendErrorMessage("网易云音乐暂不支持显示歌词翻译", player);
                            } else if (source.equalsIgnoreCase("kuwo")) {
                                MessageUtils.sendErrorMessage("酷我音乐暂不支持显示歌词翻译", player);
                            } else if (source.equalsIgnoreCase("kugou")) {
                                MessageUtils.sendErrorMessage("酷狗音乐暂不支持显示歌词翻译", player);
                            } else if (source.equalsIgnoreCase("bilibili")) {
                                MessageUtils.sendErrorMessage("哔哩哔哩音乐暂不支持显示歌词翻译", player);
                            } else {
                                MessageUtils.sendErrorMessage("未找到歌词翻译", player);
                            }
                        }
                        lyricSendTimer.maxTime = musicMaxTime;
                        lyricSendTimer.name = musicName;
                        lyricSendTimer.url = musicUrl;
                        lyricSendTimer.isActionBar = Config.supportActionBar;
                        lyricSendTimer.isBoosBar = Config.supportBossBar;
                        lyricSendTimer.isTitle = Config.supportTitle;
                        lyricSendTimer.isChat = Config.supportChat;
                        Timer timer = PlayerStatus.getPlayerTimer(player);
                        if (timer != null) {
                            timer.cancel();
                            timer = new Timer();
                            timer.schedule(lyricSendTimer, 1000L, 1000L);
                            PlayerStatus.setPlayerTimer(player, timer);
                        } else {
                            timer = new Timer();
                            timer.schedule(lyricSendTimer, 1000L, 1000L);
                            PlayerStatus.setPlayerTimer(player, timer);
                        }
                        MessageUtils.sendNormalMessage("在" + searchSourceName + "播放§r[§e" + musicName + "§r]§a成功!", player);
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
            return;
        }
    }
}
