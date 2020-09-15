package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.ZMusicBukkit;
import cn.iqianye.mc.zmusic.api.AdvancementAPI;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class PlayListPlayer extends BukkitRunnable {

    public List<JsonObject> playList;
    public String type = "normal";
    public String id;
    public boolean singleIsPlayEd = true;
    public boolean nextMusic = false;
    public boolean prevMusic = false;
    public boolean jumpMusic = false;
    public int jumpSong = 0;
    public String platform;
    public String playListName;
    public Player player;
    String searchSourceName = "";

    int songs = 0;
    int maxSongs;

    public void init() {
        searchSourceName = "";
        switch (platform) {
            case "netease":
                searchSourceName = "网易云音乐";
                break;
            case "qq":
                searchSourceName = "QQ音乐";
                break;
        }
        maxSongs = playList.size();
        maxSongs = maxSongs - 1;

    }

    @Override
    public void run() {
        if (player.isOnline()) {
            if (nextMusic) {
                if (songs != maxSongs) {
                    singleIsPlayEd = true;
                    nextMusic = false;
                } else {
                    MessageUtils.sendErrorMessage("切换失败: 已经是最后一首歌曲了", player);
                    nextMusic = false;
                }
            }
            if (prevMusic) {
                if (songs > 1) {
                    songs--;
                    songs--;
                    singleIsPlayEd = true;
                    prevMusic = false;
                } else {
                    MessageUtils.sendErrorMessage("切换失败: 已经是第一首歌曲了", player);
                    prevMusic = false;
                }
            }
            if (jumpMusic) {
                if (jumpSong < maxSongs) {
                    songs = jumpSong - 1;
                    singleIsPlayEd = true;
                    jumpMusic = false;
                } else {
                    MessageUtils.sendErrorMessage("跳转失败: 指定的歌曲不存在", player);
                    jumpMusic = false;
                }
            }
            if (singleIsPlayEd) {
                long successTime = System.currentTimeMillis();
                if (!player.isOnline()) {
                    LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")检测到玩家[" + player.getName() + "] 离线,停止线程。");
                    cancel();
                }
                switch (type) {
                    case "normal":
                        if (songs > maxSongs) {
                            cancel();
                        }
                        LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")模式为[" + type + "],当前音乐ID: " + songs);
                        break;
                    case "loop":
                        if (songs > maxSongs) {
                            songs = 0;
                        }
                        LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")模式为[" + type + "],当前音乐ID: " + songs);
                        break;
                    case "random":
                        Random random = new Random();
                        songs = random.nextInt(maxSongs);
                        LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")模式为[" + type + "],当前音乐ID: " + songs);
                        break;
                    case "stop":
                        cancel();
                        break;
                }
                String name = playList.get(songs).get("name").getAsString();
                String singer = playList.get(songs).get("singer").getAsString();
                String fullName = name + " - " + singer;
                String nextfullName;
                switch (type) {
                    case "loop":
                        if ((songs + 1) > maxSongs) {
                            String nextName = playList.get(0).get("name").getAsString();
                            String nextSinger = playList.get(0).get("singer").getAsString();
                            nextfullName = nextName + " - " + nextSinger;
                        } else {
                            String nextName = playList.get(songs + 1).get("name").getAsString();
                            String nextSinger = playList.get(songs + 1).get("singer").getAsString();
                            nextfullName = nextName + " - " + nextSinger;
                        }
                        break;
                    case "random":
                        nextfullName = "随机";
                        break;
                    default:
                        if ((songs + 1) < maxSongs) {
                            String nextName = playList.get(songs + 1).get("name").getAsString();
                            String nextSinger = playList.get(songs + 1).get("singer").getAsString();
                            nextfullName = nextName + " - " + nextSinger;
                        } else {
                            nextfullName = "已是最后一首";
                        }
                        break;
                }
                long time = playList.get(songs).get("time").getAsInt();
                String url = "";
                JsonObject lyric = null;
                Gson gson = new GsonBuilder().create();
                if (platform.equalsIgnoreCase("qq")) {
                    String id = playList.get(songs).get("id").getAsString();
                    String mid = playList.get(songs).get("mid").getAsString();
                    String getMp3Url = Config.qqMusicApiRoot + "song/url?id=" + id + "&mediaId=" + mid;
                    String getMp3JsonText = NetUtils.getNetString(getMp3Url, null);
                    JsonObject getMp3Json = gson.fromJson(getMp3JsonText, JsonObject.class);
                    try {
                        url = getMp3Json.get("data").getAsString();
                    } catch (Exception e) {
                        MessageUtils.sendErrorMessage("播放[§e" + fullName + "§c]失败.", player);
                        MessageUtils.sendErrorMessage("无法获取播放链接, 可能无版权或为VIP音乐.", player);
                        songs++;
                        return;
                    }
                    String getLyricUrl = Config.qqMusicApiRoot + "lyric?songmid=" + id;
                    String lyricJsonText = NetUtils.getNetString(getLyricUrl, null);
                    JsonObject lyricJson = gson.fromJson(lyricJsonText, JsonObject.class);
                    String lyricText = lyricJson.get("data").getAsJsonObject().get("lyric").getAsString();
                    lyricText = lyricText.replaceAll("&apos;", "'");
                    lyricText = lyricText.replaceAll("\r", "");
                    String lyricTrText = lyricJson.get("data").getAsJsonObject().get("trans").getAsString();
                    lyricTrText = lyricTrText.replaceAll("&apos;", "'");
                    lyricTrText = lyricTrText.replaceAll("\r", "");
                    if (lyricText.isEmpty()) {
                        MessageUtils.sendErrorMessage("未找到歌词信息", player);
                    }
                    if (lyricTrText.isEmpty()) {
                        MessageUtils.sendErrorMessage("未找到歌词翻译", player);
                    }
                    lyric = OtherUtils.formatLyric(lyricText, lyricTrText);
                } else if (platform.equalsIgnoreCase("netease")) {
                    String id = playList.get(songs).get("id").getAsString();
                    String getMp3Url = Config.neteaseApiRoot + "song/url?id=" + id + "&br=320000";
                    String getMp3JsonText = NetUtils.getNetString(getMp3Url, null);
                    JsonObject getMp3Json = gson.fromJson(getMp3JsonText, JsonObject.class);
                    try {
                        url = getMp3Json.get("data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
                    } catch (Exception e) {
                        MessageUtils.sendErrorMessage("播放[§e" + fullName + "§c]失败.", player);
                        MessageUtils.sendErrorMessage("无法获取播放链接, 可能无版权或为VIP音乐.", player);
                        songs++;
                        return;
                    }
                    String lyricJsonText = NetUtils.getNetString(Config.neteaseApiRoot + "lyric?id=" + id, null);
                    JsonObject lyricJson = gson.fromJson(lyricJsonText, JsonObject.class);
                    String lyricText = "";
                    String lyricTrText = "";
                    try {
                        lyricText = lyricJson.get("lrc").getAsJsonObject().get("lyric").getAsString();
                        lyricText = lyricText.replaceAll("\r", "");
                        lyricTrText = lyricJson.get("tlyric").getAsJsonObject().get("lyric").getAsString();
                        lyricTrText = lyricTrText.replaceAll("\r", "");
                    } catch (Exception ignored) {
                    }
                    lyric = OtherUtils.formatLyric(lyricText, lyricTrText);
                    if (lyricText.isEmpty()) {
                        MessageUtils.sendErrorMessage("未找到歌词信息", player);
                    }
                    if (lyricTrText.isEmpty()) {
                        MessageUtils.sendErrorMessage("未找到歌词翻译", player);
                    }
                }
                OtherUtils.resetPlayerStatus(player);
                LyricSender lyricSender = PlayerStatus.getPlayerLyricSender(player);
                if (lyricSender != null) {
                    lyricSender.stop();
                }
                lyricSender = new LyricSender();
                PlayerStatus.setPlayerLyricSender(player, lyricSender);
                lyricSender.player = player;
                lyricSender.lyric = lyric;
                lyricSender.maxTime = time;
                lyricSender.name = name;
                lyricSender.singer = singer;
                lyricSender.fullName = fullName;
                lyricSender.platform = searchSourceName;
                lyricSender.platform = "歌单<" + playListName + ">";
                lyricSender.url = url;
                lyricSender.isPlayList = true;
                lyricSender.nextMusicName = nextfullName;
                lyricSender.playListPlayer = this;
                lyricSender.init();
                lyricSender.runTaskAsynchronously(ZMusicBukkit.plugin);
                MusicUtils.play(url, player);
                singleIsPlayEd = false;
                successTime = System.currentTimeMillis() - successTime;
                LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")为[" + player.getName() + "]播放歌单<" + playListName + ">中的" + fullName);
                TextComponent message = new TextComponent(Config.prefix + "§a播放§r[§e" + fullName + "§r]§a成功,耗时" + successTime + "毫秒!");
                TextComponent prev = new TextComponent("§r[§e上一首§r]§r");
                prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist prev"));
                prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击切换到上一首").create()));
                TextComponent next = new TextComponent("§r[§e下一首§r]§r");
                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist next"));
                next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击切换到下一首").create()));
                message.addExtra(" ");
                message.addExtra(prev);
                message.addExtra(" ");
                message.addExtra(next);
                player.spigot().sendMessage(message);
                if (Config.realSupportAdvancement) {
                    JavaPlugin plugin = ZMusicBukkit.plugin;
                    Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> new AdvancementAPI(new NamespacedKey(plugin, String.valueOf(System.currentTimeMillis())), "§a正在播放\n§e" + fullName, plugin).sendAdvancement((player)), 1L);
                }
                songs++;
            }
        } else {
            LogUtils.sendDebugMessage("[歌单]玩家离线 歌单播放器(ID:" + getTaskId() + ")[" + player.getName() + "]<" + playListName + ">线程已停止。");
            cancel();
        }
    }
}
