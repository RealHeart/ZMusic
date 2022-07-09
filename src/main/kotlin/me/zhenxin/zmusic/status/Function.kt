package me.zhenxin.zmusic.status

import me.zhenxin.zmusic.bossbar.BossBar
import me.zhenxin.zmusic.bossbar.impl.BossBarBukkit
import me.zhenxin.zmusic.bossbar.impl.BossBarBungee
import me.zhenxin.zmusic.bossbar.impl.BossBarVelocity
import me.zhenxin.zmusic.music.MusicPlayer
import taboolib.common.platform.Platform
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.runningPlatform

/**
 * PlayerState 扩展函数
 *
 * @author 真心
 * @since 2022/7/5 17:06
 * @email qgzhenxin@qq.com
 */
fun ProxyPlayer.createBossBar() {
    when (runningPlatform) {
        Platform.BUKKIT -> PlayerState.BOSS_BAR[this] = BossBarBukkit(this)
        Platform.BUNGEE -> PlayerState.BOSS_BAR[this] = BossBarBungee(this)
        Platform.VELOCITY -> PlayerState.BOSS_BAR[this] = BossBarVelocity(this)
        else -> throw UnsupportedOperationException("BossBar is not supported on this platform")
    }
}

fun ProxyPlayer.getBossBar() = PlayerState.BOSS_BAR[this] ?: throw IllegalStateException("BossBar is not created")

fun ProxyPlayer.removeBossBar() = PlayerState.BOSS_BAR.remove(this)

fun ProxyPlayer.setPlaying(playing: Boolean) = PlayerState.PLAYING.put(this, playing)
fun ProxyPlayer.isPlaying() = PlayerState.PLAYING[this] ?: false

fun ProxyPlayer.setMusicPlayer(musicPlayer: MusicPlayer) = PlayerState.MUSIC_PLAYER.put(this, musicPlayer)

fun ProxyPlayer.getMusicPlayer() =
    PlayerState.MUSIC_PLAYER[this] ?: throw IllegalStateException("MusicPlayer is not created")

fun ProxyPlayer.removeMusicPlayer() = PlayerState.MUSIC_PLAYER.remove(this)