package me.zhenxin.zmusic.module.sender

import io.netty.buffer.Unpooled
import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.module.Sender
import net.md_5.bungee.api.*
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer


class SenderBC(private val sender: Any) : Sender {
    override fun sendPMsgToABF(data: String) {
        try {
            ZMusic.tasker.async {
                (sender as ProxiedPlayer).server.info.sendData(
                    "AudioBuffer",
                    data.toByteArray()
                )
            }
        } catch (e: Exception) {
            ZMusic.logger.debug("[Mod通信] 数据发送发生错误")
        }
    }

    override fun sendPMsg(data: String, channel: String) {
        try {
            val bytes: ByteArray = data.toByteArray()
            val buf = Unpooled.buffer(bytes.size + 1)
            buf.writeByte(1024)
            buf.writeBytes(bytes)
            (sender as ProxiedPlayer).server.info.sendData(
                channel,
                buf.array()
            )
        } catch (e: Exception) {
            ZMusic.logger.debug("[Mod通信] 数据发送发生错误")
        }
    }

    override fun sendMsg(msg: String) =
        (sender as CommandSender).sendMessage(TextComponent(Config.prefix + ChatColor.GREEN + msg))

    override fun sendErrMsg(msg: String) =
        (sender as CommandSender).sendMessage(TextComponent(Config.prefix + ChatColor.RED + msg))

    override fun sendJsonMsg(msg: TextComponent) = (sender as CommandSender).sendMessage(msg)

    override fun sendActionBar(msg: TextComponent) =
        (sender as ProxiedPlayer).sendMessage(ChatMessageType.ACTION_BAR, msg)

    override fun sendTitle(title: String, subTitle: String) {
        val message: Title = ProxyServer.getInstance().createTitle().apply {
            title(TextComponent(title))
            subTitle(TextComponent(subTitle))
            fadeIn(0)
            stay(200)
            fadeOut(20)
        }
        sender as ProxiedPlayer
        sender.sendTitle(message)
    }

    override fun hasPermission(permission: String): Boolean = (sender as CommandSender).hasPermission(permission)

    override val isPlayer: Boolean = sender is ProxiedPlayer
    override val onlinePlayerList: List<Any> = listOf(ProxyServer.getInstance().players)
    override val online: Boolean = if (sender is ProxiedPlayer) sender.isConnected else false
    override val name: String = (sender as CommandSender).name
}
