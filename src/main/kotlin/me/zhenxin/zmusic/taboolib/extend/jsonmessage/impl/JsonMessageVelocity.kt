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
        val msg = Component.text(base)
        commands.forEach {
            msg.append(Component.text(" "))
            val click = Component.text(it.message)
            click.hoverEvent(HoverEvent.showText(Component.text(it.hover)))
            click.clickEvent(ClickEvent.runCommand(it.command))
            msg.append(click)
        }
        player.sendMessage(msg)
    }

    override fun sendClickPageBar(sender: ProxyPlayer, pageBar: String, prev: ClickCommand, next: ClickCommand) {
        val player = sender.cast<Player>()
        val prevArr = pageBar.split("{prev}")
        val nextArr = prevArr[1].split("{next}")
        val prevClick = Component.text(prev.message)
        prevClick.hoverEvent(HoverEvent.showText(Component.text(prev.hover)))
        prevClick.clickEvent(ClickEvent.runCommand(prev.command))
        val nextClick = Component.text(next.message)
        nextClick.hoverEvent(HoverEvent.showText(Component.text(next.hover)))
        nextClick.clickEvent(ClickEvent.runCommand(next.command))

        val message = Component.text(prevArr[0])
        message.append(prevClick)
        message.append(Component.text(prevArr[1]))
        message.append(nextClick)
        message.append(Component.text(nextArr[1]))
        player.sendMessage(message)
    }
}