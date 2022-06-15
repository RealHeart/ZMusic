package me.zhenxin.zmusic.api.impl

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import me.zhenxin.zmusic.api.MusicApi
import me.zhenxin.zmusic.api.MusicInfo
import me.zhenxin.zmusic.api.PlaylistInfo
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.entity.LyricRaw
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
    private val api = config.API_NETEASE_LINK
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
        val data = JSON.parseObject(search)
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
        val info = get("$api/playlist/detail?id=$id")
        val data = JSON.parseObject(info)
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
        val result = get("$api/song/url?id=$id")
        val json = JSON.parseObject(result)
        val data = json.getJSONArray("data")[0] as JSONObject
        return data.getString("url")
    }

    override fun getLyric(id: String): MutableList<LyricRaw> {
        val result = get("$api/lyric?id=$id")
        val json = JSON.parseObject(result)
        val lrc = json.getJSONObject("lrc")
        val lyric = lrc.getString("lyric")
        val tlrc = json.getJSONObject("tlyric")
        val tlyric = tlrc.getString("lyric")
        return formatLyric(lyric, tlyric)
    }

    override fun getMusicInfo(id: String): MusicInfo {
        val result = get("$api/song/detail?ids=${id}")
        val info = JSON.parseObject(result)
        val song = info.getJSONArray("songs")[0] as JSONObject
        val name = song.getString("name")
        val singers = song.getJSONArray("ar")
        val singer = mergeSingers(singers)
        val album = song.getJSONObject("al")
        val albumName = album.getString("name")
        val albumImage = album.getString("picUrl")
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