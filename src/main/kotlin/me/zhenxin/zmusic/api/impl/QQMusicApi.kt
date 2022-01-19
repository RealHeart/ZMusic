package me.zhenxin.zmusic.api.impl

import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.api.MusicApi
import me.zhenxin.zmusic.api.MusicInfo
import me.zhenxin.zmusic.api.PlaylistInfo
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.utils.httpGet
import java.net.URLEncoder

/**
 * QQ音乐接口实现
 *
 * @author 真心
 * @since 2021/8/30 13:26
 * @email qgzhenxin@qq.com
 */
@Suppress("DuplicatedCode")
class QQMusicApi : MusicApi {
    private val api = config.API_QQ_LINK
    override val name: String = Lang.PLATFORM_QQ

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        val musics = mutableListOf<MusicInfo>()
        val search =
            httpGet(
                "$api/search?key=${
                    URLEncoder.encode(keyword, "UTF-8")
                }&pageSize=$count&pageNo=$page"
            )
        val data = JSONObject.parseObject(search.data as String)
        val result = data.getJSONObject("data")
        val songs = result.getJSONArray("list")
        songs.forEach {
            it as JSONObject
            val id = it.getString("songmid")
            musics.add(getMusicInfo(id))
        }
        return musics
    }

    override fun getPlaylist(id: String): PlaylistInfo {
        val musics = mutableListOf<MusicInfo>()
        val search =
            httpGet("$api/songlist?id=$id")
        val data = JSONObject.parseObject(search.data as String)
        val result = data.getJSONObject("data")
        val listId = data.getString("disstid")
        val listName = data.getString("dissname")
        val songs = result.getJSONArray("songlist")
        songs.forEach {
            it as JSONObject
            val songId = it.getString("songmid")
            musics.add(getMusicInfo(songId))

        }
        return PlaylistInfo(listId, listName, musics)
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String {
        val result = httpGet("$api/song/url?id=$id")
        val json = JSONObject.parseObject(result.data as String)
        return json.getString("data")
    }

    override fun getMusicInfo(id: String): MusicInfo {
        val songInfo = httpGet("$api/song?songmid=${id}")
        val info = JSONObject.parseObject(songInfo.data as String)
        val track = info.getJSONObject("data").getJSONObject("track_info")
        val name = track.getString("name")
        val singers = track.getJSONArray("singer")
        val singer = mergeSingers(singers)
        val album = track.getJSONObject("album")
        val albumName = album.getString("name")
        val albumImage = "https://y.qq.com/music/photo_new/T002R300x300M000${album.getString("pmid")}.jpg"
        val duration = track.getLong("interval") * 1000


        return MusicInfo(
            id,
            name,
            singer,
            albumName,
            albumImage,
            duration
        )
    }
}