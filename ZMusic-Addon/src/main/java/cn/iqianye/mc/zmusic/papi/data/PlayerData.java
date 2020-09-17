package cn.iqianye.mc.zmusic.papi.data;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家状态类，处理玩家状态信息等
 */
public class PlayerData {

    // 当前播放音乐名称
    private static final Map<Object, String> musicNameMap = new HashMap<>();
    // 当前播放音乐歌手
    private static final Map<Object, String> musicSingerMap = new HashMap<>();
    // 当前播放音乐平台
    private static final Map<Object, String> platformMap = new HashMap<>();
    // 当前播放音乐平台
    private static final Map<Object, String> playSourceMap = new HashMap<>();
    // 播放进度：当前时间
    private static final Map<Object, Long> currentTimeMap = new HashMap<>();
    // 播放进度：最大时间
    private static final Map<Object, Long> maxTimeMap = new HashMap<>();
    // 当前进度的歌词
    private static final Map<Object, String> lyricMap = new HashMap<>();

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
     * @param player    玩家
     * @param musicName 音乐平台
     */
    public static void setPlayerPlatform(Object player, String musicName) {
        platformMap.put(player, musicName);
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
     * @param player    玩家
     * @param musicName 音乐来源
     */
    public static void setPlayerPlaySource(Object player, String musicName) {
        playSourceMap.put(player, musicName);
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
    }

}
