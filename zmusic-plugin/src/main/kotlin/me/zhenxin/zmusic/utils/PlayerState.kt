package me.zhenxin.zmusic.status

import me.zhenxin.zmusic.entity.StateInfo
import me.zhenxin.zmusic.enums.MusicPlatform
import me.zhenxin.zmusic.enums.PlayMode
import me.zhenxin.zmusic.module.bossbar.BossBar
import me.zhenxin.zmusic.module.bossbar.impl.BossBarBukkit
import me.zhenxin.zmusic.module.bossbar.impl.BossBarBungee
import me.zhenxin.zmusic.module.bossbar.impl.BossBarVelocity
import me.zhenxin.zmusic.module.music.MusicPlayer
import taboolib.common.platform.Platform
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.runningPlatform


/**
 * 设置玩家状态
 *
 * @param playing 播放状态
 * @param name 音乐名称
 * @param singer 音乐歌手
 * @param album 音乐专辑
 * @param platform 音乐平台
 * @param time 音乐时长
 * @param currentTime 当前播放时间
 * @param lyric 当前歌词
 * @param mode 播放模式
 * @param player 播放器
 * @param bossBar BossBar
 */
fun ProxyPlayer.setState(
    playing: Boolean? = null,
    name: String? = null,
    singer: String? = null,
    album: String? = null,
    platform: MusicPlatform? = null,
    time: Int? = null,
    currentTime: Int? = null,
    lyric: String? = null,
    mode: PlayMode? = null,
    player: MusicPlayer? = null,
    bossBar: BossBar? = null
) {
    val state = playerState[this.name] ?: StateInfo()
    state.also {
        it.playing = playing ?: it.playing
        it.name = name ?: it.name
        it.singer = singer ?: it.singer
        it.album = album ?: it.album
        it.platform = platform ?: it.platform
        it.time = time ?: it.time
        it.currentTime = currentTime ?: it.currentTime
        it.lyric = lyric ?: it.lyric
        it.mode = mode ?: it.mode
        it.bossBar = bossBar ?: it.bossBar
        it.player = player ?: it.player
    }
    playerState[this.name] = state
}

/**
 * 获取玩家状态
 *
 * @return StateInfo 玩家状态
 */
fun ProxyPlayer.getState(): StateInfo {
    return playerState[this.name] ?: StateInfo()
}

/**
 * 创建BossBar
 */
fun ProxyPlayer.createBossBar() {
    val bossBar = when (runningPlatform) {
        Platform.BUKKIT -> BossBarBukkit(this)
        Platform.BUNGEE -> BossBarBungee(this)
        Platform.VELOCITY -> BossBarVelocity(this)
        else -> throw IllegalStateException("Unsupported platform")
    }
    setState(bossBar = bossBar)
}

/**
 * 移除BossBar
 */
fun ProxyPlayer.removeBossBar() {
    val bossBar = getState().bossBar ?: return
    bossBar.stop()
    playerState[this.name]?.bossBar = null
}

/**
 * 玩家状态信息
 */
val playerState = mutableMapOf<String, StateInfo>()