package me.zhenxin.zmusic.module.player

import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Player
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class PlayerBukkit : Player {
    override fun sendMsg(msg: String, player: Any) {
        player as CommandSender
        player.sendMessage(Config.prefix + ChatColor.GREEN + msg)
    }

    override fun sendErrMsg(msg: String, player: Any) {
        player as CommandSender
        player.sendMessage(Config.prefix + ChatColor.RED + msg)
    }

    override fun sendJsonMsg(msg: TextComponent, player: Any) {
        player as CommandSender
        player.spigot().sendMessage(msg)
    }

    override fun sendActionBar(msg: TextComponent, player: Any) {
        player as org.bukkit.entity.Player
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, msg)
    }

    @Suppress("DEPRECATION")
    override fun sendTitle(title: String, subTitle: String, player: Any) {
        player as org.bukkit.entity.Player
        try {
            player.sendTitle(title, subTitle, 0, 200, 20)
        } catch (e: NoSuchMethodError) {

            player.sendTitle(title, subTitle)
        }
    }

}
