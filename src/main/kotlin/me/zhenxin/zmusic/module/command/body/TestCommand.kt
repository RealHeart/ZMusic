package me.zhenxin.zmusic.module.command.body

import me.zhenxin.zmusic.module.taboolib.sendPluginMessage
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand

/**
 * 测试命令
 *
 * @author 真心
 * @since 2021/8/14 20:45
 * @email qgzhenxin@qq.com
 */

val testCommand: SimpleCommandBody = subCommand {
    execute<ProxyPlayer> { sender, _, argument ->
        sender.sendMessage(argument)
        sender.sendPluginMessage("zmusic:channel", "test".toByteArray())
    }
}