package me.zhenxin.zmusic.module.command

import me.zhenxin.zmusic.module.command.body.helpCommand
import me.zhenxin.zmusic.module.command.body.testCommand
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand

/**
 * 命令处理器
 *
 * @author 真心
 * @since 2021/8/14 20:41
 * @email qgzhenxin@qq.com
 */
@CommandHeader(name = "zm", aliases = ["zmusic", "music"], permission = "zmusic.use")
object CommandHandler {
    @CommandBody(permission = "zmusic.test", optional = true)
    val test = testCommand

    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { sender, _, argument ->
            sender.sendMessage(argument)
        }
    }

    @CommandBody(permission = "zmusic.help", optional = true)
    val help = helpCommand
}