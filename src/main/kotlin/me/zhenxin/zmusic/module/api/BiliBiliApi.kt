package me.zhenxin.zmusic.module.api

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Api
import me.zhenxin.zmusic.util.ext.URLExt.readText
import java.net.URL
import java.net.URLEncoder

class BiliBiliApi : Api {
    override var api: String = ""

    override fun search(key: String, page: Int, count: Int): JSONObject {
        val json = JSONObject()
        val array = JSONArray()
        val data = JSON.parseObject(
            get(
                "https://api.bilibili.com/audio/music-service-c/s?keyword=${
                    URLEncoder.encode(
                        key,
                        "UTF-8"
                    )
                }&pagesize=${count}"
            )
        )
        val result = data.getJSONObject("data")
        val songs = result.getJSONArray("result")
        songs.forEach {
            it as JSONObject
            val obj = info(it.getString("id"))
            array.add(obj)
        }
        json["code"] = 200
        json["data"] = array
        return json
    }

    override fun info(id: String): JSONObject {
        val json = JSONObject()
        val data = JSON.parseObject(get("https://www.bilibili.com//audio/music-service-c/web/song/info?sid=$id", true))
        val result = data.getJSONObject("data")
        json["id"] = id
        json["name"] = result.getString("title")
        json["time"] = result.getInteger("duration")
        json["singer"] = result.getString("author")
        return json
    }

    override fun url(id: String): JSONObject {
        val json = JSONObject()
        val data = URL("https://m.bilibili.com/audio/au$id").readText(
            header = mutableMapOf(
                "User-Agent" to "Mozilla/5.0 (Linux; Android 11; Mi 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.99 Mobile Safari/537.36 ZMusic/" + ZMusic.thisVer
            ),
        )
        val biliAccount = Config.Account.BiliBili
        var url: String = data
            .split("<audio preload=\"auto\" src=\"")[1]
            .split("\"")[0]
        url = url.replace("&amp;", "&")
        val mp3Url = post(
            "https://api.zhenxin.xyz/minecraft/plugins/ZMusic/bilibili/getMp3.php",
            "qq=${biliAccount.qq}&key=${biliAccount.key}&id=$id&url=${URLEncoder.encode(url, "UTF-8")}"
        )
        json["code"] = 200
        val obj = JSONObject()
        obj["url"] = url
        json["data"] = obj
        return json
    }

    override fun lyric(id: String): JSONObject {
        TODO("Not yet implemented")
    }
}
