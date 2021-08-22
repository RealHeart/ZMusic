package me.zhenxin.zmusic.module.command

import me.zhenxin.zmusic.module.api.impl.NeteaseApi
import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.module.taboolib.sendPluginMessage
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit

/**
 * 测试命令
 *
 * @author 真心
 * @since 2021/8/14 20:45
 * @email qgzhenxin@qq.com
 */

val testCommand = subCommand {
    execute<ProxyPlayer> { sender, _, argument ->
        submit {
            sender.sendMsg(argument)
            val api = NeteaseApi()
            val result = api.searchSingle("勾指起誓")
            println(result)
            sender.sendPluginMessage(
                "allmusic:channel",
                "[Play]${api.getPlayUrl(result.id)}".toByteArray()
            )
        }
    }
}