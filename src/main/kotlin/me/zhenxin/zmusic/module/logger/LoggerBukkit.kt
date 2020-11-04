package me.zhenxin.zmusic.module.logger

import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Logger
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

internal class LoggerBukkit(private val sender: CommandSender) : Logger {


    override fun log(msg: String) =
        sender.sendMessage(Config.prefix + ChatColor.GREEN + msg)

    override fun debug(msg: String) {
        if (Config.debug)
            sender.sendMessage(Config.prefix + ChatColor.YELLOW + "[Debug] " + msg)
    }

    override fun error(msg: String) =
        sender.sendMessage(Config.prefix + ChatColor.RED + msg)

}
