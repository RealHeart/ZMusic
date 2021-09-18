package me.zhenxin.zmusic.module.command.impl

import me.zhenxin.zmusic.utils.stopMusic
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit

val stopCommand = subCommand {
    execute<ProxyPlayer> { sender, _, _ ->
        submit {
            sender.stopMusic()
        }
    }
}