package me.zhenxin.zmusic.platform.impl

import me.zhenxin.zmusic.platform.Logger
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
    override fun log(msg: String) = sender.sendMessage(msg)

}