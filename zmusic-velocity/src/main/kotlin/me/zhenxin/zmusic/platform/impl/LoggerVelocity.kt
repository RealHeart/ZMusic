package me.zhenxin.zmusic.platform.impl

import com.velocitypowered.api.command.CommandSource
import me.zhenxin.zmusic.platform.Logger
import me.zhenxin.zmusic.utils.uncolored
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

/**
 * Velocity 日志实现
 *
 * @author 真心
 * @since 2023/7/24 11:01
 * @email qgzhenxin@qq.com
 */
class LoggerVelocity(private val sender: CommandSource) : Logger {
    override fun log(msg: String) = sender.sendMessage(
        LegacyComponentSerializer.legacyAmpersand().deserialize(msg.uncolored())
    )

}