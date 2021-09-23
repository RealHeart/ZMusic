package me.zhenxin.zmusic.module.command

import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.command.impl.*
import me.zhenxin.zmusic.module.taboolib.sendMsg
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.PermissionDefault.*
import taboolib.common.platform.command.mainCommand
import taboolib.expansion.createHelper

/**
 * 命令处理器
 *
 * @author 真心
 * @since 2021/8/14 20:41
 * @email qgzhenxin@qq.com
 */
@Suppress("unused", "SpellCheckingInspection")
@CommandHeader(
    name = "zm",
    aliases = ["zmusic", "music"],
    permission = "zmusic.use",
    permissionDefault = TRUE
)
object CommandHandler {
    @CommandBody(
        optional = true,
        permission = "zmusic.developer",
        permissionDefault = FALSE
    )
    val test = testCommand

    @CommandBody
    val main = mainCommand {
        createHelper()
        incorrectCommand { sender, context, index, _ ->
            var args = ""
            for (i in (1 - index)..0) {
                args = "${context.argument(i).toString()} "
            }
            args = args.trimEnd(' ')
            Lang.COMMAND_INCORRECT_COMMAND.forEach {
                sender.sendMsg(
                    it.replace("{0}", context.name)
                        .replace("{1}", args)
                )
            }
        }
        incorrectSender { sender, _ ->
            sender.sendMsg(Lang.COMMAND_INCORRECT_SENDER)
        }
    }

    @CommandBody(
        optional = true,
        permission = "zmusic.user.help",
        permissionDefault = TRUE
    )
    val help = helpCommand

    @CommandBody(
        optional = true,
        permission = "zmusic.user.play",
        permissionDefault = TRUE
    )
    val play = playCommand

    @CommandBody(
        optional = true,
        permission = "zmusic.user.stop",
        permissionDefault = TRUE
    )
    val stop = stopCommand

    @CommandBody(
        optional = true,
        permission = "zmusic.user.music",
        permissionDefault = TRUE
    )
    val music = musicCommand

    @CommandBody(
        optional = true,
        permission = "zmusic.user.queue",
        permissionDefault = TRUE
    )
    val queue = queueCommand

    @CommandBody(
        optional = true,
        permission = "zmusic.admin.main",
        permissionDefault = OP
    )
    val admin = adminCommand

    @CommandBody(
        optional = true,
        permission = "zmusic.admin.reload",
        permissionDefault = OP
    )
    val reload = reloadCommand
}