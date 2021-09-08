package me.zhenxin.zmusic.module

import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.utils.colored
import taboolib.common.platform.ProxyCommandSender

/**
 * 日志模块
 *
 * @author 真心
 * @since 2021/8/14 22:41
 * @email qgzhenxin@qq.com
 */
class Logger(private val sender: ProxyCommandSender) {
    fun info(msg: Any) {
        sender.sendMsg("$msg")
    }

    fun debug(msg: Any) {
        if (Config.DEBUG) {
            sender.sendMsg("&e[Debug] $msg".colored())
        }
    }
}