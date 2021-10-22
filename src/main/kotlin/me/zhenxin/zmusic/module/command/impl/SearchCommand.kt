package me.zhenxin.zmusic.module.command.impl

import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.type.getPlatformNames
import me.zhenxin.zmusic.utils.asMusicApi
import me.zhenxin.zmusic.utils.colored
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand

/**
 * 搜索命令
 *
 * @author 真心
 * @since 2021/9/17 11:54
 * @email qgzhenxin@qq.com
 */
val searchCommand = subCommand {
    dynamic(commit = Lang.COMMAND_SUGGESTION_PLATFORM) {
        suggestion<ProxyPlayer> { _, _ ->
            getPlatformNames()
        }
        dynamic(commit = Lang.COMMAND_SUGGESTION_SONG) {
            suggestion<ProxyPlayer>(true) { _, _ ->
                listOf("[${Lang.COMMAND_SUGGESTION_SONG}]")
            }
            execute<ProxyPlayer> { sender, context, argument ->
                sender.sendMsg(Lang.COMMAND_PLAY_SEARCHING)
                val platform = context.argument(-1)
                val api = platform.asMusicApi()
                val result = api.searchPage(argument, 1, 10)
                logger.debug(result)
                sender.sendMsg(Lang.COMMAND_SEARCH_HEADER)
                result.forEachIndexed { i, m ->
                    val msg = "&d${i + 1}&c.&a${m.name} - ${m.singer} " +
                            "<red>[<yellow><click:run_command:'/zm play $platform -id:${m.id}'><hover:show_text:'${Lang.MESSAGE_JSON_TIPS}'>${Lang.MESSAGE_JSON_PLAY}</click><red>] " +
                            "<red>[<yellow><click:run_command:'/zm music $platform -id:${m.id}'><hover:show_text:'${Lang.MESSAGE_JSON_TIPS}'>${Lang.MESSAGE_JSON_MUSIC}</click><red>]"
                    sender.sendMsg(msg.colored())
                }
                sender.sendMsg(Lang.COMMAND_SEARCH_FOOTER)
            }
        }
    }
}