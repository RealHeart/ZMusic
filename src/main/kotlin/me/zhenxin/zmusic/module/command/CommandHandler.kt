package me.zhenxin.zmusic.module.command

import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.command.impl.*
import me.zhenxin.zmusic.module.taboolib.sendMsg
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
@Suppress("unused")
@CommandHeader(
    name = "zm",
    aliases = ["zmusic", "music"],
    permission = "zmusic.use"
)
object CommandHandler {
    @CommandBody(optional = true, permission = "zmusic.developer")
    val test = testCommand

    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { sender, context, _ ->
            sender.sendMsg(
                Lang.HELP_TIPS
                    .replace("{0}", context.name)
            )
        }
    }

    @CommandBody(optional = true, permission = "zmusic.user.help")
    val help = helpCommand

    @CommandBody(optional = true, permission = "zmusic.user.play")
    val play = playCommand

    @CommandBody(optional = true, permission = "zmusic.admin")
    val music = musicCommand

    @CommandBody(optional = true, permission = "zmusic.admin")
    val queue = queueCommand

    @CommandBody(optional = true, permission = "zmusic.admin")
    val admin = adminCommand
}