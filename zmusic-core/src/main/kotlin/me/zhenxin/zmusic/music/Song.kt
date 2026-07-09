package me.zhenxin.zmusic.music

/**
 * 音乐搜索结果。
 *
 * @property id 平台内歌曲 ID
 * @property source 歌曲来源
 * @property title 歌曲标题
 * @property artists 艺术家列表
 * @property audioUrl 可直接播放的音频地址
 * @author 真心
 * @since 5.0.0
 */
data class Song(
    val id: String,
    val source: String,
    val title: String,
    val artists: List<String>,
    val audioUrl: String? = null
)
