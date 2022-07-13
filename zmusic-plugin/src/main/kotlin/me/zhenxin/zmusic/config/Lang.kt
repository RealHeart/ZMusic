package me.zhenxin.zmusic.config

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
    val PLATFORM_BILI
        get() = console().asLangText("platform-bili")
    val PLATFORM_XIMA
        get() = console().asLangText("platform-xima")

    val INIT_LOADING
        get() = console().asLangText("init-loading")
    val INIT_LOADED
        get() = console().asLangTextList("init-loaded")

    val HELP_TIPS
        get() = console().asLangText("help-tips")
    val HELP_MAIN
        get() = console().asLangTextList("help-main")

    val COMMAND_INCORRECT_COMMAND
        get() = console().asLangTextList("command-incorrect-command")
    val COMMAND_INCORRECT_SENDER
        get() = console().asLangText("command-incorrect-sender")
    val COMMAND_SEARCHING
        get() = console().asLangText("command-searching")
    val COMMAND_NOSUPPORTED_IDPLAY
        get() = console().asLangText("command-nosupported-idplay")
    val COMMAND_PLAY_SUCCESS
        get() = console().asLangText("command-play-success")
    val COMMAND_MUSIC_SUCCESS
        get() = console().asLangText("command-music-success")
    val COMMAND_SEARCH_HEADER
        get() = console().asLangText("command-search-header")
    val COMMAND_RELOAD_SUCCESS
        get() = console().asLangText("command-reload-success")

    val COMMAND_SUGGESTION_PLATFORM
        get() = console().asLangText("command-suggestion-platform")
    val COMMAND_SUGGESTION_SONG
        get() = console().asLangText("command-suggestion-song")

    val MESSAGE_JSON_PLAY
        get() = console().asLangText("message-json-play")
    val MESSAGE_JSON_MUSIC
        get() = console().asLangText("message-json-music")
    val MESSAGE_JSON_STOP
        get() = console().asLangText("message-json-stop")
    val MESSAGE_JSON_PLAYALL
        get() = console().asLangText("message-json-playall")
    val MESSAGE_JSON_QUEUE
        get() = console().asLangText("message-json-queue")
    val MESSAGE_JSON_TIPS
        get() = console().asLangText("message-json-tips")
    val MESSAGE_JSON_TIPS_PREV
        get() = console().asLangText("message-json-tips-prev")
    val MESSAGE_JSON_TIPS_NEXT
        get() = console().asLangText("message-json-tips-next")

    val TOAST_PLAYING
        get() = console().asLangText("toast-playing").replace("\\n", "\n")

    val NO_SUPPORTED_REGION
        get() = console().asLangText("no-supported-region")

    val UPDATE_CHECKING
        get() = console().asLangText("update-checking")
    val UPDATE_NO_UPDATE
        get() = console().asLangText("update-no-update")
    val UPDATE_NEW_VERSION
        get() = console().asLangTextList("update-new-version")
}