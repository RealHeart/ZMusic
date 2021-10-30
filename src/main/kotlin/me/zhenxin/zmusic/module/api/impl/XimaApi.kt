package me.zhenxin.zmusic.module.api.impl

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.MusicInfo
import me.zhenxin.zmusic.module.api.PlaylistInfo
import me.zhenxin.zmusic.utils.HttpUtil
import java.net.URLEncoder

/**
 * 喜马拉雅实现
 *
 * @author 真心
 * @since 2021/9/8 10:32
 * @email qgzhenxin@qq.com
 */
@Suppress("SpellCheckingInspection")
class XimaApi : MusicApi {
    override val name: String = Lang.PLATFORM_XIMA

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        val musics = mutableListOf<MusicInfo>()
        val search =
            HttpUtil.get(
                "https://www.ximalaya.com/revision/search/main?kw=${
                    URLEncoder.encode(keyword, "UTF-8")
                }&core=track&page=$page&rows=$count"
            )
        val data = JSONObject(search.data)
        val result = data.getJSONObject("data")
        val track = result.getJSONObject("track")
        val songs = track.getJSONArray("docs")
        songs.forEach {
            it as JSONObject

            val id = it.getStr("id")
            val name = it.getStr("title")
            val albumName = it.getStr("albumTitle")
            val albumImage = it.getStr("albumCoverPath")
            val duration = it.getLong("duration") * 1000

            musics.add(
                MusicInfo(
                    id,
                    name,
                    this.name,
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
        val result =
            HttpUtil.get("https://mobile.ximalaya.com/mobile-playpage/playpage/tabs/$id/ts-${System.currentTimeMillis()}")
        val json = JSONObject(result.data)
        val data = json.getJSONObject("data")
        val page = data.getJSONObject("playpage")
        val track = page.getJSONObject("trackInfo")
        return track.getStr("playUrl64")
    }

    override fun getMusicInfo(id: String): MusicInfo {
        TODO("Not yet implemented")
    }
}