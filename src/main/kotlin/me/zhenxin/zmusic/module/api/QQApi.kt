package me.zhenxin.zmusic.module.api

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Api
import java.net.URLEncoder

class QQApi : Api {
    override val api = Config.Api.qq
    override fun search(key: String, page: Int, count: Int): JSONObject {
        val data = JSON.parseObject(get("/search?key=${URLEncoder.encode(key, "UTF-8")}&pageNo=$page&pageSize=$count"))
        return data
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
