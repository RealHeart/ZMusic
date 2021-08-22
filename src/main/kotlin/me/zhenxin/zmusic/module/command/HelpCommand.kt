package me.zhenxin.zmusic.module.command

import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.taboolib.sendMsg
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand

/**
 * 帮助命令
 *
 * @author 真心
 * @since 2021/8/14 21:43
 * @email qgzhenxin@qq.com
 */

val helpCommand = subCommand {
    execute<ProxyCommandSender> { sender, context, _ ->
        Lang.HELP_MAIN.forEach {
            if (it.contains("[admin]")) {
                if (sender.hasPermission("zmusic.admin")) {
                    sender.sendMsg(
                        it
                            .replace("{0}", context.name)
                            .replace("[admin]", "")
                    )
                }
            } else {
                sender.sendMsg(it.replace("{0}", context.name))
            }
        }
    }
}