package me.zhenxin.zmusic.api.qq

import me.zhenxin.zmusic.api.Api
import me.zhenxin.zmusic.api.data.Json
import me.zhenxin.zmusic.config.Config

class QQApi : Api {
    override val api = Config.Api.qq
    override fun search(key: String, page: Int, count: Int): Json {
        TODO("Not yet implemented")
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
