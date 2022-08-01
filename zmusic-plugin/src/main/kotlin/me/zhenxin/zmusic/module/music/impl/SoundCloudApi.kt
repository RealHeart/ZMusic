package me.zhenxin.zmusic.module.music.impl;

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.entity.MusicInfo
import me.zhenxin.zmusic.entity.PlaylistInfo
import me.zhenxin.zmusic.module.music.MusicApi
import me.zhenxin.zmusic.utils.get
import java.net.URLEncoder

/**
 * SoundCloud实现
 *
 * @author 舞晗坤
 * @since 2021/9/8 10:32
 * @email whksoft@gmail.com
 */
@Suppress("SpellCheckingInspection")
class SoundCloudApi : MusicApi {
    override val name: String = "SoundCloud"

    private val clientId = "oAK4FVApfyPTcO3MHrfDGH5VX5x2cBGF"
    private val api = "https://api-v2.soundcloud.com"

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        val musics = mutableListOf<MusicInfo>()
        val offset = (page - 1) * count
        val result = get(
            "$api/search/tracks?q=${
                URLEncoder.encode(
                    keyword,
                    "UTF-8"
                )
            }&limit=$count&offset=$offset&client_id=$clientId"
        )
        val data = JSONObject(result)
        val songs = data.getJSONArray("collection")
        songs.forEach {
            it as JSONObject
            val id = it.getStr("permalink_url")
            val name = it.getStr("title")
            val singer = it.getJSONObject("user").getStr("username")
            val albumName = it.getStr("tag_list")
            var albumImage = it.getStr("artwork_url")
            val duration = it.getLong("duration") / 1000

            if (albumImage == null) albumImage = ""
            musics.add(
                MusicInfo(
                    id,
                    name,
                    singer,
                    albumName,
                    albumImage,
                    duration.toInt()
                )
            )
        }
        return musics
    }

    override fun getPlaylist(id: String): PlaylistInfo {
        TODO("NoSupport")
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String {
        val result = get("$api/resolve?url=${URLEncoder.encode(id, "UTF-8")}&client_id=$clientId")
        val json = JSONObject(result)
        val data = json.getJSONObject("media").getJSONArray("transcodings")
        val urlLink = (data[1] as JSONObject).getStr("url")
        val urlResult = get("$urlLink?client_id=$clientId")
        return JSONObject(urlResult).getStr("url")
    }

    override fun getLyric(id: String): MutableList<LyricRaw> {
        return mutableListOf(LyricRaw(1, "", ""))
    }

    override fun getMusicInfo(id: String): MusicInfo {
        TODO("Not yet implemented")
    }


}