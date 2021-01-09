package me.zhenxin.zmusic.module.api

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Api
import java.net.URLEncoder

class NeteaseApi : Api {
    override val api: String = Config.Api.netease
    override fun search(key: String, page: Int, count: Int): JSONObject {
        val json = JSONObject()
        val array = JSONArray()
        val data = JSON.parseObject(get("/search?keywords=${URLEncoder.encode(key, "UTF-8")}&limit=$count"))
        val result = data.getJSONObject("result")
        val songs = result.getJSONArray("songs")
        songs.forEach { song ->
            song as JSONObject
            val id = song.getString("id")
            val name = song.getString("name")
            val singers = song.getJSONArray("artists")
            var singer = ""
            singers.forEach {
                it as JSONObject
                singer += it.getString("name") + "/"
            }
            singer = singer.substring(0, singer.length - 1)
            val time = song.getInteger("duration")

            val resultJson = JSONObject()
            resultJson["id"] = id
            resultJson["name"] = name
            resultJson["singer"] = singer
            resultJson["time"] = time
            array.add(resultJson)
        }
        json["code"] = 200
        json["data"] = array
        return json
    }

    override fun info(id: String): JSONObject {
        TODO("Not yet implemented")
    }

    override fun url(id: String): JSONObject {
        val result = JSONObject()
        val json = JSONObject()
        val data = JSON.parseObject(get("/song/url?id=$id&&br=320000"))
        result["code"] = 200
        json["id"] = id
        val info = data.getJSONArray("data").getJSONObject(0)
        json["type"] = info.getString("type")
        json["url"] = info.getString("url")
        json["br"] = info.getString("br")
        result["data"] = json
        return result
    }

    override fun lyric(id: String): JSONObject {
        val data = JSON.parseObject(get("/lyric?id=$id"))
        val lyric = data.getJSONObject("lrc")
        val lyricTr = data.getJSONObject("tlyric")
        val lrc: String = lyric.getString("lyric")
        val lrcTr: String = lyricTr.getString("lyric")
        return formatLyric(lrc, lrcTr)
    }

}
