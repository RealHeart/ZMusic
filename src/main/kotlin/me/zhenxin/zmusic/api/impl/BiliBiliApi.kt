package me.zhenxin.zmusic.api.impl

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.api.MusicApi
import me.zhenxin.zmusic.api.MusicInfo
import me.zhenxin.zmusic.api.PlaylistInfo
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.utils.httpGet
import me.zhenxin.zmusic.utils.m4s2mp3
import java.net.URLEncoder

/**
 * 哔哩哔哩实现
 *
 * @author 真心
 * @since 2021/9/8 10:31
 * @email qgzhenxin@qq.com
 */
class BiliBiliApi : MusicApi {
    override val name: String = Lang.PLATFORM_BILI

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        val musics = mutableListOf<MusicInfo>()
        val search =
            httpGet(
                "https://api.bilibili.com/x/web-interface/search/type?search_type=video&page=$page&keyword=${
                    URLEncoder.encode(
                        keyword, "UTF-8"
                    )
                }"
            )
        val json = JSON.parseObject(search.data)
        val data = json.getJSONObject("data")
        val result = data.getJSONArray("result")
        result.forEach {
            it as JSONObject
            val bvid = it.getString("bvid")
            val title = it.getString("title")
                .replace("</em>", "")
                .replace("<em class=\"keyword\">", "")
            val author = it.getString("author")
            val pic = it.getString("pic")
            musics.add(
                MusicInfo(
                    bvid,
                    title,
                    author,
                    title,
                    pic,
                    0
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
        val ids = id.split(",")
        val bvid = ids[0]
        val cid = ids[1]
        val url = "https://api.bilibili.com/x/player/playurl?bvid=$bvid&cid=$cid&fnval=16"
        val result = httpGet(url)
        val json = JSON.parseObject(result.data)
        val data = json.getJSONObject("data")
        val dash = data.getJSONObject("dash")
        val audios = dash.getJSONArray("audio")
        val audio = audios.getJSONObject(0)
        val mp3 = m4s2mp3(bvid, audio.getString("baseUrl"))
        return if (mp3.isNullOrEmpty()) "" else mp3
    }

    override fun getMusicInfo(id: String): MusicInfo {
        val url = "https://api.bilibili.com/x/web-interface/view?bvid=${id}"
        val result = httpGet(url)
        val json = JSON.parseObject(result.data)
        val data = json.getJSONObject("data")
        val fullId = "${data.getString("bvid")},${data.get("cid")}"
        val title = data.getString("title")
        val singer = data.getJSONObject("owner").getString("name")
        val pic = data.getString("pic")
        val duration = data.getLong("duration") * 1000
        return MusicInfo(
            fullId,
            title,
            singer,
            title,
            pic,
            duration
        )
    }
}