package me.zhenxin.zmusic.common.modules.api.data

/**
 * 音乐信息
 *
 * @author 真心
 * @since 2021/8/5 12:54
 * @email qgzhenxin@qq.com
 */
data class MusicInfo(
    val id: String,
    val name: String,
    val singer: String,
    val albumName: String,
    val albumImage: String,
    val duration: Int
)
