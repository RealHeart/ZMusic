package me.zhenxin.zmusic.api

import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.util.OtherUtil
import me.zhenxin.zmusic.util.ext.URLExt.getString
import me.zhenxin.zmusic.util.ext.URLExt.postString
import java.net.URL


interface Api {
    val api: String

    fun get(url: String): String = URL(api + url).getString()

    fun post(url: String, data: String = ""): String = URL(api + url).postString(data = data)

    fun search(key: String, page: Int = 1, count: Int = 30): JSONObject
    fun info(id: String): JSONObject
    fun url(id: String): JSONObject
    fun lyric(id: String): JSONObject

    fun formatLyric(lyric: String, lyricTr: String): JSONObject {
        val json = JSONObject()
        val lrcMap = formatLyric(lyric)
        val lrcTrMap = formatLyric(lyricTr)

        lrcMap.forEach { (key, value) ->
            var lrcTr = ""
            if (lrcTrMap[key] != null) {
                lrcTr = lrcTrMap[key].toString()
            }
            val j = JSONObject()
            j["lrc"] = value
            j["lrcTr"] = lrcTr
            json[key] = j
        }
        return json
    }

    private fun formatLyric(lyric: String): JSONObject {
        val json = JSONObject()
        if (lyric.isEmpty()) {
            return json
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
                if (mill.length > 2) {
                    when (mill.length) {
                        2 -> {
                            mill = mill.toInt().toString()
                            mill = mill.substring(0, mill.length - 1)
                            mill.toInt().toString()
                        }
                        3 -> {
                            mill = mill.substring(0, mill.length - 1)
                            mill.toInt().toString()
                        }
                    }
                }

                val time = OtherUtil.timeToSec(min.toInt(), sec.toInt(), mill.toInt())
                json["$time"] = text
            }
        }
        return json
    }
}
