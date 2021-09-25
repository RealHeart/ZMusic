package me.zhenxin.zmusic.module.api.impl

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.MusicInfo
import me.zhenxin.zmusic.module.api.PlaylistInfo
import me.zhenxin.zmusic.utils.HttpUtil
import java.net.URLEncoder

/**
 * 酷狗音乐实现
 *·
 * @author 真心
 * @since 2021/9/8 10:28
 * @email qgzhenxin@qq.com
 */
class KuGouApi : MusicApi {
    override val name: String = Lang.PLATFORM_KUGOU

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        val musics = mutableListOf<MusicInfo>()
        val search = HttpUtil.get(
            "https://songsearch.kugou.com/song_search_v2?keyword=${
                URLEncoder.encode(keyword, "UTF-8")
            }&platform=WebFilter&format=json&page=$page&pagesize=$count"
        )
        val data = JSONObject(search.data)
        val result = data.getJSONObject("data")
        val songs = result.getJSONArray("lists")
        songs.forEach {
            it as JSONObject

            val id = it.getStr("FileHash")
            val name = it.getStr("SongName")
            val singers = it.getJSONArray("Singers")
            val singer = mergeSingers(singers)
            val albumName = it.getStr("AlbumName")
            val infoResult = HttpUtil.get("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=$id")
            val info = JSONObject(infoResult.data)
            val albumImage = info.getStr("album_img")
                .replace("/{size}/", "/")
            val duration = it.getLong("Duration") * 1000

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
        TODO("Not yet implemented")
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String {
        val infoResult = HttpUtil.get("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=$id")
        val info = JSONObject(infoResult.data)
        return info.getStr("url")
    }
}