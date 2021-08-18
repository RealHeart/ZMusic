package me.zhenxin.zmusic.module.taboolib

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.implementations

/**
 * 插件消息多平台实现
 *
 * @author 真心
 * @since 2021/8/16 17:11
 * @email qgzhenxin@qq.com
 */
interface PluginMessage {

    fun registerChannel(channel: String)

    fun sendMessage(sender: ProxyPlayer, channel: String, data: ByteArray)
}

fun ProxyCommandSender.registerChannel(channel: String) {
    return implementations<PluginMessage>().registerChannel(channel)
}

fun ProxyPlayer.sendPluginMessage(channel: String, data: ByteArray) {
    return implementations<PluginMessage>().sendMessage(this, channel, data)
}