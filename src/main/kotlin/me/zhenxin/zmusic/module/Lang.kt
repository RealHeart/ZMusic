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
    val PLATFORM_NETEASE
        get() = console().asLangText("platform-netease")
    val PLATFORM_QQ
        get() = console().asLangText("platform-qq")
    val PLATFORM_BILI
        get() = console().asLangText("platform-bili")
    val PLATFORM_KUGOU
        get() = console().asLangText("platform-kugou")
    val PLATFORM_XIMA
        get() = console().asLangText("platform-xima")

    val INIT_LOADING
        get() = console().asLangText("init-loading").colored()
    val INIT_LOADED
        get() = console().asLangTextList("init-loaded").colored()

    val HELP_TIPS
        get() = console().asLangText("help-tips").colored()
    val HELP_MAIN
        get() = console().asLangTextList("help-main").colored()

    val COMMAND_INCORRECT_COMMAND
        get() = console().asLangTextList("command-incorrect-command").colored()
    val COMMAND_INCORRECT_SENDER
        get() = console().asLangText("command-incorrect-sender").colored()
    val COMMAND_PLAY_SEARCHING
        get() = console().asLangText("command-play-searching").colored()
    val COMMAND_PLAY_SUCCESS
        get() = console().asLangText("command-play-success").colored()

    val COMMAND_SUGGESTION_PLATFORM
        get() = console().asLangText("command-suggestion-platform")
    val COMMAND_SUGGESTION_SONG
        get() = console().asLangText("command-suggestion-song")

    val TOAST_PLAYING
        get() = console().asLangText("toast-playing").colored().replace("\\n", "\n")
}