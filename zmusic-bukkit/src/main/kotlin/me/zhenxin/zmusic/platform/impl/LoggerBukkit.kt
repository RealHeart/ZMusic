package me.zhenxin.zmusic.platform.impl

import me.zhenxin.zmusic.platform.Logger
import org.bukkit.command.CommandSender

/**
 * Bukkit 日志实现
 *
 * @author 真心
 * @since 2023/7/23 9:05
 * @email qgzhenxin@qq.com
 */
class LoggerBukkit(private val sender: CommandSender) : Logger {
    override fun log(msg: String) = sender.sendMessage(msg)

}