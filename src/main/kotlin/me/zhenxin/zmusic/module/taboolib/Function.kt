package me.zhenxin.zmusic.module.taboolib

import me.zhenxin.zmusic.module.Config
import me.zhenxin.zmusic.module.taboolib.component.Component
import me.zhenxin.zmusic.module.taboolib.pluginmessage.PluginMessage
import net.kyori.adventure.text.minimessage.MiniMessage
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.implementations

/**
 * TabooLib 扩展函数
 *
 * @author 真心
 * @since 2021/8/22 14:28
 * @email qgzhenxin@qq.com
 */

fun ProxyCommandSender.sendMsg(msg: String) = sendMsg(MiniMessage.get().parse(msg))

fun ProxyCommandSender.sendMsg(component: net.kyori.adventure.text.Component) {
    sendMessage(net.kyori.adventure.text.Component.text(Config.PREFIX).append(component))
}


fun ProxyCommandSender.registerChannel(channel: String) {
    return implementations<PluginMessage>().registerChannel(channel)
}

fun ProxyPlayer.sendPluginMessage(channel: String, data: ByteArray) {
    return implementations<PluginMessage>().sendMessage(this, channel, data)
}

fun ProxyPlayer.playMusic(url: String) {
    sendPluginMessage("allmusic:channel", "[Play]$url".toByteArray())
    sendPluginMessage("zmusic:channel", "[Play]$url".toByteArray())
}

fun ProxyCommandSender.sendMessage(component: net.kyori.adventure.text.Component) {
    return implementations<Component>().sendMsg(this, component)
}
