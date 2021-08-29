package me.zhenxin.zmusic.module.command.impl

import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.module.api.MusicApi
import me.zhenxin.zmusic.module.api.impl.NeteaseApi
import me.zhenxin.zmusic.module.taboolib.playMusic
import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.module.taboolib.sendPluginMessage
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
                var api: MusicApi = NeteaseApi()
                when (context.argument(1)) {
                    "netease" -> api = NeteaseApi()
                }
                val result = api.searchSingle(argument)
                val url = api.getPlayUrl(result.id)
                sender.playMusic(url)
            }
        }
    }
}