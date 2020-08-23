package cn.iqianye.mc.zmusic.player;

import cn.iqianye.mc.zmusic.api.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * 玩家状态类，处理玩家状态信息等
 */
public class PlayerStatus {

    // 播放状态
    private static Map<Player, Boolean> playingMap = new HashMap<>();
    // 当前播放音乐名称
    private static Map<Player, String> musicNameMap = new HashMap<>();
    // Timer
    private static Map<Player, Timer> timerMap = new HashMap<>();
    // BossBar
    private static Map<Player, BossBar> boosBarMap = new HashMap<>();
    // 播放进度：当前时间
    private static Map<Player, Integer> currentTimeMap = new HashMap<>();
    // 播放进度：最大时间
    private static Map<Player, Integer> maxTimeMap = new HashMap<>();
    // 当前进度的歌词
    private static Map<Player, String> lyricMap = new HashMap<>();
    // 循环播放
    private static Map<Player, Boolean> loopPlayMap = new HashMap<>();

    /**
     * 获取玩家播放状态
     *
     * @param player 玩家
     * @return 播放状态
     */
    public static Boolean getPlayerPlayStatus(Player player) {
        return playingMap.get(player);
    }

    /**
     * 设置玩家播放状态
     *
     * @param player  玩家
     * @param playing 播放状态
     */
    public static void setPlayerPlayStatus(Player player, Boolean playing) {
        playingMap.put(player, playing);
    }

    /**
     * 获取玩家当前播放音乐名称
     *
     * @param player 玩家
     * @return 当前播放音乐名称
     */
    public static String getPlayerMusicName(Player player) {
        return musicNameMap.get(player);
    }

    /**
     * 设置玩家当前播放音乐名称
     *
     * @param player    玩家
     * @param musicName 音乐名称
     */
    public static void setPlayerMusicName(Player player, String musicName) {
        musicNameMap.put(player, musicName);
    }

    /**
     * 获取玩家Timer
     *
     * @param player 玩家
     * @return Timer
     */
    public static Timer getPlayerTimer(Player player) {
        return timerMap.get(player);
    }

    /**
     * 设置玩家Timer
     *
     * @param player 玩家
     * @param timer  Timer
     */
    public static void setPlayerTimer(Player player, Timer timer) {
        timerMap.put(player, timer);
    }

    /**
     * 获取玩家BossBar
     *
     * @param player 玩家
     * @return BossBar
     */
    public static BossBar getPlayerBoosBar(Player player) {
        return boosBarMap.get(player);
    }

    /**
     * 设置玩家BossBar
     *
     * @param player  玩家
     * @param bossBar BossBar
     */
    public static void setPlayerBoosBar(Player player, BossBar bossBar) {
        boosBarMap.put(player, bossBar);
    }

    /**
     * 获取玩家当前播放时间
     *
     * @param player 玩家
     * @return 时间(秒)
     */
    public static Integer getPlayerCurrentTime(Player player) {
        return currentTimeMap.get(player);
    }

    /**
     * 设置玩家当前播放时间
     *
     * @param player 玩家
     * @param time   时间(秒)
     */
    public static void setPlayerCurrentTime(Player player, Integer time) {
        currentTimeMap.put(player, time);
    }

    /**
     * 获取玩家最大播放时间
     *
     * @param player 玩家
     * @return 时间(秒)
     */
    public static Integer getPlayerMaxTime(Player player) {
        return maxTimeMap.get(player);
    }

    /**
     * 设置玩家最大播放时间
     *
     * @param player 玩家
     * @param time   时间(秒)
     */
    public static void setPlayerMaxTime(Player player, Integer time) {
        maxTimeMap.put(player, time);
    }

    /**
     * 获取玩家最大播放时间
     *
     * @param player 玩家
     * @return 歌词
     */
    public static String getPlayerLyric(Player player) {
        return lyricMap.get(player);
    }

    /**
     * 设置玩家最大播放时间
     *
     * @param player 玩家
     * @param lyric  歌词
     */
    public static void setPlayerLyric(Player player, String lyric) {
        lyricMap.put(player, lyric);
    }

    /**
     * 获取玩家循环播放状态
     *
     * @param player 玩家
     * @return 状态
     */
    public static Boolean getPlayerLoopPlay(Player player) {
        return loopPlayMap.get(player);
    }

    /**
     * 设置玩家循环播放状态
     *
     * @param player 玩家
     * @param loop   状态
     */
    public static void setPlayerLoopPlay(Player player, Boolean loop) {
        loopPlayMap.put(player, loop);
    }

}
