@file:JvmName("ZMusic.API")

package me.zhenxin.zmusic.module

import com.google.gson.JsonObject
import me.zhenxin.zmusic.util.ext.URLExt.readText
import java.net.URL

internal interface Api {
    val api: String

    fun get(url: String): String = URL(api + url).readText()

    fun post(url: String, data: String = ""): String = URL(api + url).readText(data)

    fun search(key: String, page: Int = 1, count: Int = 30): JsonObject
    fun info(id: String): JsonObject
    fun url(id: String): JsonObject
    fun lyric(id: String): JsonObject

    fun formatLyric(lyric: String, lyricTr: String): JsonObject {
        val json = JsonObject()
        val lrcMap = formatLyric(lyric)
        val lrcTrMap = formatLyric(lyricTr)
        lrcMap.forEach { (key, value) ->
            var lrcTr = ""
            if (lrcTrMap[key] != null) {
                lrcTr = lrcTrMap[key].toString()
            }
            val j = JsonObject()
            j.addProperty("lrc", value)
            j.addProperty("lrcTr", lrcTr)
            json.add(key.toString(), j)
        }
        return json
    }

    private fun formatLyric(lyric: String): MutableMap<Int, String> {
        val map = mutableMapOf<Int, String>()
        if (lyric.isEmpty()) {
            return map
        }
        val lyrics = lyric.split("\n")
        val regex = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})]".toRegex()
        lyrics.forEach {
            val ms = regex.findAll(it)
            val text: String = it.replace(regex, "")
            ms.forEach { mr ->
                val min: String = mr.groupValues[1]
                val sec: String = mr.groupValues[2]
                var mill: String = mr.groupValues[3]
                if (mill.length == 3) mill = mill.substring(0, mill.length - 1)
                val time = min.toInt() * 60 * 1000 + sec.toInt() * 1000 + mill.toInt()
                map[time] = text
            }
        }
        return map
    }
}
