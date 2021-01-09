package me.zhenxin.zmusic.module.api

import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.module.Api

class KugouApi : Api {
    override val api: String
        get() = TODO("Not yet implemented")

    override fun search(key: String, page: Int, count: Int): JSONObject {
        TODO("Not yet implemented")
    }

    override fun info(id: String): JSONObject {
        TODO("Not yet implemented")
    }

    override fun url(id: String): JSONObject {
        TODO("Not yet implemented")
    }

    override fun lyric(id: String): JSONObject {
        TODO("Not yet implemented")
    }
}
