package me.zhenxin.zmusic.data;

import com.google.gson.JsonObject;
import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.api.bossbar.BossBar;
import me.zhenxin.zmusic.music.LyricSender;
import me.zhenxin.zmusic.music.PlayListPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家状态类，处理玩家状态信息等
 */
public class PlayerData {

    // 播放状态
    private static final Map<Object, Boolean> playingMap = new HashMap<>();
    // 当前播放音乐名称
    private static final Map<Object, String> musicNameMap = new HashMap<>();
    // 当前播放音乐歌手
    private static final Map<Object, String> musicSingerMap = new HashMap<>();
    // 当前播放音乐平台
    private static final Map<Object, String> platformMap = new HashMap<>();
    // 当前播放音乐平台
    private static final Map<Object, String> playSourceMap = new HashMap<>();
    // LyricSender
    private static final Map<Object, LyricSender> lyricSenderMap = new HashMap<>();
    // BossBar
    private static final Map<Object, BossBar> boosBarMap = new HashMap<>();
    // 播放进度：当前时间
    private static final Map<Object, Long> currentTimeMap = new HashMap<>();
    // 播放进度：最大时间
    private static final Map<Object, Long> maxTimeMap = new HashMap<>();
    // 当前进度的歌词
    private static final Map<Object, String> lyricMap = new HashMap<>();
    // 循环播放
    private static final Map<Object, Boolean> loopPlayMap = new HashMap<>();
    // 歌单播放器
    private static final Map<Object, PlayListPlayer> playListPlayer = new HashMap<>();
    // 歌单播放类型
    private static final Map<Object, String> playListType = new HashMap<>();

    private static final Map<Object, JsonObject> jsonMap = new HashMap<>();

    /**
     * 获取玩家播放状态
     *
     * @param player 玩家
     * @return 播放状态
     */
    public static Boolean getPlayerPlayStatus(Object player) {
        return playingMap.get(player);
    }

    /**
     * 设置玩家播放状态
     *
     * @param player  玩家
     * @param playing 播放状态
     */
    public static void setPlayerPlayStatus(Object player, Boolean playing) {
        playingMap.put(player, playing);
    }

    /**
     * 获取玩家当前播放音乐名称
     *
     * @param player 玩家
     * @return 当前播放音乐名称
     */
    public static String getPlayerMusicName(Object player) {
        return musicNameMap.get(player);
    }

    /**
     * 设置玩家当前播放音乐名称
     *
     * @param player    玩家
     * @param musicName 音乐名称
     */
    public static void setPlayerMusicName(Object player, String musicName) {
        musicNameMap.put(player, musicName);
        if (ZMusic.isBC) {
            JsonObject json = getJson(player);
            json.addProperty("name", musicName);
            jsonMap.put(player, json);
            ZMusic.send.sendToZMusicAddon(player, json.toString());
        }
    }

    private static JsonObject getJson(Object player) {
        JsonObject json = jsonMap.get(player);
        if (json == null) {
            json = new JsonObject();
            json.addProperty("name", "");
            json.addProperty("singer", "");
            json.addProperty("lyric", "");
            json.addProperty("currentTime", "");
            json.addProperty("maxTime", "");
            json.addProperty("platform", "");
            json.addProperty("src", "");
        }
        return json;
    }

    /**
     * 获取玩家当前播放音乐歌手
     *
     * @param player 玩家
     * @return 当前播放音乐歌手
     */
    public static String getPlayerMusicSinger(Object player) {
        return musicSingerMap.get(player);
    }

    /**
     * 设置玩家当前播放音乐歌手
     *
     * @param player     玩家
     * @param singerName 音乐歌手
     */
    public static void setPlayerMusicSinger(Object player, String singerName) {
        musicSingerMap.put(player, singerName);
        if (ZMusic.isBC) {
            JsonObject json = getJson(player);
            json.addProperty("singer", singerName);
            jsonMap.put(player, json);
            ZMusic.send.sendToZMusicAddon(player, json.toString());
        }
    }

    /**
     * 获取玩家当前播放音乐平台
     *
     * @param player 玩家
     * @return 当前播放音乐平台
     */
    public static String getPlayerPlatform(Object player) {
        return platformMap.get(player);
    }

    /**
     * 设置玩家当前播放音乐平台
     *
     * @param player   玩家
     * @param platform 音乐平台
     */
    public static void setPlayerPlatform(Object player, String platform) {
        platformMap.put(player, platform);
        if (ZMusic.isBC) {
            JsonObject json = getJson(player);
            json.addProperty("platform", platform);
            jsonMap.put(player, json);
            ZMusic.send.sendToZMusicAddon(player, json.toString());
        }
    }

    /**
     * 获取玩家当前播放音乐来源
     *
     * @param player 玩家
     * @return 当前播放音乐来源
     */
    public static String getPlayerPlaySource(Object player) {
        return playSourceMap.get(player);
    }

    /**
     * 设置玩家当前播放音乐来源
     *
     * @param player 玩家
     * @param src    音乐来源
     */
    public static void setPlayerPlaySource(Object player, String src) {
        playSourceMap.put(player, src);
        if (ZMusic.isBC) {
            JsonObject json = getJson(player);
            json.addProperty("src", src);
            jsonMap.put(player, json);
            ZMusic.send.sendToZMusicAddon(player, json.toString());
        }
    }

    /**
     * 获取玩家LyricSender
     *
     * @param player 玩家
     * @return LyricSender
     */
    public static LyricSender getPlayerLyricSender(Object player) {
        return lyricSenderMap.get(player);
    }

    /**
     * 设置玩家LyricSender
     *
     * @param player      玩家
     * @param lyricSender LyricSender
     */
    public static void setPlayerLyricSender(Object player, LyricSender lyricSender) {
        lyricSenderMap.put(player, lyricSender);
    }

    /**
     * 获取玩家BossBar
     *
     * @param player 玩家
     * @return BossBar
     */
    public static BossBar getPlayerBoosBar(Object player) {
        return boosBarMap.get(player);
    }

    /**
     * 设置玩家BossBar
     *
     * @param player  玩家
     * @param bossBar BossBar
     */
    public static void setPlayerBoosBar(Object player, BossBar bossBar) {
        boosBarMap.put(player, bossBar);
    }

    /**
     * 获取玩家当前播放时间
     *
     * @param player 玩家
     * @return 时间(秒)
     */
    public static Long getPlayerCurrentTime(Object player) {
        return currentTimeMap.get(player);
    }

    /**
     * 设置玩家当前播放时间
     *
     * @param player 玩家
     * @param time   时间(秒)
     */
    public static void setPlayerCurrentTime(Object player, Long time) {
        currentTimeMap.put(player, time);
        if (ZMusic.isBC) {
            JsonObject json = getJson(player);
            json.addProperty("currentTime", time);
            ZMusic.send.sendToZMusicAddon(player, json.toString());
        }
    }

    /**
     * 获取玩家最大播放时间
     *
     * @param player 玩家
     * @return 时间(秒)
     */
    public static Long getPlayerMaxTime(Object player) {
        return maxTimeMap.get(player);
    }

    /**
     * 设置玩家最大播放时间
     *
     * @param player 玩家
     * @param time   时间(秒)
     */
    public static void setPlayerMaxTime(Object player, Long time) {
        maxTimeMap.put(player, time);
        if (ZMusic.isBC) {
            JsonObject json = getJson(player);
            json.addProperty("maxTime", time);
            jsonMap.put(player, json);
            ZMusic.send.sendToZMusicAddon(player, json.toString());
        }
    }

    /**
     * 获取玩家最大播放时间
     *
     * @param player 玩家
     * @return 歌词
     */
    public static String getPlayerLyric(Object player) {
        return lyricMap.get(player);
    }

    /**
     * 设置玩家最大播放时间
     *
     * @param player 玩家
     * @param lyric  歌词
     */
    public static void setPlayerLyric(Object player, String lyric) {
        lyricMap.put(player, lyric);
        if (ZMusic.isBC) {
            JsonObject json = getJson(player);
            json.addProperty("lyric", lyric);
            jsonMap.put(player, json);
            ZMusic.send.sendToZMusicAddon(player, json.toString());
        }
    }

    /**
     * 获取玩家循环播放状态
     *
     * @param player 玩家
     * @return 状态
     */
    public static Boolean getPlayerLoopPlay(Object player) {
        return loopPlayMap.get(player);
    }

    /**
     * 设置玩家循环播放状态
     *
     * @param player 玩家
     * @param loop   状态
     */
    public static void setPlayerLoopPlay(Object player, Boolean loop) {
        loopPlayMap.put(player, loop);
    }

    /**
     * 获取玩家歌单播放器
     *
     * @param player 玩家
     * @return 歌单播放器
     */
    public static PlayListPlayer getPlayerPlayListPlayer(Object player) {
        return playListPlayer.get(player);
    }

    /**
     * 设置玩家歌单播放器
     *
     * @param player 玩家
     * @param plp    歌单播放器
     */
    public static void setPlayerPlayListPlayer(Object player, PlayListPlayer plp) {
        playListPlayer.put(player, plp);
    }

    /**
     * 获取玩家歌单播放类型
     *
     * @param player 玩家
     * @return 歌单播放类型
     */
    public static String getPlayerPlayListType(Object player) {
        return playListType.get(player);
    }

    /**
     * 设置玩家歌单播放类型
     *
     * @param player 玩家
     * @param type   歌单播放类型
     */
    public static void setPlayerPlayListType(Object player, String type) {
        playListType.put(player, type);
    }

}
