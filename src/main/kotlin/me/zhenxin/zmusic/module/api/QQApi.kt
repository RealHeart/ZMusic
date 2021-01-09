package me.zhenxin.zmusic.module.api

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Api
import java.net.URLEncoder

class QQApi : Api {
    override val api = Config.Api.qq
    override fun search(key: String, page: Int, count: Int): JSONObject {
        val json = JSONObject()
        val array = JSONArray()
        val data = JSON.parseObject(get("/search?key=${URLEncoder.encode(key, "UTF-8")}&pageNo=$page&pageSize=$count"))
        val result = data.getJSONObject("data")
        val list = result.getJSONArray("list")
        list.forEach {
            it as JSONObject
            val obj = info(it.getString("songmid"))
            array.add(obj)
        }
        json["code"] = 200
        json["data"] = array
        return json
    }

    override fun info(id: String): JSONObject {
        val json = JSONObject()
        val data = JSON.parseObject(get("/song?songmid=$id"))
        val result = data.getJSONObject("data")
        val info = result.getJSONObject("track_info")
        json["id"] = info.getString("mid")
        json["name"] = info.getString("name")
        val singers = info.getJSONArray("singer")
        var singer = ""
        singers.forEach {
            it as JSONObject
            singer += it.getString("name") + "/"
        }
        singer = singer.substring(0, singer.length - 1)
        json["singer"] = singer
        json["time"] = info.getString("interval")
        return json
    }

    override fun url(id: String): JSONObject {
        val json = JSONObject()
        val data = JSON.parseObject(get("/song/url?id=$id"))
        json["code"] = 200
        val obj = JSONObject()
        obj["url"] = data.getString("data")
        json["data"] = obj
        return json
    }

    override fun lyric(id: String): JSONObject {
        TODO("Not yet implemented")
    }
}
