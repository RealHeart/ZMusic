package me.zhenxin.zmusic.module.taboolib

import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.module.taboolib.jsonmessage.ClickCommand
import me.zhenxin.zmusic.module.taboolib.jsonmessage.JsonMessage
import me.zhenxin.zmusic.module.taboolib.pluginmessage.PluginMessage
import me.zhenxin.zmusic.status.removeBossBar
import me.zhenxin.zmusic.status.setState
import me.zhenxin.zmusic.utils.colored
import me.zhenxin.zmusic.utils.stopMusic
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.implementations
import taboolib.common.platform.function.submit

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
fun ProxyCommandSender.sendMsg(msg: String) = sendMessage("${config.PREFIX}$msg".colored())

/**
 * 发送点击消息
 * @param base 基本消息
 * @param commands 命令
 */
fun ProxyPlayer.sendClickMessage(base: String, commands: Array<ClickCommand>) {
    return implementations<JsonMessage>().sendClickMessage(this, "${config.PREFIX.colored()}$base", commands)
}

/**
 * 发送分页条
 * @param pageBar 分页条消息内容 使用{prev} {next} 指定分页点击消息位置
 * @param prev 上一页命令
 * @param next 下一页命令
 */
fun ProxyPlayer.sendClickPageBar(pageBar: String, prev: ClickCommand, next: ClickCommand) {
    return implementations<JsonMessage>().sendClickPageBar(this, "${config.PREFIX.colored()}$pageBar", prev, next)
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
 * @param message 发送数据
 */
fun ProxyPlayer.sendPluginMessage(channel: String, message: ByteArray) {
    submit(async = true) {
        implementations<PluginMessage>().sendMessage(this@sendPluginMessage, channel, message)
    }
}

/**
 * 发送通信桥
 * @param message 发送数据
 */
fun ProxyPlayer.sendBridgeMessage(message: ByteArray) {
    val channel = "zmusic:bridge"
    submit(async = true) {
        implementations<PluginMessage>().sendBridgeMessage(this@sendBridgeMessage, channel, message)
    }
}

/**
 * 发送插件消息 (聚合)
 * @param message 消息
 */
fun ProxyPlayer.sendPluginMessage(message: String) {
    logger.debug("PluginMessage: $message")
    sendPluginMessage("zmusic:channel", message.toByteArray())
    sendPluginMessage("allmusic:channel", message.toByteArray())
}


fun ProxyPlayer.resetData() {
    stopMusic()
    removeBossBar()
    setState()
}