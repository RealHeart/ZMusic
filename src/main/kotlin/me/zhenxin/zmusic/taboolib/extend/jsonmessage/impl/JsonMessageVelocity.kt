package me.zhenxin.zmusic.taboolib.extend.jsonmessage.impl

import com.velocitypowered.api.proxy.Player
import me.zhenxin.zmusic.taboolib.extend.jsonmessage.ClickCommand
import me.zhenxin.zmusic.taboolib.extend.jsonmessage.JsonMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyPlayer

/**
 * JSON消息 Velocity 实现
 *
 * @author 真心
 * @since 2021/7/3 14:20
 * @email qgzhenxin@qq.com
 */

@Suppress("unused", "DuplicatedCode")
@PlatformImplementation(Platform.VELOCITY)
class JsonMessageVelocity : JsonMessage {

    override fun sendClickMessage(sender: ProxyPlayer, base: String, commands: Array<ClickCommand>) {
        val player = sender.cast<Player>()
        val msg = Component.empty().toBuilder()
        msg.append(Component.text(base))
        for (it in commands) {
            msg.append(Component.text(" "))
            msg.append(
                Component.text(it.message)
                    .hoverEvent(HoverEvent.showText(Component.text(it.hover)))
                    .clickEvent(ClickEvent.runCommand(it.command))
            )
        }
        player.sendMessage(msg.build())
    }

    override fun sendClickPageBar(sender: ProxyPlayer, pageBar: String, prev: ClickCommand, next: ClickCommand) {
        val player = sender.cast<Player>()
        val prevArr = pageBar.split("{prev}")
        val nextArr = prevArr[1].split("{next}")
        val prevClick = Component.text(prev.message).toBuilder()
        prevClick.hoverEvent(HoverEvent.showText(Component.text(prev.hover)))
        prevClick.clickEvent(ClickEvent.runCommand(prev.command))
        val nextClick = Component.text(next.message).toBuilder()
        nextClick.hoverEvent(HoverEvent.showText(Component.text(next.hover)))
        nextClick.clickEvent(ClickEvent.runCommand(next.command))

        val message = Component.text(prevArr[0]).toBuilder()
        message.append(prevClick)
        message.append(Component.text(nextArr[0]))
        message.append(nextClick)
        message.append(Component.text(nextArr[1]))
        player.sendMessage(message.build())
    }
}