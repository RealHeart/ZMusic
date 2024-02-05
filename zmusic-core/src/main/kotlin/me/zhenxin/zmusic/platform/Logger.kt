package me.zhenxin.zmusic.platform

import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.utils.colored

/**
 * 日志
 *
 * @author 真心
 * @since 2023/7/22 20:08
 * @email qgzhenxin@qq.com
 */
interface Logger {
    /**
     * 信息日志
     * @param msg String 日志信息
     */
    fun info(msg: String) {
        val prefix = Config.prefix.colored()
        val color = "&a".colored()
        val message = msg.colored()
        log("$prefix$color$message")
    }

    /**
     * 错误日志
     * @param msg String 日志信息
     */
    fun error(msg: String) {
        val prefix = Config.prefix.colored()
        val color = "&c".colored()
        val message = msg.colored()
        log("$prefix$color$message")
    }

    /**
     * 调试日志
     * @param msg String 日志信息
     */
    fun debug(msg: String) {
        if (Config.debug) {
            val prefix = Config.prefix.colored()
            val color = "&e[Debug] ".colored()
            log("$prefix$color$msg")
        }
    }

    /**
     * 打印日志
     * @param msg String 日志信息
     */
    fun log(msg: String)
}
