package me.zhenxin.zmusic.platform.impl

import com.velocitypowered.api.command.CommandSource
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.platform.Logger
import me.zhenxin.zmusic.utils.colored
import net.kyori.adventure.text.Component

/**
 * Velocity 日志实现
 *
 * @author 真心
 * @since 2023/7/24 11:01
 * @email qgzhenxin@qq.com
 */
class LoggerVelocity(private val sender: CommandSource) : Logger {
    override fun info(msg: String) {
        val message = Component.text("&a$msg".colored())
        sender.sendMessage(message)
    }

    override fun warn(msg: String) {
        val message = Component.text("&e$msg".colored())
        sender.sendMessage(message)
    }

    override fun error(msg: String) {
        val message = Component.text("&c$msg".colored())
        sender.sendMessage(message)
    }

    override fun debug(msg: String) {
        if (Config.debug) {
            val message = Component.text("&b$msg".colored())
            sender.sendMessage(message)
        }
    }
}