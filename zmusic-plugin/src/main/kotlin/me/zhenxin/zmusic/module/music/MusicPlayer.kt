package me.zhenxin.zmusic.module.music

import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.entity.BridgeMusicInfo
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.entity.MusicInfo
import me.zhenxin.zmusic.enums.PlayMode
import me.zhenxin.zmusic.enums.PlayMode.*
import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.module.bossbar.BossBar
import me.zhenxin.zmusic.module.taboolib.resetData
import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.status.createBossBar
import me.zhenxin.zmusic.status.getState
import me.zhenxin.zmusic.status.removeBossBar
import me.zhenxin.zmusic.status.setState
import me.zhenxin.zmusic.utils.colored
import me.zhenxin.zmusic.utils.playMusic
import me.zhenxin.zmusic.utils.sendBridgeInfo
import me.zhenxin.zmusic.utils.stopMusic
import taboolib.common.platform.Platform
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.runningPlatform
import taboolib.common.platform.function.submit
import java.util.*

/**
 * 音乐播放器
 *
 * @author 真心
 * @since 2022/1/24 10:20
 */
class MusicPlayer(
    private val player: ProxyPlayer,
    private val api: MusicApi,
    private val musicList: MutableList<MusicInfo>,
    private val mode: PlayMode = SINGLE
) {
    private var currentIndex = 0
    private lateinit var currentMusic: MusicInfo
    private lateinit var bossBar: BossBar
    private var currentLyric: MutableList<LyricRaw> = mutableListOf()

    private var currentTime = 0
    private var currentLyricString = ""

    private var playing = true

    private fun play() {
        val url = api.getPlayUrl(currentMusic.id)
        player.playMusic(url)
        // bossBar.start()
    }

    private fun run() {
        while (playing) {
            if (!player.isOnline()) {
                logger.debug("[Thread:${Thread.currentThread().id}]玩家离线, 线程终止")
                stop()
            }
            currentTime += 1
            sendLyric()
            updateState()
            checkMode()
            Thread.sleep(1000)
        }
    }

    fun stop() {
        playing = false
        player.stopMusic()
        player.removeBossBar()
        player.setState(playing = false)
        player.setState(player = null)
    }

    fun start() {
        player.resetData()
        currentMusic = musicList[currentIndex]
        currentLyric = api.getLyric(currentMusic.id)
        player.createBossBar()
        bossBar = player.getState().bossBar!!
        bossBar.setTitle(config.LYRIC_COLOR.colored() + currentMusic.fullName)
        bossBar.setTime(currentMusic.duration.toFloat())
        player.setState(bossBar = bossBar)
        play()
        submit(async = true) { run() }
    }

    private fun sendLyric() {
        submit(async = true) {
            val lyric = currentLyric.find { it.time == currentTime } ?: return@submit
            currentLyricString = lyric.content
            val content = "${config.LYRIC_COLOR.colored()}${lyric.content}"
            if (config.LYRIC_BOSS_BAR) {
                bossBar.setTitle(content)
            }
            if (config.LYRIC_ACTION_BAR) {
                player.sendActionBar(content)
            }
            if (config.LYRIC_CHAT) {
                player.sendMsg(content)
            }
        }
    }

    private fun updateState() {
        if (runningPlatform == Platform.BUNGEE || runningPlatform == Platform.VELOCITY) {
            player.sendBridgeInfo(
                BridgeMusicInfo(
                    name = currentMusic.name,
                    singer = currentMusic.singer,
                    lyric = currentLyricString,
                    currentTime = currentTime,
                    maxTime = currentMusic.duration
                )
            )
        }
        player.setState(
            playing = playing,
            name = currentMusic.name,
            singer = currentMusic.singer,
            album = currentMusic.albumName,
            platform = null,
            time = currentMusic.duration,
            currentTime = currentTime,
            lyric = currentLyricString,
            mode = mode
        )
    }

    private fun checkMode() {
        if (currentTime >= currentMusic.duration) {
            when (mode) {
                SINGLE -> {
                    stop()
                }

                SINGLE_LOOP -> {
                    currentTime = 0
                    play()
                }

                LIST -> {
                    if (currentIndex == musicList.size - 1) {
                        stop()
                    } else {
                        currentIndex++
                    }
                    currentMusic = musicList[currentIndex]
                    currentLyric = api.getLyric(currentMusic.id)
                    bossBar.setTitle(currentMusic.fullName)
                    bossBar.setTime(currentMusic.duration.toFloat())
                    currentTime = 0
                    play()
                }

                LIST_LOOP -> {
                    if (currentIndex == musicList.size - 1) {
                        currentIndex = 0
                    } else {
                        currentIndex++
                    }
                    currentMusic = musicList[currentIndex]
                    currentLyric = api.getLyric(currentMusic.id)
                    bossBar.setTitle(currentMusic.fullName)
                    bossBar.setTime(currentMusic.duration.toFloat())
                    currentTime = 0
                    play()
                }

                LIST_RANDOM -> {
                    currentIndex = Random().nextInt(musicList.size)
                    currentMusic = musicList[currentIndex]
                    currentLyric = api.getLyric(currentMusic.id)
                    bossBar.setTitle(currentMusic.fullName)
                    bossBar.setTime(currentMusic.duration.toFloat())
                    currentTime = 0
                    play()
                }
            }
        }
    }
}
