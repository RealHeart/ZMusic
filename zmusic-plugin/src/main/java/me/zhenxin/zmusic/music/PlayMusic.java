package me.zhenxin.zmusic.music;

import com.google.gson.JsonObject;
import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.component.ZClickEvent;
import me.zhenxin.zmusic.component.ZComponent;
import me.zhenxin.zmusic.component.ZHoverEvent;
import me.zhenxin.zmusic.component.ZTextComponent;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.data.PlayerData;
import me.zhenxin.zmusic.language.Lang;
import me.zhenxin.zmusic.music.searchSource.BiliBiliMusic;
import me.zhenxin.zmusic.music.searchSource.KuwoMusic;
import me.zhenxin.zmusic.music.searchSource.NeteaseCloudMusic;
import me.zhenxin.zmusic.utils.OtherUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayMusic {

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
            JsonObject json;
            String searchSourceName;
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
                    if (ZMusic.isVip) {
                        ZMusic.message.sendNormalMessage("哔哩哔哩视频音频需要在插件服务器将M4A转换为MP3。", player);
                        ZMusic.message.sendNormalMessage("第一次搜索将会耗时很久，如有其他用户使用过，将会返回缓存文件。", player);
                        ZMusic.message.sendNormalMessage("请耐心等待。。。。", player);
                        json = BiliBiliMusic.getMusic(searchKey);
                        searchSourceName = "哔哩哔哩视频";
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
            String musicID = null;
            String musicName;
            String musicSinger;
            String musicFullName;
            String musicUrl;
            JsonObject musicLyric;
            long musicMaxTime;
            String[] errMsg;
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
            MusicData musicData = new MusicData(errMsg, musicName, musicSinger, musicFullName,
                musicUrl, musicLyric, musicMaxTime, searchSourceName);
            switch (type) {
                case "all":
                    play(null, players, Lang.playAllSource
                        .replaceAll("%player%", ZMusic.player.getName(player)), time, musicData);
                    break;
                case "self":
                    play(player, new ArrayList<>(), "搜索", time, musicData);
                    break;
                case "music":
                    String s = Lang.musicMessage;
                    String prefix = "§a" + s.split("%fullName%")[0];
                    ZComponent message = ZTextComponent.of(Config.prefix + prefix
                        .replaceAll("%player%", ZMusic.player.getName(player))
                        .replaceAll("%source%", searchSourceName));
                    ZComponent music = ZTextComponent.of("§r[§e" + musicFullName + "§r]");
                    if (supportId) {
                        music.setClickEvent(ZClickEvent.runCommand("/zm play " + source + " -id:" + musicID));
                    } else {
                        music.setClickEvent(ZClickEvent.runCommand("/zm play " + source + " " + musicName));
                    }
                    music.setHoverEvent(ZHoverEvent.showText("§b" + Lang.clickPlayText));
                    message.addChild(music);
                    String suffix = s.split("%fullName%")[1];
                    message.addChild(ZTextComponent.of(suffix));
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

    private static void play(Object player, List<Object> players, String src, long time, MusicData musicData) {
        if (player != null) {
            players.add(player);
        }
        for (Object p : players) {
            LyricSender nextLyricSender = new LyricSender();
            nextLyricSender.player = p;
            nextLyricSender.lyric = musicData.lyric;
            nextLyricSender.maxTime = musicData.maxTime;
            nextLyricSender.name = musicData.name;
            nextLyricSender.singer = musicData.singer;
            nextLyricSender.fullName = musicData.fullName;
            nextLyricSender.platform = musicData.sourceName;
            nextLyricSender.src = src;
            nextLyricSender.url = musicData.url;
            synchronized (p) {
                PlayListPlayer plp = PlayerData.getPlayerPlayListPlayer(p);
                if (plp != null) {
                    plp.isStop = true;
                    PlayerData.setPlayerPlayListPlayer(p, null);
                }
                LyricSender previousLyricSender = PlayerData.getPlayerLyricSender(p);
                PlayerData.setPlayerLyricSender(p, nextLyricSender);
                if (previousLyricSender != null) {
                    previousLyricSender.stopThis();
                }
                OtherUtils.resetPlayerStatus(p);
                nextLyricSender.init();
                ZMusic.runTask.runAsync(nextLyricSender);
                ZMusic.music.play(musicData.url, p);
            }
            for (String msg : musicData.errorMessages) {
                if (!msg.isEmpty()) {
                    ZMusic.message.sendErrorMessage(msg, p);
                }
            }
            time = System.currentTimeMillis() - time;
            ZComponent success = ZTextComponent.of(Config.prefix + "§a" + Lang.playSuccess
                .replaceAll("%source%", musicData.sourceName)
                .replaceAll("%fullName%", musicData.fullName)
                .replaceAll("%time%", String.valueOf(time)));
            ZComponent stop = ZTextComponent.of("§r[§e" + Lang.clickStop + "§r]");
            stop.setClickEvent(ZClickEvent.runCommand("/zm stop"));
            success.addChild(stop);
            ZComponent loop = ZTextComponent.of("§r[§e" + Lang.clickLoop + "§r]");
            loop.setClickEvent(ZClickEvent.runCommand("/zm loop"));
            success.addChild(loop);
            ZMusic.message.sendJsonMessage(success, p);
            String title = "§a" + Lang.playing + "\n§e" + musicData.fullName;
            OtherUtils.sendAdv(p, title);
        }
    }

    private static final class MusicData {

        private final String[] errorMessages;
        private final String name;
        private final String singer;
        private final String fullName;
        private final String url;
        private final JsonObject lyric;
        private final long maxTime;
        private final String sourceName;

        private MusicData(String[] errorMessages, String name, String singer, String fullName,
                          String url, JsonObject lyric, long maxTime, String sourceName) {
            this.errorMessages = errorMessages;
            this.name = name;
            this.singer = singer;
            this.fullName = fullName;
            this.url = url;
            this.lyric = lyric;
            this.maxTime = maxTime;
            this.sourceName = sourceName;
        }
    }
}
