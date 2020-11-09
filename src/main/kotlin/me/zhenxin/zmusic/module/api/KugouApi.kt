package me.zhenxin.zmusic.module.api

import com.google.gson.JsonObject
import me.zhenxin.zmusic.module.Api

class KugouApi : Api {
    override val api: String
        get() = TODO("Not yet implemented")

    override fun search(key: String, page: Int, count: Int): JsonObject {
        TODO("Not yet implemented")
    }

    override fun info(id: String): JsonObject {
        TODO("Not yet implemented")
    }

    override fun url(id: String): JsonObject {
        TODO("Not yet implemented")
    }

    override fun lyric(id: String): JsonObject {
        TODO("Not yet implemented")
    }
}
