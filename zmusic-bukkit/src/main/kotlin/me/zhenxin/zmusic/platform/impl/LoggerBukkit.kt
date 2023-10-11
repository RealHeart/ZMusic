package me.zhenxin.zmusic.platform.impl

import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.platform.Logger
import me.zhenxin.zmusic.utils.colored
import org.bukkit.command.CommandSender

/**
 * Bukkit 日志实现
 *
 * @author 真心
 * @since 2023/7/23 9:05
 * @email qgzhenxin@qq.com
 */
class LoggerBukkit(private val sender: CommandSender) : Logger {
    override fun info(msg: String) {
        sender.sendMessage("&a$msg".colored())
    }

    override fun warn(msg: String) {
        sender.sendMessage("&e$msg".colored())
    }

    override fun error(msg: String) {
        sender.sendMessage("&c$msg".colored())
    }

    override fun debug(msg: String) {
        if (Config.debug) {
            sender.sendMessage("&b$msg".colored())
        }
    }
}