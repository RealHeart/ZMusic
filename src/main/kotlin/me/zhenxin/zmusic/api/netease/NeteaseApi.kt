package me.zhenxin.zmusic.api.netease

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.api.Api
import me.zhenxin.zmusic.config.Config
import java.net.URLEncoder

class NeteaseApi : Api {
    override val api: String = Config.Api.netease

    override fun search(key: String, page: Int, count: Int): JSONObject {
        val json = JSONObject()
        val array = JSONArray()
        val data = JSON.parseObject(get("/search?keywords=${URLEncoder.encode(key, "UTF-8")}"))
        val result = data["result"] as JSONObject
        val songs = result["songs"] as JSONArray
        songs.forEach { song ->
            song as JSONObject
            val id = song.getString("id")
            val name = song.getString("name")
            val singers = song.getJSONArray("artists")
            var singer = ""
            singers.forEach {
                it as JSONObject
                singer += it["name"] as String + "/"
            }
            singer = singer.substring(0, singer.length - 1)
            val time = song.getLong("duration")
            val url = JSON.parseObject(get("/song/url?id=$id")).getJSONArray("data").getJSONObject(0).getString("url")
            val resultJSON = JSONObject()
            resultJSON["id"] = id
            resultJSON["name"] = name
            resultJSON["singer"] = singer
            resultJSON["time"] = time
            resultJSON["url"] = url
            array.add(resultJSON)
        }
        json["code"] = 200
        json["data"] = array
        return json
    }

    override fun info(id: String): JSONObject {
        TODO("Not yet implemented")
    }

    override fun url(id: String): JSONObject {
        TODO("Not yet implemented")
    }

    override fun lyric(id: String): JSONObject {
        val data = JSON.parseObject(get("/lyric?id=$id"))
        val lyric: JSONObject? = data.getJSONObject("lrc")
        val lyricTr: JSONObject? = data.getJSONObject("tlyric")
        val lrc: String = lyric?.getString("lyric").toString()
        val lrcTr: String = lyricTr?.getString("lyric").toString()
        return formatLyric(lrc, lrcTr)
    }

}
