package me.zhenxin.zmusic.taboolib.extend

import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.taboolib.extend.jsonmessage.ClickCommand
import me.zhenxin.zmusic.taboolib.extend.jsonmessage.JsonMessage
import me.zhenxin.zmusic.taboolib.extend.pluginmessage.PluginMessage
import me.zhenxin.zmusic.utils.colored
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
fun ProxyCommandSender.sendMsg(msg: String) = sendMessage("${config.PREFIX}$msg".colored())

/**
 * 发送点击消息
 * @param base 基本消息
 * @param commands 命令
 */
fun ProxyPlayer.sendClickMessage(base: String, commands: Array<ClickCommand>) {
    return implementations<JsonMessage>().sendClickMessage(this, base, commands)
}

/**
 * 发送分页条
 * @param pageBar 分页条消息内容 使用{prev} {next} 指定分页点击消息位置
 * @param prev 上一页命令
 * @param next 下一页命令
 */
fun ProxyPlayer.sendClickPageBar(pageBar: String, prev: ClickCommand, next: ClickCommand) {
    return implementations<JsonMessage>().sendClickPageBar(this, pageBar, prev, next)
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
    logger.debug("PluginMessage: $data")
    sendPluginMessage("allmusic:channel", data.toByteArray())
    sendPluginMessage("zmusic:channel", data.toByteArray())
}
