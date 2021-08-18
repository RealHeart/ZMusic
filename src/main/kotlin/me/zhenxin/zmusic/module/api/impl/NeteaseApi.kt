package me.zhenxin.zmusic.module.api.impl

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.module.Config
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.data.MusicInfo
import me.zhenxin.zmusic.utils.HttpUtil
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * 网易云音乐接口实现
 *
 * @author 真心
 * @since 2021/7/14 21:37
 * @email qgzhenxin@qq.com
 */
class NeteaseApi : MusicApi {
    private val api = Config.API_NETEASE_LINK
    override fun searchSingle(keyword: String): MusicInfo {
        val data = searchPage(keyword, 1, 1)
        return data[0]
    }

    override fun searchPage(keyword: String, page: Int, count: Int): MutableList<MusicInfo> {
        val musics = mutableListOf<MusicInfo>()
        val offset = (page - 1) * count
        val search =
            HttpUtil.get(
                "$api/cloudsearch?keywords=${
                    URLEncoder.encode(keyword, StandardCharsets.UTF_8)
                }&limit=$count&offset=$offset"
            )
        val data = JSON.parseObject(search.data as String)
        val result = data.getJSONObject("result")
        val songs = result.getJSONArray("songs")
        songs.forEach {
            it as JSONObject
            val id = it.getString("id")
            val name = it.getString("name")
            val singer = it.getJSONArray("ar")
            var singers = ""
            singer.forEach { ar ->
                ar as JSONObject
                singers = "$singers${ar.getString("name")}/"
            }
            singers = singers.trimEnd('/')
            val album = it.getJSONObject("al")
            val albumName = album.getString("name")
            val albumImage = album.getString("picUrl")
            val duration = it.getLongValue("dt")

            musics.add(
                MusicInfo(
                    id,
                    name,
                    singers,
                    albumName,
                    albumImage,
                    duration
                )
            )
        }
        return musics
    }

    override fun getPlaylist(id: String) {
        TODO("Not yet implemented")
    }

    override fun getAlbum(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPlayUrl(id: String) {
        TODO("Not yet implemented")
    }
}