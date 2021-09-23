package me.zhenxin.zmusic.module

import me.zhenxin.zmusic.utils.colored
import taboolib.common.platform.function.console
import taboolib.module.lang.asLangText
import taboolib.module.lang.asLangTextList


/**
 * 语言
 *
 * @author 真心
 * @since 2021/8/14 22:30
 * @email qgzhenxin@qq.com
 */
@Suppress("SpellCheckingInspection")
object Lang {
    val PLATFORM_NETEASE = console().asLangText("platform-netease")
    val PLATFORM_QQ = console().asLangText("platform-qq")
    val PLATFORM_BILI = console().asLangText("platform-bili")
    val PLATFORM_KUGOU = console().asLangText("platform-kugou")
    val PLATFORM_XIMA = console().asLangText("platform-xima")

    val INIT_LOADING = console().asLangText("init-loading").colored()
    val INIT_LOADED = console().asLangTextList("init-loaded").colored()

    val HELP_TIPS = console().asLangText("help-tips").colored()
    val HELP_MAIN = console().asLangTextList("help-main").colored()

    val COMMAND_INCORRECT_COMMAND = console().asLangTextList("command-incorrect-command").colored()
    val COMMAND_INCORRECT_SENDER = console().asLangText("command-incorrect-sender").colored()
    val COMMAND_PLAY_SEARCHING = console().asLangText("command-play-searching").colored()
    val COMMAND_PLAY_SUCCESS = console().asLangText("command-play-success").colored()

    val COMMAND_SUGGESTION_PLATFORM = console().asLangText("command-suggestion-platform")
    val COMMAND_SUGGESTION_SONG = console().asLangText("command-suggestion-song")

    val TOAST_PLAYING = console().asLangText("toast-playing").colored().replace("\\n", "\n")
}