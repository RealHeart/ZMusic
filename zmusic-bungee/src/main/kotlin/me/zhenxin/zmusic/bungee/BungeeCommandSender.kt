package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.platform.entity.ZCommandSender
import me.zhenxin.zmusic.platform.entity.ZPlayer
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.UUID

/**
 * BungeeCord 命令发送者包装。
 *
 * @property sender BungeeCord 命令发送者
 * @author 真心
 * @since 5.0.0
 */
open class BungeeCommandSender(private val sender: CommandSender) : ZCommandSender {
    /** 发送者名称。 */
    override val name: String get() = sender.name
    /** 是否为代理端玩家。 */
    override val isPlayer: Boolean get() = sender is ProxiedPlayer

    /**
     * 转发 BungeeCord 权限检查。
     *
     * @param permission 权限节点
     * @return true 表示有权限
     */
    override fun hasPermission(permission: String): Boolean {
        return sender.hasPermission(permission)
    }

    /**
     * 使用 BungeeCord 旧颜色码发送消息。
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
        return (sender as? ProxiedPlayer)?.let(::BungeePlayer)
    }
}

/**
 * BungeeCord 玩家包装。
 *
 * @property player BungeeCord 玩家对象
 * @author 真心
 * @since 5.0.0
 */
class BungeePlayer(private val player: ProxiedPlayer) : BungeeCommandSender(player), ZPlayer {
    /** 玩家 UUID。 */
    override val uniqueId: UUID get() = player.uniqueId
    /** 玩家包装固定为 true。 */
    override val isPlayer: Boolean get() = true
    /**
     * 取回原始 BungeeCord 玩家对象。
     *
     * @return BungeeCord 玩家对象
     */
    fun platformPlayer(): ProxiedPlayer = player
}
