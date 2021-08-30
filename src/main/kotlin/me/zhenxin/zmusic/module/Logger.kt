package me.zhenxin.zmusic.module

import taboolib.common.platform.ProxyCommandSender

/**
 * 日志模块
 *
 * @author 真心
 * @since 2021/8/14 22:41
 * @email qgzhenxin@qq.com
 */
class Logger(private val sender: ProxyCommandSender) {
    fun log(msg: Any) {
        sender.sendMessage(Config.PREFIX + msg)
    }

    fun debug(msg: Any) {
        if (Config.DEBUG) {
            sender.sendMessage(Config.PREFIX + "§e[Debug] $msg")
        }
    }
}