package me.zhenxin.zmusic.entity

import me.zhenxin.zmusic.enums.MusicPlatform

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
    val musicName: String,
    /** 歌手 */
    val musicSinger: String,
    /** 专辑 */
    val album: String,
    /** 平台 */
    val platform: MusicPlatform,
    /** 当前播放时间 */
    val currentTime: Long,
    /** 最大播放时间 */
    val maxTime: Long,
    /** 歌词 */
    val lyric: Long,
    /** 是否循环播放 */
    val loop: Boolean
)