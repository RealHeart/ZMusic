package me.zhenxin.zmusic.module

import me.zhenxin.zmusic.utils.colored
import taboolib.common.platform.ProxyCommandSender
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
    var PLATFORM_NETEASE = ""
    var PLATFORM_QQ = ""
    var PLATFORM_BILI = ""
    var PLATFORM_KUGOU = ""
    var PLATFORM_XIMA = ""

    var INIT_LOADING = ""
    var INIT_LOADED = emptyList<String>()

    var HELP_TIPS = ""
    var HELP_MAIN = emptyList<String>()

    var COMMAND_NOT_PERMISSION = ""
    var COMMAND_PLAY_SEARCHING = ""
    var COMMAND_PLAY_SUCCESS = ""

    var COMMAND_SUGGESTION_SONG = ""

    fun init(sender: ProxyCommandSender) {
        PLATFORM_NETEASE = sender.asLangText("platform-netease")
        PLATFORM_QQ = sender.asLangText("platform-qq")
        PLATFORM_BILI = sender.asLangText("platform-bili")
        PLATFORM_KUGOU = sender.asLangText("platform-kugou")
        PLATFORM_XIMA = sender.asLangText("platform-xima")

        INIT_LOADING = sender.asLangText("init-loading").colored()
        INIT_LOADED = sender.asLangTextList("init-loaded").colored()

        HELP_TIPS = sender.asLangText("help-tips").colored()
        HELP_MAIN = sender.asLangTextList("help-main").colored()

        COMMAND_NOT_PERMISSION = sender.asLangText("command-not-permission").colored()
        COMMAND_PLAY_SEARCHING = sender.asLangText("command-play-searching").colored()
        COMMAND_PLAY_SUCCESS = sender.asLangText("command-play-success").colored()
        COMMAND_SUGGESTION_SONG = sender.asLangText("command-suggestion-song")
    }
}