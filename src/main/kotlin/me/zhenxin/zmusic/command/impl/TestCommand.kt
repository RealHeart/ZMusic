package me.zhenxin.zmusic.command.impl

import me.zhenxin.zmusic.api.impl.NeteaseApi
import me.zhenxin.zmusic.proto.sendToast
import me.zhenxin.zmusic.taboolib.extend.sendPluginMessage
import me.zhenxin.zmusic.utils.colored
import taboolib.common.platform.Platform
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.runningPlatform
import taboolib.common.platform.function.submit

/**
 * 测试命令
 *
 * @author 真心
 * @since 2021/8/14 20:45
 * @email qgzhenxin@qq.com
 */

val testCommand = subCommand {
    execute<ProxyPlayer> { sender, _, _ ->
        submit {
            val api = NeteaseApi()
            val result = api.searchSingle("让风告诉你")
            println(result)
            sender.sendPluginMessage(
                "[Play]${api.getPlayUrl(result.id)}"
            )
        }
    }
    execute<ProxyPlayer> { sender, _, _ ->
        if (runningPlatform == Platform.BUKKIT) {
            sender.sendToast("&a正在播放\n&e让风告诉你".colored())
        }
    }
}