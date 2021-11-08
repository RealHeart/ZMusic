package me.zhenxin.zmusic.module.api.impl

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.MusicInfo
import me.zhenxin.zmusic.module.api.PlaylistInfo
import me.zhenxin.zmusic.module.config
import me.zhenxin.zmusic.utils.httpGet
import java.net.URLEncoder

/**
 * 网易云音乐接口实现
 *
 * @author 真心
 * @since 2021/7/14 21:37
 * @email qgzhenxin@qq.com
 */
@Suppress("DuplicatedCode")
class NeteaseApi : MusicApi {
    private val api = config.API_NETEASE_LINK
    override val name: String = Lang.PLATFORM_NETEASE

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        val musics = mutableListOf<MusicInfo>()
        val offset = (page - 1) * count
        val search =
            httpGet(
                "$api/cloudsearch?keywords=${
                    URLEncoder.encode(keyword, "UTF-8")
                }&limit=$count&offset=$offset"
            )
        val data = JSONObject(search.data)
        val result = data.getJSONObject("result")
        val songs = result.getJSONArray("songs")
        songs.forEach {
            it as JSONObject
            val id = it.getStr("id")
            musics.add(getMusicInfo(id))
        }
        return musics
    }

    override fun getPlaylist(id: String): PlaylistInfo {
        val musics = mutableListOf<MusicInfo>()
        val info = httpGet("$api/playlist/detail?id=$id")
        val data = JSONObject(info.data)
        val playlist = data.getJSONObject("playlist")
        val listId = playlist.getStr("id")
        val listName = playlist.getStr("name")
        val tracks = playlist.getJSONArray("tracks")
        tracks.forEach {
            it as JSONObject
            val songId = it.getStr("id")
            musics.add(getMusicInfo(songId))
        }

        return PlaylistInfo(listId, listName, musics)
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String {
        val result = httpGet("$api/song/url?id=$id")
        val json = JSONObject(result.data)
        val data = json.getJSONArray("data")[0] as JSONObject
        return data.getStr("url")
    }

    override fun getMusicInfo(id: String): MusicInfo {
        val result = httpGet("$api/song/detail?ids=${id}")
        val info = JSONObject(result.data)
        val song = info.getJSONArray("songs")[0] as JSONObject
        val name = song.getStr("name")
        val singers = song.getJSONArray("ar")
        val singer = mergeSingers(singers)
        val album = song.getJSONObject("al")
        val albumName = album.getStr("name")
        val albumImage = album.getStr("picUrl")
        val duration = song.getLong("dt")

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