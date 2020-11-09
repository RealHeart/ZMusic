package me.zhenxin.zmusic.module

import com.google.gson.JsonObject
import me.zhenxin.zmusic.config.Lang
import net.md_5.bungee.api.chat.TextComponent

interface Sender {

    fun sendPMsgToABF(data: String)

    fun sendPMsgToAM(data: String) {
        sendPMsg(data, "allmusic:channel")
    }

    fun sendPMsg(data: String, channel: String)

    fun sendPMsgToAM(infoX: Int, infoY: Int, lyricX: Int, lyricY: Int) {
        val data = JsonObject()
        data.addProperty("EnableLyric", true)
        data.addProperty("EnableInfo", true)
        val info = JsonObject()
        info.addProperty("x", infoX)
        info.addProperty("y", infoY)
        val lyric = JsonObject()
        lyric.addProperty("x", lyricX)
        lyric.addProperty("y", lyricY)
        data.add("Info", info)
        data.add("Lyric", lyric)
        sendPMsgToAM(data.toString())
    }

    fun sendPMsgToZMAddon(data: String) {
        sendPMsg(data, "zmusic:channel")
    }

    fun sendMsg(msg: String)

    fun sendErrMsg(msg: String)

    fun sendJsonMsg(msg: TextComponent)

    fun sendActionBar(msg: TextComponent)

    fun sendTitle(title: String, subTitle: String)

    fun sendNull(player: Any) {
        sendMsg(Lang.Help.tip)
    }

    fun sendPlayErr(musicName: String) {
        for (s in Lang.Play.error) {
            sendErrMsg(s.replace("%musicName%", musicName))
        }
    }

    fun hasPermission(permission: String): Boolean

    val isPlayer: Boolean

    val onlinePlayerList: List<Any>
    val online: Boolean
    val name: String
}
