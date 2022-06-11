package me.zhenxin.zmusic.command.impl

import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.taboolib.extend.sendMsg
import me.zhenxin.zmusic.utils.component
import taboolib.common.platform.ProxyCommandSender
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
        sender.sendMsg(
            "&6========== &r[<gradient:#66CCFF:#0088FF>ZMusic</gradient>&r] &eBy: ZhenXin &6=========="

                .component()
        )
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