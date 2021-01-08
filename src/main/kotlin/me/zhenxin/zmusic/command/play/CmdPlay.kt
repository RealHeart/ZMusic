package me.zhenxin.zmusic.command.play

import me.zhenxin.zmusic.type.ApiType

object CmdPlay {
    fun playSingle(key: String, apiType: ApiType) {
        val api = apiType.getApi()
    }
}
