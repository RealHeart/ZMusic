package me.zhenxin.zmusic.module.api.data

/**
 * 音乐信息
 *
 * @author 真心
 * @since 2021/8/5 12:54
 * @email qgzhenxin@qq.com
 */
data class MusicInfo(
    /**音乐ID */
    val id: String,
    /** 音乐名称 */
    val name: String,
    /** 歌手名称 */
    val singer: String,
    /** 专辑名称 */
    val albumName: String,
    /** 专辑图片 */
    val albumImage: String,
    /** 持续时间(毫秒) */
    val duration: Long
)
