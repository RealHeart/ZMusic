package me.zhenxin.zmusic.command

import com.alibaba.fastjson.JSONObject
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
                    when (args[1].toLowerCase()) {
                        "qq" -> {
                            val api = ApiType.QQ.getApi()
                            val data = api.search("璃月", count = 8)
                            sender.sendMsg(data.toJSONString())
                        }
                        "163" -> {
                            val api = ApiType.NETEASE.getApi()
                            val data = api.search("璃月", count = 8)
                            sender.sendMsg(data.toJSONString())
                            data.getJSONArray("data").forEach {
                                it as JSONObject
                                sender.sendMsg(api.url(it.getString("id")).toJSONString())
                            }
                        }
                        "bili" -> {
                            val api = ApiType.BILIBILI.getApi()
                            val data = api.search("璃月", count = 8)
                            sender.sendMsg(data.toJSONString())
                            data.getJSONArray("data").forEach {
                                it as JSONObject
                                sender.sendMsg(api.url(it.getString("id")).toJSONString())
                            }
                        }
                    }
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
