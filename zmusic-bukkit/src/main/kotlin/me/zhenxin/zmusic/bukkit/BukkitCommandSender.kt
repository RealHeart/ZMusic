package me.zhenxin.zmusic.bukkit

import me.zhenxin.zmusic.platform.entity.ZCommandSender
import me.zhenxin.zmusic.platform.entity.ZPlayer
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID

/**
 * Bukkit 命令发送者包装。
 *
 * @property sender Bukkit 命令发送者
 * @author 真心
 * @since 5.0.0
 */
open class BukkitCommandSender(private val sender: CommandSender) : ZCommandSender {
    /** 发送者名称。 */
    override val name: String get() = sender.name
    /** 是否为 Bukkit 玩家。 */
    override val isPlayer: Boolean get() = sender is Player

    /**
     * 转发 Bukkit 权限检查。
     *
     * @param permission 权限节点
     * @return true 表示有权限
     */
    override fun hasPermission(permission: String): Boolean {
        return sender.hasPermission(permission)
    }

    /**
     * 使用 Bukkit 旧颜色码发送消息。
     *
     * @param message 消息内容
     */
    override fun sendMessage(message: String) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
    }

    /**
     * 玩家发送者转为 ZMusic 玩家。
     *
     * @return 玩家对象；非玩家返回 null
     */
    override fun asPlayer(): ZPlayer? {
        return (sender as? Player)?.let(::BukkitPlayer)
    }
}

/**
 * Bukkit 玩家包装。
 *
 * @property player Bukkit 玩家对象
 * @author 真心
 * @since 5.0.0
 */
class BukkitPlayer(private val player: Player) : BukkitCommandSender(player), ZPlayer {
    /** Bukkit 玩家 UUID。 */
    override val uniqueId: UUID get() = player.uniqueId
    /** 玩家包装固定为 true。 */
    override val isPlayer: Boolean get() = true
    /**
     * 取回原始 Bukkit 玩家对象。
     *
     * @return Bukkit 玩家对象
     */
    fun platformPlayer(): Player = player
}
