package me.zhenxin.zmusic.music.impl

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.music.MusicApi
import me.zhenxin.zmusic.music.MusicInfo
import me.zhenxin.zmusic.music.PlaylistInfo
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.entity.LyricRaw
import me.zhenxin.zmusic.utils.get
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
            get(
                "https://api.bilibili.com/x/web-interface/search/type?search_type=video&page=$page&keyword=${
                    URLEncoder.encode(
                        keyword, "UTF-8"
                    )
                }"
            )
        val json = JSONObject(search)
        val data = json.getJSONObject("data")
        val result = data.getJSONArray("result")
        result.forEach {
            it as JSONObject
            val bvid = it.getStr("bvid")
            val title = it.getStr("title")
                .replace("</em>", "")
                .replace("<em class=\"keyword\">", "")
            val author = it.getStr("author")
            val pic = it.getStr("pic")
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
        val result = get(url)
        val json = JSONObject(result)
        val data = json.getJSONObject("data")
        val dash = data.getJSONObject("dash")
        val audios = dash.getJSONArray("audio")
        val audio = audios.getJSONObject(0)
        val mp3 = m4s2mp3(bvid, audio.getStr("baseUrl"))
        return mp3.ifEmpty { "" }
    }

    override fun getLyric(id: String): MutableList<LyricRaw> {
        return mutableListOf(LyricRaw(1, "", ""))
    }

    override fun getMusicInfo(id: String): MusicInfo {
        val url = "https://api.bilibili.com/x/web-interface/view?bvid=${id}"
        val result = get(url)
        val json = JSONObject(result)
        val data = json.getJSONObject("data")
        val fullId = "${data.getStr("bvid")},${data["cid"]}"
        val title = data.getStr("title")
        val singer = data.getJSONObject("owner").getStr("name")
        val pic = data.getStr("pic")
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