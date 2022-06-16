package me.zhenxin.zmusic.command.impl

import me.zhenxin.zmusic.enums.MusicPlatform
import me.zhenxin.zmusic.enums.getPlatformNamesWithSupportAccount
import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.taboolib.extend.sendMsg
import me.zhenxin.zmusic.utils.loginNetease
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand

/**
 * 登录命令
 *
 * @author 真心
 * @since 2021/9/27 21:04
 * @email qgzhenxin@qq.com
 */
val loginCommand = subCommand {
    dynamic(commit = "平台") {
        suggestion<ProxyCommandSender> { _, _ ->
            getPlatformNamesWithSupportAccount()
        }
        execute<ProxyCommandSender> { sender, _, argument ->
            val arguments = argument.split(" ")
            val platform = MusicPlatform.valueOf(arguments[0].uppercase())
            logger.debug(argument)
            when (platform) {
                // 暂时实验性 后续加入语言文件
                MusicPlatform.NETEASE -> {
                    sender.sendMsg("&a正在尝试登录网易云音乐...")
                    val result = loginNetease()
                    sender.sendMsg(result.message)
                }
                else -> return@execute
            }
        }
    }
}