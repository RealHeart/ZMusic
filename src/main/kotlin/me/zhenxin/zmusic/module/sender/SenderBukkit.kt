package me.zhenxin.zmusic.module.sender

import io.netty.buffer.Unpooled
import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.ZMusicBukkit
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Sender
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender


internal class SenderBukkit(private val sender: CommandSender) : Sender {
    override fun sendPMsgToABF(data: String) {
        try {
            ZMusic.tasker?.async {
                (sender as org.bukkit.entity.Player).sendPluginMessage(
                    ZMusic.plugin as ZMusicBukkit,
                    "AudioBuffer",
                    data.toByteArray()
                )
            }
        } catch (e: Exception) {
            ZMusic.logger?.debug("[Mod通信] 数据发送发生错误")
        }
    }

    override fun sendPMsg(data: String, channel: String) {
        try {
            val bytes: ByteArray = data.toByteArray()
            val buf = Unpooled.buffer(bytes.size + 1)
            buf.writeByte(666)
            buf.writeBytes(bytes)
            ZMusic.tasker?.async {
                (sender as org.bukkit.entity.Player).sendPluginMessage(
                    ZMusic.plugin as ZMusicBukkit,
                    channel,
                    buf.array()
                )
            }
        } catch (e: Exception) {
            ZMusic.logger?.debug("[Mod通信] 数据发送发生错误")
        }
    }

    override fun sendMsg(msg: String) =
        sender.sendMessage(Config.prefix + ChatColor.GREEN + msg)

    override fun sendErrMsg(msg: String) =
        sender.sendMessage(Config.prefix + ChatColor.RED + msg)

    override fun sendJsonMsg(msg: TextComponent) = sender.spigot().sendMessage(msg)

    override fun sendActionBar(msg: TextComponent) =
        (sender as org.bukkit.entity.Player).spigot().sendMessage(ChatMessageType.ACTION_BAR, msg)

    @Suppress("DEPRECATION")
    override fun sendTitle(title: String, subTitle: String) {
        val player = sender as org.bukkit.entity.Player
        try {
            player.sendTitle(title, subTitle, 0, 200, 20)
        } catch (e: NoSuchMethodError) {
            player.sendTitle(title, subTitle)
        }
    }

    override fun hasPermission(permission: String): Boolean = sender.hasPermission(permission)

    override val isPlayer: Boolean = sender is org.bukkit.entity.Player
    override val onlinePlayerList: List<Any> = listOf(Bukkit.getServer().onlinePlayers)
    override val online: Boolean = (sender as org.bukkit.entity.Player).isOnline
    override val name: String = sender.name

}
