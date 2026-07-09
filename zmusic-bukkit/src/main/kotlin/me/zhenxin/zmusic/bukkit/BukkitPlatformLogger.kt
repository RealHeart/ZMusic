package me.zhenxin.zmusic.bukkit

import me.zhenxin.zmusic.platform.PlatformLogger
import org.bukkit.plugin.java.JavaPlugin

/**
 * Bukkit logger 适配器。
 *
 * @property plugin Bukkit 插件实例
 * @author 真心
 * @since 5.0.0
 */
class BukkitPlatformLogger(private val plugin: JavaPlugin) : PlatformLogger {
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
