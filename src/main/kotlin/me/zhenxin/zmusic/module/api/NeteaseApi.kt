package me.zhenxin.zmusic.module.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Api
import java.net.URLEncoder

internal class NeteaseApi : Api {
    override val api: String = Config.Api.netease
    private val gson = GsonBuilder().create()
    override fun search(key: String, page: Int, count: Int): JsonObject {
        val json = JsonObject()
        val array = JsonArray()
        val data = gson.fromJson(get("/search?keywords=${URLEncoder.encode(key, "UTF-8")}"), JsonObject::class.java)
        val result = data["result"] as JsonObject
        val songs = result["songs"] as JsonArray
        songs.forEach { song ->
            song as JsonObject
            val id = song["id"].asString
            val name = song["name"].asString
            val singers = song["artists"].asJsonArray
            var singer = ""
            singers.forEach {
                it as JsonObject
                singer += it["name"].asString + "/"
            }
            singer = singer.substring(0, singer.length - 1)
            val time = song.get("duration").asLong
            val url = gson.fromJson(get("/song/url?id=$id"), JsonObject::class.java)
                .get("data").asJsonArray[0].asJsonObject
                .get("url").asString
            val resultJson = JsonObject()
            resultJson.addProperty("id", id)
            resultJson.addProperty("name", name)
            resultJson.addProperty("singer", singer)
            resultJson.addProperty("time", time)
            resultJson.addProperty("url", url)
            array.add(resultJson)
        }
        json.addProperty("code", 200)
        json.add("data", array)
        return json
    }

    override fun info(id: String): JsonObject {
        TODO("Not yet implemented")
    }

    override fun url(id: String): JsonObject {
        TODO("Not yet implemented")
    }

    override fun lyric(id: String): JsonObject {
        val data = gson.fromJson(get("/lyric?id=$id"), JsonObject::class.java)
        val lyric: JsonObject? = data["lrc"]?.asJsonObject
        val lyricTr: JsonObject? = data["tlyric"]?.asJsonObject
        val lrc: String = lyric?.get("lyric")?.asString.toString()
        val lrcTr: String = lyricTr?.get("lyric")?.asString.toString()
        return formatLyric(lrc, lrcTr)
    }

}
