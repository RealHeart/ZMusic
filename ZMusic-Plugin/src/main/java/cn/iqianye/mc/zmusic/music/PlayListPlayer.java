package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.data.PlayerData;
import cn.iqianye.mc.zmusic.language.Lang;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.Random;

public class PlayListPlayer extends Thread {

    public List<JsonObject> playList;
    public String type = "normal";
    public String id;
    public boolean isStop = false;
    public boolean singleIsPlayEd = true;
    public boolean nextMusic = false;
    public boolean prevMusic = false;
    public boolean jumpMusic = false;
    public int jumpSong = 0;
    public String platform;
    public String playListName;
    public Object player;
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
        while (!isStop) {
            if (ZMusic.player.isOnline(player)) {
                if (nextMusic) {
                    if (songs != maxSongs) {
                        singleIsPlayEd = true;
                        nextMusic = false;
                    } else {
                        ZMusic.message.sendErrorMessage("切换失败: 已经是最后一首歌曲了", player);
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
                        ZMusic.message.sendErrorMessage("切换失败: 已经是第一首歌曲了", player);
                    }
                }
                if (jumpMusic) {
                    if (jumpSong < maxSongs) {
                        songs = jumpSong - 1;
                        singleIsPlayEd = true;
                        jumpMusic = false;
                    } else {
                        ZMusic.message.sendErrorMessage("跳转失败: 指定的歌曲不存在", player);
                        jumpMusic = false;
                    }
                }
                if (singleIsPlayEd) {
                    long successTime = System.currentTimeMillis();
                    if (!ZMusic.player.isOnline(player)) {
                        ZMusic.log.sendDebugMessage("[歌单] 歌单播放器(ID:" + getId() + ")检测到玩家[" + player + "] 离线,停止线程。");
                        break;
                    }
                    switch (type) {
                        case "normal":
                            if (songs > maxSongs) {
                                break;
                            }
                            ZMusic.log.sendDebugMessage("[歌单] 歌单播放器(ID:" + getId() + ")模式为[" + type + "],当前音乐ID: " + songs);
                            break;
                        case "loop":
                            if (songs > maxSongs) {
                                songs = 0;
                            }
                            ZMusic.log.sendDebugMessage("[歌单] 歌单播放器(ID:" + getId() + ")模式为[" + type + "],当前音乐ID: " + songs);
                            break;
                        case "random":
                            Random random = new Random();
                            songs = random.nextInt(maxSongs);
                            ZMusic.log.sendDebugMessage("[歌单] 歌单播放器(ID:" + getId() + ")模式为[" + type + "],当前音乐ID: " + songs);
                            break;
                        case "stop":
                            isStop = true;
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
                            ZMusic.message.sendErrorMessage("播放[§e" + fullName + "§c]失败.", player);
                            ZMusic.message.sendErrorMessage("无法获取播放链接, 可能无版权或为VIP音乐.", player);
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
                        if (lyricText.isEmpty()) {
                            ZMusic.message.sendErrorMessage("未找到歌词信息", player);
                        }
                        if (lyricTrText.isEmpty()) {
                            ZMusic.message.sendErrorMessage("未找到歌词翻译", player);
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
                            ZMusic.message.sendErrorMessage("播放[§e" + fullName + "§c]失败.", player);
                            ZMusic.message.sendErrorMessage("无法获取播放链接, 可能无版权或为VIP音乐.", player);
                            songs++;
                            continue;
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
                    }
                    OtherUtils.resetPlayerStatus(player);
                    LyricSender lyricSender = PlayerData.getPlayerLyricSender(player);
                    if (lyricSender != null) {
                        lyricSender.stopThis();
                    }
                    lyricSender = new LyricSender();
                    PlayerData.setPlayerLyricSender(player, lyricSender);
                    lyricSender.player = player;
                    lyricSender.lyric = lyric;
                    lyricSender.maxTime = time;
                    lyricSender.name = name;
                    lyricSender.singer = singer;
                    lyricSender.fullName = fullName;
                    lyricSender.platform = searchSourceName;
                    lyricSender.src = "歌单<" + playListName + ">";
                    lyricSender.url = url;
                    lyricSender.isPlayList = true;
                    lyricSender.nextMusicName = nextfullName;
                    lyricSender.playListPlayer = this;
                    lyricSender.init();
                    ZMusic.runTask.runAsync(lyricSender);
                    ZMusic.music.play(url, player);
                    singleIsPlayEd = false;
                    successTime = System.currentTimeMillis() - successTime;
                    ZMusic.log.sendDebugMessage("[歌单] 歌单播放器(ID:" + getId() + ")为[" + player + "]播放歌单<" + playListName + ">中的" + fullName);
                    TextComponent message = new TextComponent(Config.prefix + "§a" + Lang.playSuccess
                            .replaceAll("%source%", searchSourceName)
                            .replaceAll("%fullName%", fullName)
                            .replaceAll("%time%", String.valueOf(successTime)));
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
                    ZMusic.message.sendJsonMessage(message, player);
                    String title = "§a" + Lang.playing + "\n§e" + fullName;
                    OtherUtils.sendAdv(player, title);
                    songs++;
                }
            } else {
                ZMusic.log.sendDebugMessage("[歌单]玩家离线 歌单播放器(ID:" + getId() + ")[" + player + "]<" + playListName + ">线程已停止。");
                break;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
