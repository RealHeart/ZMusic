package me.zhenxin.zmusic.entity

import me.zhenxin.zmusic.enums.MusicPlatform
import me.zhenxin.zmusic.enums.PlayMode
import me.zhenxin.zmusic.module.bossbar.BossBar
import me.zhenxin.zmusic.module.music.MusicPlayer

/**
 * 状态信息
 *
 * @author 真心
 * @since 2021/11/8 13:25
 * @email qgzhenxin@qq.com
 */
data class StateInfo(
    /** 是否正在播放 */
    var playing: Boolean? = null,
    /** 歌名 */
    var name: String? = null,
    /** 歌手 */
    var singer: String? = null,
    /** 专辑 */
    var album: String? = null,
    /** 平台 */
    var platform: MusicPlatform? = null,
    /** 最大播放时间 */
    var time: Int? = null,
    /** 当前播放时间 */
    var currentTime: Int? = null,
    /** 歌词 */
    var lyric: String? = null,
    /** 播放模式 */
    var mode: PlayMode? = null,
    /** 播放器 */
    var player: MusicPlayer? = null,
    /** BossBar */
    var bossBar: BossBar? = null
)