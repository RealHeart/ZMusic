package me.zhenxin.zmusic.command

import me.zhenxin.zmusic.bossbar.createBossBar
import me.zhenxin.zmusic.bossbar.getBossBar
import me.zhenxin.zmusic.command.impl.*
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.taboolib.extend.sendMsg
import me.zhenxin.zmusic.utils.colored
import me.zhenxin.zmusic.utils.playMusic
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.PermissionDefault.*
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
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
        permission = "zmusic.devloper.test",
        permissionDefault = FALSE
    )
    val test = subCommand {
        execute<ProxyPlayer> { sender, context, _ ->
            submit(async = true) {
                sender.createBossBar()
                val bossBar = sender.getBossBar()
                bossBar.show("", 100F)
                for (i in 0..100) {
                    bossBar.updateTitle("&bBossBar Test: Current Number $i".colored())
                    Thread.sleep(1000)
                }
            }
        }
    }


    @CommandBody
    val main = mainCommand {
        createHelper()
        incorrectCommand { sender, context, index, _ ->
            var args = ""
            for (i in (1 - index)..0) {
                args = "${context.argument(i)} "
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

    @CommandBody(
        optional = true,
        permission = "zmusic.user.url",
        permissionDefault = TRUE
    )
    val url = subCommand {
        execute<ProxyPlayer> { sender, _, argument ->
            submit {
                sender.playMusic(argument.replace("url ", ""))
            }
        }
    }
}