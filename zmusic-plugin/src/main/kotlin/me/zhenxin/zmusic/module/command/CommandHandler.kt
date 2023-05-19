package me.zhenxin.zmusic.module.command

import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.module.command.impl.*
import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.utils.playMusic
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.PermissionDefault.OP
import taboolib.common.platform.command.PermissionDefault.TRUE
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand

/**
 * 命令处理器
 *
 * @author 真心
 * @since 2021/8/14 20:41
 * @email qgzhenxin@qq.com
 */
@Suppress("unused")
@CommandHeader(
    name = "zmusic",
    aliases = ["zm", "music"],
    permission = "zmusic.use",
    permissionDefault = TRUE,
)
object CommandHandler {

    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { sender, context, _ ->
            sender.sendMsg(Lang.HELP_TIPS.replace("{0}", context.name))
        }
        incorrectCommand { sender, context, _, _ ->
            sender.sendMsg(Lang.HELP_TIPS.replace("{0}", context.name))
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
        permission = "zmusic.user.search",
        permissionDefault = TRUE
    )
    val search = searchCommand

    @CommandBody(
        optional = true,
        permission = "zmusic.user.queue",
        permissionDefault = TRUE
    )
    val queue = queueCommand

    @CommandBody(
        optional = true,
        permission = "zmusic.user.url",
        permissionDefault = TRUE
    )
    val url = subCommand {
        execute<ProxyPlayer> { sender, _, argument ->
            sender.playMusic(argument.replace("url ", ""))
        }
    }

    @CommandBody(
        optional = true,
        permission = "zmusic.admin.main",
        permissionDefault = OP
    )
    val admin = adminCommand

    @CommandBody(
        optional = true,
        permission = "zmusic.admin.login",
        permissionDefault = OP
    )
    val login = loginCommand

    @CommandBody(
        optional = true,
        permission = "zmusic.admin.reload",
        permissionDefault = OP
    )
    val reload = reloadCommand
}