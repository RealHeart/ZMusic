package me.zhenxin.zmusic.api.data


/***
 * 歌曲信息数据类
 * @param id 歌曲ID
 * @param name 歌名
 * @param singer 歌手
 * @param album 专辑
 * @param time 持续时间
 * @param url 播放链接
 */
data class Song(
    val id: String,
    val name: String,
    val singer: String,
    val album: String,
    val time: Int,
    val url: String,
)
