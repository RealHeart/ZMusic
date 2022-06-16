package me.zhenxin.zmusic.command.impl

import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.enums.asMusicPlatform
import me.zhenxin.zmusic.enums.getPlatformNames
import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.taboolib.extend.sendMsg
import me.zhenxin.zmusic.utils.asMusicApi
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit

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
                var page = 1
                var args = argument
                if (argument.contains("-page:")) {
                    try {
                        page = argument.split("-page:")[1].toInt()
                        args = argument.split(" -page:")[0]
                    } catch (e: Exception) {
                    }
                }
                sender.sendMsg(Lang.COMMAND_SEARCHING)
                val platform = context.argument(-1)
                val supportId = platform.asMusicPlatform().supportIdPlay
                val api = platform.asMusicApi()
                submit(async = true) {
                    val result = api.searchPage(args, page, 10)
                    sender.sendMsg(Lang.COMMAND_SEARCH_HEADER)
                    result.forEachIndexed { i, m ->
                        val keyword = if (supportId) "-id:${m.id}" else "${m.name} ${m.singer}"
                        val msg = "&d${i + 1}&c.&a${m.name} - ${m.singer} " +
                                "<red>[<yellow><click:run_command:'/zm play $platform ${
                                    keyword.replace(
                                        "'",
                                        "\\'"
                                    )
                                }'><hover:show_text:'${Lang.MESSAGE_JSON_TIPS}'>${Lang.MESSAGE_JSON_PLAY}</click><red>] " +
                                "<red>[<yellow><click:run_command:'/zm music $platform ${
                                    keyword.replace(
                                        "'",
                                        "\\'"
                                    )
                                }'><hover:show_text:'${Lang.MESSAGE_JSON_TIPS}'>${Lang.MESSAGE_JSON_MUSIC}</click><red>]"
                        sender.sendMsg(msg)
                    }
                    val prev =
                        "<click:run_command:'/zm search $platform $args -page:${page - 1}'><hover:show_text:'${Lang.MESSAGE_JSON_TIPS_PREV}'><<<</click>"
                    val next =
                        "<click:run_command:'/zm search $platform $args -page:${page + 1}'><hover:show_text:'${Lang.MESSAGE_JSON_TIPS_NEXT}'>>>></click>"
                    sender.sendMsg("<gold>======<red>$prev<gold>=====================<red>${next}<gold>=======")
                }
            }
        }
    }
}