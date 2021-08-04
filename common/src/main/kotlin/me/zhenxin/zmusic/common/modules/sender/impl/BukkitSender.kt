package me.zhenxin.zmusic.common.modules.sender.impl

import me.zhenxin.zmusic.common.modules.sender.Sender
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Sender Bukkit 实现
 *
 * @author 真心
 * @since 2021/8/4 12:31
 * @email qgzhenxin@qq.com
 */
class BukkitSender(private val sender: CommandSender) : Sender {
    override fun sendMsg(msg: String) {
        sender.sendMessage(msg)
    }

    override fun hasPem(pem: String): Boolean {
        return sender.hasPermission(pem)
    }

    override val isPlayer: Boolean = sender is Player

    override val name: String = sender.name

    override val playerUniqueId: String = if (isPlayer) {
        (sender as Player).uniqueId.toString()
    } else {
        ""
    }
}