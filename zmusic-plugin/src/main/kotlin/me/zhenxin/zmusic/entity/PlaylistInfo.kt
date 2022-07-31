package me.zhenxin.zmusic.entity

/**
 * 歌单信息
 *
 * @author 真心
 * @since 2021/9/25 21:05
 * @email qgzhenxin@qq.com
 */
data class PlaylistInfo(
    /** 歌单ID */
    val id: String,
    /** 歌单名称 */
    val name: String,
    /** 歌单歌曲列表 */
    val musics: List<MusicInfo>
)