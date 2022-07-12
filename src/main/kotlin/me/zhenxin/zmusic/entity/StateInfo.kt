package me.zhenxin.zmusic.entity

import me.zhenxin.zmusic.enums.MusicPlatform
import me.zhenxin.zmusic.enums.PlayMode

/**
 * 状态信息
 *
 * @author 真心
 * @since 2021/11/8 13:25
 * @email qgzhenxin@qq.com
 */
data class StateInfo(
    /** 是否正在播放 */
    var playing: Boolean,
    /** 歌名 */
    var name: String,
    /** 歌手 */
    var singer: String,
    /** 专辑 */
    var album: String,
    /** 平台 */
    var platform: MusicPlatform,
    /** 最大播放时间 */
    var time: Long,
    /** 当前播放时间 */
    var currentTime: Long,
    /** 歌词 */
    var lyric: String,
    /** 播放模式 */
    var mode: PlayMode
)