package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.Main;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class PlayListPlayer extends BukkitRunnable {

    public List<JsonObject> playList;
    public String type = "normal";
    public boolean isStop = false;
    public boolean isPlayEd = false;
    public String platform;
    public String playListName;
    public Player player;

    int songs = 0;


    @Override
    public void run() {
        String searchSourceName = "";
        switch (platform) {
            case "netease":
                searchSourceName = "网易云音乐";
                break;
            case "qq":
                searchSourceName = "QQ音乐";
                break;
        }
        int maxSongs = playList.size();
        maxSongs = maxSongs - 1;
        LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")[" + player.getName() + "]<" + playListName + ">线程已启动。");
        while (!isStop) {
            if (!player.isOnline()) {
                LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")检测到玩家[" + player.getName() + "] 离线,停止线程。");
                break;
            }
            if (type.equalsIgnoreCase("normal")) {
                if (songs > maxSongs) {
                    isPlayEd = true;
                    break;
                }
                LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")模式为[" + type + "],当前音乐ID: " + songs);
            } else if (type.equalsIgnoreCase("loop")) {
                if (songs > maxSongs) {
                    songs = 0;
                }
                LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")模式为[" + type + "],当前音乐ID: " + songs);
            } else if (type.equalsIgnoreCase("random")) {
                Random random = new Random();
                songs = random.nextInt(maxSongs);
                LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")模式为[" + type + "],当前音乐ID: " + songs);
            } else if (type.equalsIgnoreCase("stop")) {
                break;
            }
            String name = playList.get(songs).get("name").getAsString() + "(" + playList.get(songs).get("singer").getAsString() + ")";
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
                    MessageUtils.sendErrorMessage("播放[§e" + name + "§c]失败.", player);
                    MessageUtils.sendErrorMessage("无法获取播放链接, 可能无版权或为VIP音乐.", player);
                    songs++;
                    continue;
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
                lyric = OtherUtils.formatLyric(lyricText, lyricTrText);
            } else if (platform.equalsIgnoreCase("netease")) {
                String id = playList.get(songs).get("id").getAsString();
                String getMp3Url = Config.neteaseApiRoot + "song/url?id=" + id + "&br=320000&" +
                        "cookie=" + Val.neteaseCookie;
                String getMp3JsonText = NetUtils.getNetString(getMp3Url, null);
                JsonObject getMp3Json = gson.fromJson(getMp3JsonText, JsonObject.class);
                try {
                    url = getMp3Json.get("data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
                } catch (Exception e) {
                    MessageUtils.sendErrorMessage("播放[§e" + name + "§c]失败.", player);
                    MessageUtils.sendErrorMessage("无法获取播放链接, 可能无版权或为VIP音乐.", player);
                    songs++;
                    continue;
                }
                String lyricJsonText = NetUtils.getNetString(Config.neteaseApiRoot + "lyric?id=" + id, null);
                JsonObject lyricJson = gson.fromJson(lyricJsonText, JsonObject.class);
                String lyricText = "";
                String lyricTrText = "";
                try {
                    lyricText = lyricJson.get("lrc").getAsJsonObject().get("lyric").getAsString();
                    lyricTrText = lyricJson.get("tlyric").getAsJsonObject().get("lyric").getAsString();
                    lyricText = lyricText.replaceAll("\r", "");
                    lyricTrText = lyricText.replaceAll("\r", "");
                    lyric = OtherUtils.formatLyric(lyricText, lyricTrText);
                } catch (Exception ignored) {
                }
            }
            MusicUtils.playSelf(url, player);
            OtherUtils.resetPlayerStatusSelf(player);
            PlayerStatus.setPlayerPlayStatus(player, true);
            PlayerStatus.setPlayerMusicName(player, name);
            PlayerStatus.setPlayerPlatform(player, searchSourceName);
            PlayerStatus.setPlayerPlaySource(player, "歌单<" + playListName + ">");
            PlayerStatus.setPlayerMaxTime(player, time);
            PlayerStatus.setPlayerCurrentTime(player, 0L);
            LyricSender lyricSender = PlayerStatus.getPlayerLyricSender(player);
            if (lyricSender != null) {
                lyricSender.cancel();
                lyricSender = new LyricSender();
                PlayerStatus.setPlayerLyricSender(player, lyricSender);
            } else {
                lyricSender = new LyricSender();
                PlayerStatus.setPlayerLyricSender(player, lyricSender);
            }
            lyricSender.player = player;
            lyricSender.lyric = lyric;
            if (lyric == null) {
                MessageUtils.sendErrorMessage("未找到歌词信息", player);
            }
            lyricSender.maxTime = time;
            lyricSender.name = name;
            lyricSender.url = url;
            lyricSender.isActionBar = Config.supportActionBar;
            lyricSender.isBoosBar = Config.supportBossBar;
            lyricSender.isTitle = Config.supportTitle;
            lyricSender.isChat = Config.supportChat;
            lyricSender.runTaskAsynchronously(JavaPlugin.getPlugin(Main.class));
            LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")为[" + player.getName() + "]播放歌单<" + playListName + ">中的" + name);
            MessageUtils.sendNormalMessage("播放§r[§e" + name + "§r]§a成功!", player);
            JavaPlugin plugin = JavaPlugin.getPlugin(Main.class);
            if (Config.realSupportAdvancement) {
                // new AdvancementAPI(new NamespacedKey(plugin, String.valueOf(System.currentTimeMillis())), "§a正在播放\n§e" + name, plugin).sendAdvancement((player));
            }
            try {
                Thread.sleep((time * 1000) + 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            songs++;
        }
        LogUtils.sendDebugMessage("[歌单] 歌单播放器(ID:" + getTaskId() + ")[" + player.getName() + "]<" + playListName + ">线程已停止。");
    }
}
