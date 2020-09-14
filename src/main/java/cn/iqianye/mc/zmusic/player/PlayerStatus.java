package cn.iqianye.mc.zmusic.player;

import cn.iqianye.mc.zmusic.api.BossBar;
import cn.iqianye.mc.zmusic.music.LyricSender;
import cn.iqianye.mc.zmusic.music.PlayListPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家状态类，处理玩家状态信息等
 */
public class PlayerStatus {

    // 播放状态
    private static final Map<Player, Boolean> playingMap = new HashMap<>();
    // 当前播放音乐名称
    private static final Map<Player, String> musicNameMap = new HashMap<>();
    // 当前播放音乐歌手
    private static final Map<Player, String> musicSingerMap = new HashMap<>();
    // 当前播放音乐平台
    private static final Map<Player, String> platformMap = new HashMap<>();
    // 当前播放音乐平台
    private static final Map<Player, String> playSourceMap = new HashMap<>();
    // LyricSender
    private static final Map<Player, LyricSender> lyricSenderMap = new HashMap<>();
    // BossBar
    private static final Map<Player, BossBar> boosBarMap = new HashMap<>();
    // 播放进度：当前时间
    private static final Map<Player, Long> currentTimeMap = new HashMap<>();
    // 播放进度：最大时间
    private static final Map<Player, Long> maxTimeMap = new HashMap<>();
    // 当前进度的歌词
    private static final Map<Player, String> lyricMap = new HashMap<>();
    // 循环播放
    private static final Map<Player, Boolean> loopPlayMap = new HashMap<>();
    // 歌单播放器
    private static final Map<Player, PlayListPlayer> playListPlayer = new HashMap<>();
    // 歌单播放类型
    private static final Map<Player, String> playListType = new HashMap<>();

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
     * 获取玩家当前播放音乐歌手
     *
     * @param player 玩家
     * @return 当前播放音乐歌手
     */
    public static String getPlayerMusicSinger(Player player) {
        return musicSingerMap.get(player);
    }

    /**
     * 设置玩家当前播放音乐歌手
     *
     * @param player     玩家
     * @param singerName 音乐歌手
     */
    public static void setPlayerMusicSinger(Player player, String singerName) {
        musicSingerMap.put(player, singerName);
    }

    /**
     * 获取玩家当前播放音乐平台
     *
     * @param player 玩家
     * @return 当前播放音乐平台
     */
    public static String getPlayerPlatform(Player player) {
        return platformMap.get(player);
    }

    /**
     * 设置玩家当前播放音乐平台
     *
     * @param player    玩家
     * @param musicName 音乐平台
     */
    public static void setPlayerPlatform(Player player, String musicName) {
        platformMap.put(player, musicName);
    }

    /**
     * 获取玩家当前播放音乐来源
     *
     * @param player 玩家
     * @return 当前播放音乐来源
     */
    public static String getPlayerPlaySource(Player player) {
        return playSourceMap.get(player);
    }

    /**
     * 设置玩家当前播放音乐来源
     *
     * @param player    玩家
     * @param musicName 音乐来源
     */
    public static void setPlayerPlaySource(Player player, String musicName) {
        playSourceMap.put(player, musicName);
    }

    /**
     * 获取玩家LyricSender
     *
     * @param player 玩家
     * @return LyricSender
     */
    public static LyricSender getPlayerLyricSender(Player player) {
        return lyricSenderMap.get(player);
    }

    /**
     * 设置玩家LyricSender
     *
     * @param player      玩家
     * @param lyricSender LyricSender
     */
    public static void setPlayerLyricSender(Player player, LyricSender lyricSender) {
        lyricSenderMap.put(player, lyricSender);
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
    public static Long getPlayerCurrentTime(Player player) {
        return currentTimeMap.get(player);
    }

    /**
     * 设置玩家当前播放时间
     *
     * @param player 玩家
     * @param time   时间(秒)
     */
    public static void setPlayerCurrentTime(Player player, Long time) {
        currentTimeMap.put(player, time);
    }

    /**
     * 获取玩家最大播放时间
     *
     * @param player 玩家
     * @return 时间(秒)
     */
    public static Long getPlayerMaxTime(Player player) {
        return maxTimeMap.get(player);
    }

    /**
     * 设置玩家最大播放时间
     *
     * @param player 玩家
     * @param time   时间(秒)
     */
    public static void setPlayerMaxTime(Player player, Long time) {
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

    /**
     * 获取玩家歌单播放器
     *
     * @param player 玩家
     * @return 歌单播放器
     */
    public static PlayListPlayer getPlayerPlayListPlayer(Player player) {
        return playListPlayer.get(player);
    }

    /**
     * 设置玩家歌单播放器
     *
     * @param player 玩家
     * @param plp    歌单播放器
     */
    public static void setPlayerPlayListPlayer(Player player, PlayListPlayer plp) {
        playListPlayer.put(player, plp);
    }

    /**
     * 获取玩家歌单播放类型
     *
     * @param player 玩家
     * @return 歌单播放类型
     */
    public static String getPlayerPlayListType(Player player) {
        return playListType.get(player);
    }

    /**
     * 设置玩家歌单播放类型
     *
     * @param player 玩家
     * @param type   歌单播放类型
     */
    public static void setPlayerPlayListType(Player player, String type) {
        playListType.put(player, type);
    }
}
