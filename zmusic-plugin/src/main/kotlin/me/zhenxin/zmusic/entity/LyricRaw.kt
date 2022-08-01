package me.zhenxin.zmusic.entity

/**
 * 歌词信息
 *
 * @author 真心
 * @since 2022/1/9 12:26
 */
data class LyricRaw(
    var time: Int = 0,
    var content: String = "",
    var translation: String = ""
)
