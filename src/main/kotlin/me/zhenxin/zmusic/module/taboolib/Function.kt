package me.zhenxin.zmusic.module.taboolib

import me.zhenxin.zmusic.module.Config
import me.zhenxin.zmusic.module.taboolib.component.Component
import me.zhenxin.zmusic.module.taboolib.pluginmessage.PluginMessage
import me.zhenxin.zmusic.utils.component
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

/**
 * 发送消息
 * @param msg 消息
 */
fun ProxyCommandSender.sendMsg(msg: String) = sendMsg(msg.component())

/**
 * 发送消息 Component
 * @param component Component
 */
fun ProxyCommandSender.sendMsg(component: net.kyori.adventure.text.Component) {
    sendMessage(Config.PREFIX.component().append(component))
}

/**
 * 发送消息 Component
 * @param component Component
 */
fun ProxyCommandSender.sendMessage(component: net.kyori.adventure.text.Component) {
    return implementations<Component>().sendMsg(this, component)
}

/**
 * 注册通信频道
 * @param channel 频道ID
 */
fun ProxyCommandSender.registerChannel(channel: String) {
    return implementations<PluginMessage>().registerChannel(channel)
}

/**
 * 发送插件消息
 * @param channel 消息频道ID
 * @param data 发送数据
 */
fun ProxyPlayer.sendPluginMessage(channel: String, data: ByteArray) {
    return implementations<PluginMessage>().sendMessage(this, channel, data)
}

/**
 * 播放音乐
 * @param url 播放链接
 */
fun ProxyPlayer.playMusic(url: String) {
    sendPluginMessage("allmusic:channel", "[Play]$url".toByteArray())
    sendPluginMessage("zmusic:channel", "[Play]$url".toByteArray())
}

/**
 * 发送插件消息 (聚合)
 * @param data 消息
 */
fun ProxyPlayer.sendPluginMessage(data: String) {
    sendPluginMessage("allmusic:channel", data.toByteArray())
    sendPluginMessage("zmusic:channel", data.toByteArray())
}
