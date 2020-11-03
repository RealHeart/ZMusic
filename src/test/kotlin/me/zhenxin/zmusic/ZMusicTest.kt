package me.zhenxin.zmusic

import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.api.netease.NeteaseApi
import org.junit.Test


class ZMusicTest {

    @Test
    fun test() {
        val api = NeteaseApi()
        val data = api.search("初音未来的消失").getJSONArray("data")
        data.forEach {
            it as JSONObject
            println("==============")
            println(it["id"])
            println(it["name"])
            println(it["singer"])
            println(it["time"])
            println(it["url"])
        }
    }
}
