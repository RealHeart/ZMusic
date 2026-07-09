package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.platform.PlatformLogger
import net.md_5.bungee.api.plugin.Plugin

/**
 * BungeeCord logger 适配器。
 *
 * @property plugin BungeeCord 插件实例
 * @author 真心
 * @since 5.0.0
 */
class BungeePlatformLogger(private val plugin: Plugin) : PlatformLogger {
    /**
     * 输出 info。
     *
     * @param message 日志内容
     */
    override fun info(message: String) {
        plugin.logger.info(message)
    }

    /**
     * 输出 warning。
     *
     * @param message 日志内容
     */
    override fun warn(message: String) {
        plugin.logger.warning(message)
    }

    /**
     * 输出 severe 和异常栈。
     *
     * @param message 日志内容
     * @param throwable 关联异常，可以为 null
     */
    override fun error(message: String, throwable: Throwable?) {
        plugin.logger.severe(message)
        throwable?.printStackTrace()
    }
}
