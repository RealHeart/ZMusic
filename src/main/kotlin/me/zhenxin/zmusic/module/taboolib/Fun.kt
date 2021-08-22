package me.zhenxin.zmusic.module.taboolib

import me.zhenxin.zmusic.module.Config
import taboolib.common.platform.ProxyCommandSender

/**
 * TabooLib 扩展函数
 *
 * @author 真心
 * @since 2021/8/22 14:28
 * @email qgzhenxin@qq.com
 */

fun ProxyCommandSender.sendMsg(msg: String) {
    sendMessage(Config.PREFIX + msg)
}