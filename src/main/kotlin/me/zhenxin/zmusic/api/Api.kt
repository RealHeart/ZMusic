package me.zhenxin.zmusic.api

import me.zhenxin.zmusic.api.data.Json
import me.zhenxin.zmusic.util.NetUtil

interface Api {
    val api: String

    fun get(url: String): String {
        return NetUtil.getAsString(api + url)
    }

    fun search(key: String, page: Int = 1, count: Int = 30): Json
    fun info(id: String): Json
    fun url(id: String): Json
    fun lyric(id: String): Json
}
