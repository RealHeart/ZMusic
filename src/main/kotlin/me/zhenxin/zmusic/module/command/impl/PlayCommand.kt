package me.zhenxin.zmusic.module.command.impl

import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.impl.NeteaseApi
import me.zhenxin.zmusic.module.api.impl.QQMusicApi
import me.zhenxin.zmusic.module.taboolib.playMusic
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
            listOf("netease", "qq", "kugou", "bilibili", "xima")
        }
        execute<ProxyPlayer> { sender, _, argument ->
            sender.sendMsg(argument)
        }
        dynamic {
            suggestion<ProxyPlayer>(true) { _, _ ->
                listOf("<歌名>")
            }
            execute<ProxyPlayer> { sender, context, argument ->
                sender.sendMsg(Lang.COMMAND_PLAY_SEARCHING)
                val platform = context.argument(-1)
                val api: MusicApi
                when (platform) {
                    "netease" -> {
                        api = NeteaseApi()
                    }
                    "qq" -> {
                        api = QQMusicApi()
                    }
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