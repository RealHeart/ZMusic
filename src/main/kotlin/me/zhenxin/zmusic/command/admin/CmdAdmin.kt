package me.zhenxin.zmusic.command.admin

import me.zhenxin.zmusic.module.Sender
import me.zhenxin.zmusic.type.Platform

object CmdAdmin {
    fun playAll(key: String, platform: Platform, sender: Sender) {
        println("$key $platform $sender")
    }
}
