package me.zhenxin.zmusic.command.play

import me.zhenxin.zmusic.type.Platform

object CmdPlay {
    fun playSingle(key: String, platform: Platform) {
        val api = platform.getApi()
    }
}
