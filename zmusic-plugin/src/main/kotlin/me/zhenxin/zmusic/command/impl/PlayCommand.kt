@file:Suppress("SpellCheckingInspection")

package me.zhenxin.zmusic.command.impl

import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.enums.asMusicPlatform
import me.zhenxin.zmusic.enums.getPlatformNames
import me.zhenxin.zmusic.music.MusicPlayer
import me.zhenxin.zmusic.proto.sendToast
import me.zhenxin.zmusic.status.setState
import me.zhenxin.zmusic.taboolib.extend.sendMsg
import me.zhenxin.zmusic.utils.asMusicApi
import me.zhenxin.zmusic.utils.colored
import me.zhenxin.zmusic.utils.isChina
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit

/**
 * 播放命令
 *
 * @author 真心
 * @since 2021/8/22 16:34
 * @email qgzhenxin@qq.com
 */

val playCommand = subCommand {
    dynamic(commit = Lang.COMMAND_SUGGESTION_PLATFORM) {
        suggestion<ProxyPlayer> { _, _ ->
            getPlatformNames()
        }
        dynamic(commit = Lang.COMMAND_SUGGESTION_SONG) {
            suggestion<ProxyPlayer>(true) { _, _ ->
                listOf("[${Lang.COMMAND_SUGGESTION_SONG}]")
            }
            execute<ProxyPlayer> { sender, context, argument ->
                sender.sendMsg(Lang.COMMAND_SEARCHING)
                val platform = context.argument(-1)
                if (platform == "soundcloud") {
                    if (isChina()) {
                        sender.sendMsg(Lang.NO_SUPPORTED_REGION)
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
                    val player = MusicPlayer(
                        sender,
                        api,
                        mutableListOf(result)
                    )
                    player.start()
                    sender.setState(player = player)
                    sender.sendToast(Lang.TOAST_PLAYING.replace("{0}", result.name).colored())
                    sender.sendMsg(
                        Lang.COMMAND_PLAY_SUCCESS
                            .replace("{0}", api.name)
                            .replace("{1}", "${result.singer} - ${result.name}")
                    )
                }
            }
        }
    }
}