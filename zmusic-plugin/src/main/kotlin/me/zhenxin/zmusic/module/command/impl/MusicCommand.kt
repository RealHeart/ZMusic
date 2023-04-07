package me.zhenxin.zmusic.module.command.impl

import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.ZMusicData
import me.zhenxin.zmusic.enums.asMusicPlatform
import me.zhenxin.zmusic.enums.getPlatformNames
import me.zhenxin.zmusic.module.music.MusicPlayer
import me.zhenxin.zmusic.module.taboolib.jsonmessage.ClickCommand
import me.zhenxin.zmusic.module.taboolib.sendClickMessage
import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.proto.sendToast
import me.zhenxin.zmusic.status.setState
import me.zhenxin.zmusic.utils.asMusicApi
import me.zhenxin.zmusic.utils.colored
import me.zhenxin.zmusic.utils.isChina
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers
import taboolib.common.platform.function.submit

/**
 * 点歌命令
 *
 * @author 真心
 * @since 2021/8/23 0:36
 * @email qgzhenxin@qq.com
 */
val musicCommand = subCommand {
    dynamic(comment = Lang.COMMAND_SUGGESTION_PLATFORM) {
        suggestion<ProxyPlayer> { _, _ ->
            getPlatformNames()
        }
        dynamic(comment = Lang.COMMAND_SUGGESTION_SONG) {
            suggestion<ProxyPlayer>(true) { _, _ ->
                listOf("[${Lang.COMMAND_SUGGESTION_SONG}]")
            }
            execute<ProxyPlayer> { sender, context, argument ->
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
                if (argument.contains("-id:")) {
                    val p = platform.asMusicPlatform()
                    if (!p.supportIdPlay) {
                        sender.sendMsg(Lang.COMMAND_NOSUPPORTED_IDPLAY)
                        return@execute
                    }
                }
                val api = platform.asMusicApi()
                submit(async = true) {
                    val result = api.searchSingle(argument)
                    if (result == null) {
                        sender.sendMsg(Lang.MUSIC_NOT_FOUND)
                        return@submit
                    }
                    onlinePlayers().forEach {
                        val player = MusicPlayer(
                            it,
                            api,
                            mutableListOf(result)
                        )
                        player.start()
                        it.setState(player = player)
                        it.sendToast(Lang.TOAST_PLAYING.replace("{0}", result.name).colored())
                        it.sendClickMessage(
                            Lang.COMMAND_MUSIC_SUCCESS
                                .replace("{0}", sender.name)
                                .replace("{1}", api.name)
                                .replace("{2}", "${result.singer} - ${result.name}")
                                .colored(),
                            arrayOf(
                                ClickCommand(
                                    "&f[&c${Lang.MESSAGE_JSON_STOP}&f]".colored(),
                                    Lang.MESSAGE_JSON_TIPS.colored(),
                                    "/zm stop"
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}