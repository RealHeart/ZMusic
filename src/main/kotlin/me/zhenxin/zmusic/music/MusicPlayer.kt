package me.zhenxin.zmusic.music

import me.zhenxin.zmusic.api.MusicApi
import me.zhenxin.zmusic.api.MusicInfo
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.enums.PlayMode
import me.zhenxin.zmusic.enums.PlayMode.*
import me.zhenxin.zmusic.utils.playMusic
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit

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
    private var currentLyric: MutableList<LyricRaw> = mutableListOf()

    private fun play() {
        val url = api.getPlayUrl(currentMusic.id)
        player.playMusic(url)
    }

    private fun sendLyric() {
    }

    fun start() {
        when (mode) {
            SINGLE -> {
                currentMusic = musicList[currentIndex]
                val url = api.getPlayUrl(currentMusic.id)
                player.playMusic(url)
                submit(async = true) {
                    currentLyric = api.getLyric(currentMusic.id)
                    play()
                    sendLyric()
                }
            }
            SINGLE_LOOP -> TODO()
            LIST -> TODO()
            LIST_LOOP -> TODO()
            LIST_RANDOM -> TODO()
        }
    }

}