package me.zhenxin.zmusic.module.command

import me.zhenxin.zmusic.module.taboolib.sendMsg
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand

/**
 * 播放命令
 *
 * @author 真心
 * @since 2021/8/22 16:34
 * @email qgzhenxin@qq.com
 */

val playCommand = subCommand {
    dynamic {
        suggestion<ProxyPlayer> { _, _ ->
            listOf("netease", "qq", "kugou")
        }
        execute<ProxyPlayer> { sender, context, argument ->

        }
        dynamic {
            execute<ProxyPlayer> { sender, context, argument ->
                sender.sendMsg("Test")
            }
        }
    }
}