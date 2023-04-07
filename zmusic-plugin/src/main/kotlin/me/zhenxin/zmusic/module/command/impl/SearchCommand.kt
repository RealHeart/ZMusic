package me.zhenxin.zmusic.module.command.impl

import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.ZMusicData
import me.zhenxin.zmusic.enums.asMusicPlatform
import me.zhenxin.zmusic.enums.getPlatformNames
import me.zhenxin.zmusic.module.taboolib.jsonmessage.ClickCommand
import me.zhenxin.zmusic.module.taboolib.sendClickMessage
import me.zhenxin.zmusic.module.taboolib.sendClickPageBar
import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.utils.asMusicApi
import me.zhenxin.zmusic.utils.colored
import me.zhenxin.zmusic.utils.isChina
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
    dynamic(comment = Lang.COMMAND_SUGGESTION_PLATFORM) {
        suggestion<ProxyPlayer> { _, _ ->
            getPlatformNames()
        }
        dynamic(comment = Lang.COMMAND_SUGGESTION_SONG) {
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
                if (platform == "soundcloud" || platform == "youtube") {
                    if (isChina()) {
                        sender.sendMsg(Lang.NO_SUPPORTED_REGION)
                        return@execute
                    }
                }
                if (platform == "netease") {
                    if (Config.NETEASE_API_LINK.isEmpty()) {
                        sender.sendMsg(Lang.PLATFORM_NETEASE_NOT_FOUND_API)
                        return@execute
                    }
                }
                if (platform == "bilibili") {
                    if (!ZMusicData.IS_VIP) {
                        sender.sendMsg("&c由于需要服务器转码, 此平台需要VIP授权方可使用.")
                        return@execute
                    }
                }
                val supportId = platform.asMusicPlatform().supportIdPlay
                val api = platform.asMusicApi()
                submit(async = true) {
                    val result = api.searchPage(args, page, 10)
                    if (result == null) {
                        sender.sendMsg(Lang.MUSIC_NOT_FOUND)
                        return@submit
                    }
                    sender.sendMsg(Lang.COMMAND_SEARCH_HEADER)
                    result.forEachIndexed { i, m ->
                        val keyword = if (supportId) "-id:${m.id}" else "${m.name} ${m.singer}"
                        sender.sendClickMessage(
                            "&d${i + 1}&c.&a${m.name} - ${m.singer}".colored(),
                            arrayOf(
                                ClickCommand(
                                    "&f[&e${Lang.MESSAGE_JSON_PLAY}&f]".colored(),
                                    Lang.MESSAGE_JSON_TIPS.colored(),
                                    "/zm play $platform $keyword"
                                ),
                                ClickCommand(
                                    "&f[&e${Lang.MESSAGE_JSON_MUSIC}&f]".colored(),
                                    Lang.MESSAGE_JSON_TIPS.colored(),
                                    "/zm music $platform $keyword"
                                ),
                            )
                        )
                    }
                    sender.sendClickPageBar(
                        "&6======{prev}&6========================{next}&6=======".colored(),
                        ClickCommand(
                            "&c<<<<".colored(),
                            Lang.MESSAGE_JSON_TIPS_PREV.colored(),
                            "/zm search $platform $args -page:${page - 1}"
                        ),
                        ClickCommand(
                            "&c>>>>".colored(),
                            Lang.MESSAGE_JSON_TIPS_NEXT.colored(),
                            "/zm search $platform $args -page:${page + 1}"
                        )
                    )
                }
            }
        }
    }
}