package me.zhenxin.zmusic.module.api.impl

import cn.hutool.core.util.StrUtil
import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.module.Config
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.MusicInfo
import me.zhenxin.zmusic.utils.HttpUtil
import java.net.URLEncoder

/**
 * QQ音乐接口实现
 *
 * @author 真心
 * @since 2021/8/30 13:26
 * @email qgzhenxin@qq.com
 */
class QQMusicApi : MusicApi {
    private val api = Config.API_QQ_LINK
    override val name: String = Lang.PLATFORM_QQ

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        val musics = mutableListOf<MusicInfo>()
        val search =
            HttpUtil.get(
                "$api/search?key=${
                    URLEncoder.encode(keyword, "UTF-8")
                }&pageSize=$count&pageNo=$page"
            )
        val data = JSONObject(search.data)
        val result = data.getJSONObject("data")
        val songs = result.getJSONArray("list")
        songs.forEach {
            it as JSONObject
            val id = it.getStr("songmid")
            val songInfo = HttpUtil.get("$api/song?songmid=${id}")
            val info = JSONObject(songInfo.data)
            val track = info.getJSONObject("data").getJSONObject("track_info")
            val name = track.getStr("name")
            val singer = track.getJSONArray("singer")
            var singers = ""
            singer.forEach { ar ->
                ar as JSONObject
                singers = "$singers${ar.getStr("name")}/"
            }
            singers = StrUtil.removeSuffix(singers, "/")
            val album = track.getJSONObject("album")
            val albumName = album.getStr("name")
            val albumImage = "https://y.qq.com/music/photo_new/T002R300x300M000${album.getStr("pmid")}.jpg"
            val duration = track.getLong("interval") * 1000


            musics.add(
                MusicInfo(
                    id,
                    name,
                    singers,
                    albumName,
                    albumImage,
                    duration
                )
            )
        }
        return musics
    }

    override fun getPlaylist(id: String) {
        TODO("Not yet implemented")
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String {
        val result = HttpUtil.get("$api/song/url?id=$id")
        val json = JSONObject(result.data)
        return json.getStr("data")
    }
}