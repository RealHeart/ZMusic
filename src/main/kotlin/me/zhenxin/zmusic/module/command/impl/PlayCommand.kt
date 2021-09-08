@file:Suppress("SpellCheckingInspection")

package me.zhenxin.zmusic.module.command.impl

import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.impl.NeteaseApi
import me.zhenxin.zmusic.module.api.impl.QQMusicApi
import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.utils.playMusic
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
            listOf(
                "netease",
                "qq",
                "bilibili",
                "kugou",
                "xima"
            )
        }
        execute<ProxyPlayer> { sender, _, argument ->
            sender.sendMsg(argument)
        }
        dynamic {
            suggestion<ProxyPlayer>(true) { _, _ ->
                listOf("[${Lang.COMMAND_SUGGESTION_SONG}]")
            }
            execute<ProxyPlayer> { sender, context, argument ->
                sender.sendMsg(Lang.COMMAND_PLAY_SEARCHING)
                val api = when (context.argument(-1)) {
                    "netease" -> NeteaseApi()
                    "qq" -> QQMusicApi()
                    else -> return@execute
                }
                val result = api.searchSingle(argument)
                logger.debug(result)
                val url = api.getPlayUrl(result.id)
                sender.playMusic(url)
                sender.sendMsg(
                    Lang.COMMAND_PLAY_SUCCESS
                        .replace("{0}", api.name)
                        .replace("{1}", "${result.singer} - ${result.name}")
                )
            }
        }
    }
}