package me.zhenxin.zmusic.command

import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.module.Sender
import me.zhenxin.zmusic.type.ApiType

object CmdEx {

    fun tab(sender: Sender, args: Array<String>): MutableList<String> {
        return mutableListOf()
    }

    fun cmd(sender: Sender, args: Array<String>): Boolean {
        if (!ZMusic.zLibIsOK) {
            ZMusic.logger.error(Lang.Loading.zLibNoOK.replace("%version%", ZMusic.zLibVer))
            return true
        }
        if (!ZMusic.isEnabled) return true
        if (!ZMusic.isEnable) return true
        val isUse = sender.hasPermission("zmusic.use")
        val isAdmin = sender.hasPermission("zmusic.admin")
        if (isUse) {
            if (args.isEmpty()) {
                sender.sendMsg(Lang.Help.tip)
                return true
            }
            when (args[0].toLowerCase()) {
                "help" -> Lang.Help.main.forEach {
                    if (it.contains("[admin]")) {
                        sender.sendMsg(it.split("[admin]")[1])
                    } else {
                        sender.sendMsg(it)
                    }
                }
                "play" -> {

                }
                "test" -> {
                    val type = when (args[1].toLowerCase()) {
                        "qq" -> ApiType.QQ
                        "163" -> ApiType.NETEASE
                        "bili" -> ApiType.BILIBILI
                        else -> ApiType.NETEASE
                    }
                    val api = type.getApi()
                    val data = api.search(args[2], count = 1)
                    val list = data.getJSONArray("data")
                    sender.sendMsg(data.toJSONString())
                    val url = api.url(list.getJSONObject(0).getString("id")).getJSONObject("data").getString("url")
                    sender.sendMsg(url)
                    sender.sendPMsgToAM("[Play]$url")
                }
                else -> {
                    sender.sendMsg(Lang.Help.tip)
                }
            }
        } else {
            sender.sendMsg(Lang.Help.tip)
        }
        return true
    }
}
