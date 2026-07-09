package me.zhenxin.zmusic.velocity

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import me.zhenxin.zmusic.platform.entity.ZCommandSender
import me.zhenxin.zmusic.platform.entity.ZPlayer
import net.kyori.adventure.text.Component
import java.util.UUID

/**
 * Velocity 命令发送者包装。
 *
 * @property sender Velocity 命令发送者
 * @author 真心
 * @since 5.0.0
 */
open class VelocityCommandSender(private val sender: CommandSource) : ZCommandSender {
    /** 发送者名称。 */
    override val name: String get() = (sender as? Player)?.username ?: "Console"
    /** 是否为 Velocity 玩家。 */
    override val isPlayer: Boolean get() = sender is Player

    /**
     * 转发 Velocity 权限检查。
     *
     * @param permission 权限节点
     * @return true 表示有权限
     */
    override fun hasPermission(permission: String): Boolean {
        return sender.hasPermission(permission)
    }

    /**
     * 发送纯文本消息。
     *
     * @param message 消息内容
     */
    override fun sendMessage(message: String) {
        sender.sendMessage(Component.text(message))
    }

    /**
     * 玩家发送者转为 ZMusic 玩家。
     *
     * @return 玩家对象；非玩家返回 null
     */
    override fun asPlayer(): ZPlayer? {
        return (sender as? Player)?.let(::VelocityPlayer)
    }
}

/**
 * Velocity 玩家包装。
 *
 * @property player Velocity 玩家对象
 * @author 真心
 * @since 5.0.0
 */
class VelocityPlayer(private val player: Player) : VelocityCommandSender(player), ZPlayer {
    /** 玩家 UUID。 */
    override val uniqueId: UUID get() = player.uniqueId
    /** 玩家包装固定为 true。 */
    override val isPlayer: Boolean get() = true

    /**
     * 取回原始 Velocity 玩家对象。
     *
     * @return Velocity 玩家对象
     */
    fun platformPlayer(): Player = player
}
