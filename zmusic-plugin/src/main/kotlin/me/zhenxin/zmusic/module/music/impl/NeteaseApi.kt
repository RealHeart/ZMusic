package me.zhenxin.zmusic.module.music.impl

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.parseObject
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.config.Lang
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
    private val api = Config.API_NETEASE_LINK
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
        val data = search.parseObject()
        val result = data.getJSONObject("result")
        val songs = result.getJSONArray("songs")
        songs.forEach {
            it as JSONObject
            val id = it.getString("id")
            musics.add(getMusicInfo(id))
        }
        return musics
    }

    override fun getPlaylist(id: String): PlaylistInfo {
        val musics = mutableListOf<MusicInfo>()
        val result = get("$api/playlist/detail?id=$id")
        val data = result.parseObject()
        val playlist = data.getJSONObject("playlist")
        val listId = playlist.getString("id")
        val listName = playlist.getString("name")
        val tracks = playlist.getJSONArray("tracks")
        tracks.forEach {
            it as JSONObject
            val songId = it.getString("id")
            musics.add(getMusicInfo(songId))
        }

        return PlaylistInfo(listId, listName, musics)
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String {
        val result = get("$api/song/url?id=$id&br=320000")
        val json = result.parseObject()
        val data = json.getJSONArray("data").getJSONObject(0)
        return data.getString("url")
    }

    override fun getLyric(id: String): MutableList<LyricRaw> {
        val result = get("$api/lyric?id=$id")
        val json = result.parseObject()
        val lrc = json.getJSONObject("lrc")
        val lyric = lrc.getString("lyric")
        val tlrc = json.getJSONObject("tlyric")
        val tlyric = if (tlrc != null) tlrc.getString("lyric") else ""
        return formatLyric(lyric, tlyric)
    }

    override fun getMusicInfo(id: String): MusicInfo {
        val result = get("$api/song/detail?ids=${id}")
        val info = result.parseObject()
        val song = info.getJSONArray("songs").getJSONObject(0)
        val name = song.getString("name")
        val singers = song.getJSONArray("ar")
        val singer = mergeSingers(singers)
        val album = song.getJSONObject("al")
        val albumName = album.getString("name")
        val albumImage = album.getString("picUrl")
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