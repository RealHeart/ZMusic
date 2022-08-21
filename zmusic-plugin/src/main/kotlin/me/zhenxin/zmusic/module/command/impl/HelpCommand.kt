package me.zhenxin.zmusic.module.command.impl

import me.zhenxin.zmusic.module.taboolib.sendMsg
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper

/**
 * 帮助命令
 *
 * @author 真心
 * @since 2021/8/14 21:43
 * @email qgzhenxin@qq.com
 */

val helpCommand = subCommand {
    execute<ProxyCommandSender> { sender, context, _ ->
        sender.sendMsg("&6========== &r[&bZMusic&r] &eBy: ZhenXin &6==========")
        createHelper()
        sender.sendMsg("&6==================================")

    }
}