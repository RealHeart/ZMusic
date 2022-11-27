package me.zhenxin.zmusic.module.music.impl

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.entity.MusicInfo
import me.zhenxin.zmusic.entity.PlaylistInfo
import me.zhenxin.zmusic.module.music.MusicApi
import me.zhenxin.zmusic.utils.formatLyric
import me.zhenxin.zmusic.utils.get
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
    private val api = config.API_NETEASE
    override val name: String = Lang.PLATFORM_NETEASE

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        val musics = mutableListOf<MusicInfo>()
        val offset = (page - 1) * count
        val search =
            get(
                "$api/cloudsearch?keywords=${
                    URLEncoder.encode(keyword, "UTF-8")
                }&limit=$count&offset=$offset"
            )
        val data = JSONObject(search)
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
        val result = get("$api/playlist/detail?id=$id")
        val data = JSONObject(result)
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
        val result = get("$api/song/url?id=$id&br=320000")
        val json = JSONObject(result)
        val data = json.getJSONArray("data")[0] as JSONObject
        return data.getStr("url")
    }

    override fun getLyric(id: String): MutableList<LyricRaw> {
        val result = get("$api/lyric?id=$id")
        val json = JSONObject(result)
        val lrc = json.getJSONObject("lrc")
        val lyric = lrc.getStr("lyric")
        val tlrc = json.getJSONObject("tlyric")
        val tlyric = if (tlrc != null) tlrc.getStr("lyric") else ""
        return formatLyric(lyric, tlyric)
    }

    override fun getMusicInfo(id: String): MusicInfo {
        val result = get("$api/song/detail?ids=${id}")
        val info = JSONObject(result)
        val song = info.getJSONArray("songs").getJSONObject(0)
        val name = song.getStr("name")
        val singers = song.getJSONArray("ar")
        val singer = mergeSingers(singers)
        val album = song.getJSONObject("al")
        val albumName = album.getStr("name")
        val albumImage = album.getStr("picUrl")
        val duration = song.getLong("dt") / 1000

        return MusicInfo(
            id,
            name,
            singer,
            albumName,
            albumImage,
            duration.toInt()
        )
    }
}