package me.zhenxin.zmusic.entity

/**
 * 通信音乐消息
 *
 * @author 真心
 * @since 2022/7/28 11:31
 * @email qgzhenxin@qq.com
 */
data class BridgeMusicInfo(
    val name: String? = null,
    val singer: String? = null,
    val lyric: String? = null,
    val currentTime: Int? = null,
    val maxTime: Int? = null
)
