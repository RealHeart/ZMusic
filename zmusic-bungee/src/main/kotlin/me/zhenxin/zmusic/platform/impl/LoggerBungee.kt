package me.zhenxin.zmusic.platform.impl

import me.zhenxin.zmusic.platform.Logger
import me.zhenxin.zmusic.utils.colored
import net.md_5.bungee.api.CommandSender


/**
 * BungeeCord 日志实现
 *
 * @author 真心
 * @since 2023/8/28 12:28
 * @email qgzhenxin@qq.com
 */
@Suppress("DEPRECATION")
class LoggerBungee(private val sender: CommandSender) : Logger {
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
        sender.sendMessage("&b$msg".colored())
    }
}