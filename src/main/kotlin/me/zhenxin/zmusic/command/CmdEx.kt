package me.zhenxin.zmusic.command

import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.module.Sender

internal object CmdEx {

    fun tab(sender: Sender, args: Array<String>): MutableList<String> {
        return mutableListOf()
    }

    fun cmd(sender: Sender, args: Array<String>): Boolean {
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
                    if (it.contains("[admin]"))
                        sender.sendMsg(it.split("[admin]")[1])
                    else
                        sender.sendMsg(it)
                }
            }
        } else {
            sender.sendMsg(Lang.Help.tip)
        }
        return true
    }
}
