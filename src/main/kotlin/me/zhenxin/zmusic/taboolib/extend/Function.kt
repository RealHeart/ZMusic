  package me.zhenxin.zmusic.taboolib.extend

import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.taboolib.extend.component.Component
import me.zhenxin.zmusic.taboolib.extend.pluginmessage.PluginMessage
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
fun ProxyCommandSender.sendMsg(component: me.zhenxin.adventure.text.Component) {
    sendMessage(config.PREFIX.component().append(component))
}

/**
 * 发送消息 Component
 * @param component Component
 */
fun ProxyCommandSender.sendMessage(component: me.zhenxin.adventure.text.Component) {
    return implementations<Component>().sendMsg(this, component)
}

/**
 * 注册通信频道
 * @param channel 频道ID
 */
fun registerChannel(channel: String) {
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
 * 发送插件消息 (聚合)
 * @param data 消息
 */
fun ProxyPlayer.sendPluginMessage(data: String) {
    sendPluginMessage("allmusic:channel", data.toByteArray())
    sendPluginMessage("zmusic:channel", data.toByteArray())
}
