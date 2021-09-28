package me.zhenxin.zmusic.module.api.impl

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.module.Config
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.MusicInfo
import me.zhenxin.zmusic.module.api.PlaylistInfo
import me.zhenxin.zmusic.utils.HttpUtil
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
            val singers = track.getJSONArray("singer")
            val singer = mergeSingers(singers)
            val album = track.getJSONObject("album")
            val albumName = album.getStr("name")
            val albumImage = "https://y.qq.com/music/photo_new/T002R300x300M000${album.getStr("pmid")}.jpg"
            val duration = track.getLong("interval") * 1000


            musics.add(
                MusicInfo(
                    id,
                    name,
                    singer,
                    albumName,
                    albumImage,
                    duration
                )
            )
        }
        return musics
    }

    override fun getPlaylist(id: String): PlaylistInfo {
        val musics = mutableListOf<MusicInfo>()
        val search =
            HttpUtil.get("$api/songlist?id=$id")
        val data = JSONObject(search.data)
        val result = data.getJSONObject("data")
        val listId = data.getStr("disstid")
        val listName = data.getStr("dissname")
        val songs = result.getJSONArray("songlist")
        songs.forEach {
            it as JSONObject
            val songId = it.getStr("songmid")
            val songInfo = HttpUtil.get("$api/song?songmid=${songId}")
            val info = JSONObject(songInfo.data)
            val track = info.getJSONObject("data").getJSONObject("track_info")
            val name = track.getStr("name")
            val singers = track.getJSONArray("singer")
            val singer = mergeSingers(singers)
            val album = track.getJSONObject("album")
            val albumName = album.getStr("name")
            val albumImage = "https://y.qq.com/music/photo_new/T002R300x300M000${album.getStr("pmid")}.jpg"
            val duration = track.getLong("interval") * 1000


            musics.add(
                MusicInfo(
                    songId,
                    name,
                    singer,
                    albumName,
                    albumImage,
                    duration
                )
            )
        }
        return PlaylistInfo(listId, listName, musics)
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