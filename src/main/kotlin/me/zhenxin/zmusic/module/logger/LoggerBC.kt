package me.zhenxin.zmusic.module.logger

import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Logger
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

class LoggerBC(private val sender: CommandSender) : Logger {


    override fun normal(msg: String) {
        sender.sendMessage(TextComponent(Config.prefix + ChatColor.GREEN + msg))
    }

    override fun debug(msg: String) {
        normal(Config.debug.toString())
        if (Config.debug) {
            sender.sendMessage(TextComponent(Config.prefix + ChatColor.YELLOW + "[Debug] " + msg))
        }
    }

    override fun error(msg: String) {
        sender.sendMessage(TextComponent(Config.prefix + ChatColor.RED + msg))
    }

}
