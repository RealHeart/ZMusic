package me.zhenxin.zmusic.api.impl;

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import me.zhenxin.zmusic.api.MusicApi
import me.zhenxin.zmusic.api.MusicInfo
import me.zhenxin.zmusic.api.PlaylistInfo
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.logger
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

    private val clientId = "cyfcuTc4OKTJ09j8IoqWUZQkZ7QFN3p8"
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
        logger.debug(result)
        val data = JSON.parseObject(result)
        val songs = data.getJSONArray("collection")
        songs.forEach {
            it as JSONObject
            val id = it.getString("permalink_url")
            val name = it.getString("title")
            val singer = it.getJSONObject("user").getString("username")
            val albumName = it.getString("tag_list")
            var albumImage = it.getString("artwork_url")
            val duration = it.getLong("duration")

            if (albumImage == null) albumImage = ""
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
        TODO("NoSupport")
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String): String {
        val result = get("$api/resolve?url=${URLEncoder.encode(id, "UTF-8")}&client_id=$clientId")
        val json = JSON.parseObject(result)
        val data = json.getJSONObject("media").getJSONArray("transcodings")
        val urlLink = (data[1] as JSONObject).getString("url")
        val urlResult = get("$urlLink?client_id=$clientId")
        return JSON.parseObject(urlResult).getString("url")
    }

    override fun getLyric(id: String): MutableList<LyricRaw> {
        TODO("Not yet implemented")
    }

    override fun getMusicInfo(id: String): MusicInfo {
        TODO("Not yet implemented")
    }


}