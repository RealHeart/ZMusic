package me.zhenxin.zmusic.module.player

import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.ZMusicBC
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Player
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.Title
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer


class PlayerBC : Player {
    override fun sendMsg(msg: String, player: Any) {
        player as CommandSender
        player.sendMessage(TextComponent(Config.prefix + ChatColor.GREEN + msg))
    }

    override fun sendErrMsg(msg: String, player: Any) {
        player as CommandSender
        player.sendMessage(TextComponent(Config.prefix + ChatColor.RED + msg))
    }

    override fun sendJsonMsg(msg: TextComponent, player: Any) {
        player as ProxiedPlayer
        player.sendMessage(msg)
    }

    override fun sendActionBar(msg: TextComponent, player: Any) {
        player as ProxiedPlayer
        player.sendMessage(ChatMessageType.ACTION_BAR, msg)
    }

    override fun sendTitle(title: String, subTitle: String, player: Any) {
        player as ProxiedPlayer
        val plugin = ZMusic.plugin as ZMusicBC
        val message: Title = plugin.proxy.createTitle().apply {
            title(TextComponent(title))
            subTitle(TextComponent(subTitle))
            fadeIn(0)
            stay(200)
            fadeOut(20)
        }
        player.sendTitle(message)
    }

}
