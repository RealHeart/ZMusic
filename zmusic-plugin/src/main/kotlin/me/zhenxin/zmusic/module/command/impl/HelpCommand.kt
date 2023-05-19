package me.zhenxin.zmusic.module.command.impl

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.component.CommandComponent
import taboolib.common.platform.command.component.CommandComponentDynamic
import taboolib.common.platform.command.component.CommandComponentLiteral
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
    createHelper()
    execute<ProxyCommandSender> { sender, context, _ ->
        println(context.commandCompound.findChildren(context))

        fun message(component: CommandComponent): String {
            val sb = StringBuilder()
            if (component.children.isNotEmpty()) {
                component.children.forEach {
                    it as CommandComponentDynamic
                    println(it.children)
                    sb.append(it.comment)
                }
            }
            return sb.toString()
        }

        val filterChildren = context.commandCompound.children.filter { sender.hasPermission(it.permission) }
        val checkedChildren = filterChildren.filter { it !is CommandComponentLiteral || !it.hidden }

        checkedChildren.forEach {
            val message = message(it)
            println(message)
        }
    }
}