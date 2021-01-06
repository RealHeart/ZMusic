package me.zhenxin.zmusic.module.api

import com.alibaba.fastjson.JSONObject
import com.google.gson.JsonObject
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Api

class XimaApi : Api {
    override val api = Config.Api.xima
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
