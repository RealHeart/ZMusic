@file:Suppress("SpellCheckingInspection")

package me.zhenxin.zmusic.module.command.impl

import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.impl.*
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
                "bilibili           ",
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
                    "netease" -> NeteaseApi() // 网易云音乐
                    "qq" -> QQMusicApi() // QQ音乐
                    "bilibili" -> BiliBiliApi() // 哔哩哔哩
                    "kugou" -> KuGouApi() // 酷狗
                    "xima" -> XimaApi() // 喜马拉雅
                    else -> return@execute // 理论上永远不会执行
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