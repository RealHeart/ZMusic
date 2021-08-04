package me.zhenxin.zmusic.common.modules.sender.impl

import me.zhenxin.zmusic.common.modules.sender.Sender
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer

/**
 * Sender Bungee 实现
 *
 * @author 真心
 * @since 2021/8/4 12:31
 * @email qgzhenxin@qq.com
 */
class BungeeSender(private val sender: CommandSender) : Sender {
    override fun sendMsg(msg: String) {
        sender.sendMessage(TextComponent(msg))
    }

    override fun hasPem(pem: String): Boolean {
        return sender.hasPermission(pem)
    }

    override val isPlayer: Boolean = sender is ProxiedPlayer


    override val name: String = sender.name


    override val playerUniqueId: String = if (isPlayer) {
        (sender as ProxiedPlayer).uniqueId.toString()
    } else {
        ""
    }

}