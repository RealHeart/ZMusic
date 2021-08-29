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
object Lang {

    var INIT_LOADING = ""
    var INIT_LOADED = emptyList<String>()
    var HELP_TIPS = ""
    var HELP_MAIN = emptyList<String>()
    var COMMAND_NOT_PERMISSION = ""

    fun init(sender: ProxyCommandSender) {
        INIT_LOADING = sender.asLangText("init-loading").colored()
        INIT_LOADED = sender.asLangTextList("init-loaded").colored()
        HELP_TIPS = sender.asLangText("help-tips").colored()
        HELP_MAIN = sender.asLangTextList("help-main").colored()
        COMMAND_NOT_PERMISSION = sender.asLangText("command-not-permission").colored()
    }
}