package me.zhenxin.zmusic.module.command.body

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.asLangTextList

/**
 * 帮助命令
 *
 * @author 真心
 * @since 2021/8/14 21:43
 * @email qgzhenxin@qq.com
 */

val helpCommand: SimpleCommandBody = subCommand {
    execute<ProxyCommandSender> { sender, _, _ ->
        val help by lazy { sender.asLangTextList("help") }
        help.forEach {
            sender.sendMessage(it)
        }
    }
}