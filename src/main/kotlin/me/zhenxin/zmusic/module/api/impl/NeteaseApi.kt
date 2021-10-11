package me.zhenxin.zmusic.module.api.impl

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.MusicInfo
import me.zhenxin.zmusic.module.api.PlaylistInfo
import me.zhenxin.zmusic.module.config
import me.zhenxin.zmusic.utils.HttpUtil
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
            HttpUtil.get(
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
            val name = it.getStr("name")
            val singers = it.getJSONArray("ar")
            val singer = mergeSingers(singers)
            val album = it.getJSONObject("al")
            val albumName = album.getStr("name")
            val albumImage = album.getStr("picUrl")
            val duration = it.getLong("dt")

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
        val info = HttpUtil.get("$api/playlist/detail?id=$id")
        val data = JSONObject(info.data)
        val playlist = data.getJSONObject("playlist")
        val listId = playlist.getStr("id")
        val listName = playlist.getStr("name")
        val tracks = playlist.getJSONArray("tracks")
        tracks.forEach {
            it as JSONObject
            val name = it.getStr("name")
            val songId = it.getStr("id")
            val singers = it.getJSONArray("ar")
            val singer = mergeSingers(singers)
            val album = it.getJSONObject("al")
            val albumName = album.getStr("name")
            val albumImage = album.getStr("picUrl")
            val duration = it.getLong("dt")

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
        val data = json.getJSONArray("data")[0] as JSONObject
        return data.getStr("url")
    }
}