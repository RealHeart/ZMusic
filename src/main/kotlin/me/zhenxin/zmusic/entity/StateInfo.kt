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
    val playing: Boolean,
    /** 歌名 */
    val name: String,
    /** 歌手 */
    val singer: String,
    /** 专辑 */
    val album: String,
    /** 平台 */
    val platform: MusicPlatform,
    /** 最大播放时间 */
    val time: Long,
    /** 当前播放时间 */
    val currentTime: Long,
    /** 歌词 */
    val lyric: Long,
    /** 播放模式 */
    val mode: PlayMode
)