package me.zhenxin.zmusic.module

import me.zhenxin.zmusic.config.Lang
import net.md_5.bungee.api.chat.TextComponent

interface Player {
    fun sendMsg(msg: String, player: Any)

    fun sendErrMsg(msg: String, player: Any)

    fun sendJsonMsg(msg: TextComponent, player: Any)

    fun sendActionBar(msg: TextComponent, player: Any)

    fun sendTitle(title: String, subTitle: String, player: Any)

    fun sendNull(player: Any) {
        sendMsg(Lang.Help.tip, player)
    }

    fun sendPlayErr(player: Any, musicName: String) {
        for (s in Lang.Play.error) {
            sendErrMsg(s.replace("%musicName%", musicName), player)
        }
    }
}
