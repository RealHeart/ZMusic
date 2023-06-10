package me.zhenxin.zmusic.music;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.data.PlayerData;
import me.zhenxin.zmusic.language.Lang;
import me.zhenxin.zmusic.utils.NetUtils;
import me.zhenxin.zmusic.utils.OtherUtils;
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
                        prevMusic = false;
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
                        ZMusic.log.sendDebugMessage("[歌单] 歌单播放器(ID:" + getId() + ")检测到玩家[" + ZMusic.player.getName(player) + "] 离线,停止线程。");
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
                    if (platform.equalsIgnoreCase("netease")) {
                        String id = playList.get(songs).get("id").getAsString();
                        String getMp3Url = Config.neteaseApiRoot + "song/url?id=" + id + "&br=320000";
                        String getMp3JsonText = NetUtils.getNetString(getMp3Url, null);
                        JsonObject getMp3Json = gson.fromJson(getMp3JsonText, JsonObject.class);
                        try {
                            url = getMp3Json.get("data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
                        } catch (Exception e) {
                            for (String s : Lang.playListPlayError) {
                                ZMusic.message.sendErrorMessage(s.replaceAll("%musicName%", fullName), player);
                            }
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
                    ZMusic.log.sendDebugMessage("[歌单] 歌单播放器(ID:" + getId() + ")为[" + ZMusic.player.getName(player) + "]播放歌单<" + playListName + ">中的" + fullName);
                    TextComponent message = new TextComponent(Config.prefix + "§a" + Lang.playSuccess
                            .replaceAll("%source%", searchSourceName)
                            .replaceAll("%fullName%", fullName)
                            .replaceAll("%time%", String.valueOf(successTime)));
                    TextComponent prev = new TextComponent("§r[§e" + Lang.clickPrev + "§r]§r");
                    prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist prev"));
                    prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b" + Lang.clickPrevText).create()));
                    TextComponent next = new TextComponent("§r[§e" + Lang.clickNext + "§r]§r");
                    next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist next"));
                    next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b" + Lang.clickNextText).create()));
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
                ZMusic.log.sendDebugMessage("[歌单]玩家离线 歌单播放器(ID:" + getId() + ")[" + ZMusic.player.getName(player) + "]<" + playListName + ">线程已停止。");
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
