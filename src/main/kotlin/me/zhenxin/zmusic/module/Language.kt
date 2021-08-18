package me.zhenxin.zmusic.module

import taboolib.common.platform.ProxyCommandSender
import taboolib.module.chat.colored
import taboolib.module.lang.asLangText


/**
 * 语言
 *
 * @author 真心
 * @since 2021/8/14 22:30
 * @email qgzhenxin@qq.com
 */
object Language {

    var LOADING = ""

    fun init(sender: ProxyCommandSender) {
        LOADING = sender.asLangText("loading").colored()
    }
}