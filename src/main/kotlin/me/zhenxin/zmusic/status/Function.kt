package me.zhenxin.zmusic.status

import me.zhenxin.zmusic.bossbar.impl.BossBarBukkit
import me.zhenxin.zmusic.bossbar.impl.BossBarBungee
import me.zhenxin.zmusic.bossbar.impl.BossBarVelocity
import me.zhenxin.zmusic.enums.MusicPlatform
import me.zhenxin.zmusic.enums.PlayMode
import me.zhenxin.zmusic.music.MusicPlayer
import taboolib.common.platform.Platform
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.runningPlatform


fun ProxyPlayer.setState(
    playing: Boolean? = null,
    name: String? = null,
    singer: String? = null,
    album: String? = null,
    platform: MusicPlatform? = null,
    time: Long? = null,
    currentTime: Long? = null,
    lyric: String? = null,
    mode: PlayMode? = null
) {
    val state = PlayerState.STATE[this]
    if (state != null) {
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
        }
        PlayerState.STATE[this] = state
    }
}

fun ProxyPlayer.createBossBar() {
    when (runningPlatform) {
        Platform.BUKKIT -> PlayerState.BOSS_BAR[this] = BossBarBukkit(this)
        Platform.BUNGEE -> PlayerState.BOSS_BAR[this] = BossBarBungee(this)
        Platform.VELOCITY -> PlayerState.BOSS_BAR[this] = BossBarVelocity(this)
        else -> throw UnsupportedOperationException("BossBar is not supported on this platform")
    }
}

fun ProxyPlayer.getBossBar() = PlayerState.BOSS_BAR[this] ?: throw IllegalStateException("BossBar is not created")

fun ProxyPlayer.removeBossBar() {
    val bossBar = PlayerState.BOSS_BAR[this]
    if (bossBar != null) {
        bossBar.stop()
        PlayerState.BOSS_BAR.remove(this)
    }
}

fun ProxyPlayer.isPlaying(): Boolean {
    val state = PlayerState.STATE[this]
    return state?.playing ?: false
}

fun ProxyPlayer.setMusicPlayer(musicPlayer: MusicPlayer) = PlayerState.MUSIC_PLAYER.put(this, musicPlayer)

fun ProxyPlayer.getMusicPlayer() =
    PlayerState.MUSIC_PLAYER[this] ?: throw IllegalStateException("MusicPlayer is not created")

fun ProxyPlayer.removeMusicPlayer() {
    val player = PlayerState.MUSIC_PLAYER[this]
    if (player != null) {
        player.cancel()
        PlayerState.MUSIC_PLAYER.remove(this)
    }
}