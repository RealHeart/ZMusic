package me.zhenxin.zmusic.command.admin

import me.zhenxin.zmusic.module.Sender
import me.zhenxin.zmusic.type.ApiType

object CmdAdmin {
    fun playAll(key: String, apiType: ApiType, sender: Sender) {
        println("$key $apiType $sender")
    }
}
