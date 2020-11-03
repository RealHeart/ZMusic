package me.zhenxin.zmusic.api.netease

import com.google.gson.Gson
import com.google.gson.JsonObject
import me.zhenxin.zmusic.api.Api
import me.zhenxin.zmusic.api.data.Json
import me.zhenxin.zmusic.api.data.Song
import me.zhenxin.zmusic.config.Config
import java.net.URLEncoder

class NeteaseApi : Api {
    override val api: String = Config.Api.netease

    override fun search(key: String, page: Int, count: Int): Json {
        val data = get("/search?keywords=${URLEncoder.encode(key, "UTF-8")}")
        val json = Gson().fromJson(data, JsonObject::class.java)
        println(json.toString())
        val result = Song("", "", "", "", 1, "")
        return Json(200, result)
    }

    override fun info(id: String): Json {
        TODO("Not yet implemented")
    }

    override fun url(id: String): Json {
        TODO("Not yet implemented")
    }

    override fun lyric(id: String): Json {
        TODO("Not yet implemented")
    }

}
