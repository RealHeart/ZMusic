package me.zhenxin.zmusic.taboolib.extend.jsonmessage.impl

import me.zhenxin.zmusic.taboolib.extend.jsonmessage.ClickCommand
import me.zhenxin.zmusic.taboolib.extend.jsonmessage.JsonMessage
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyPlayer

/**
 * JSON消息 Bungee 实现
 *
 * @author 真心
 * @since 2021/7/3 14:20
 * @email qgzhenxin@qq.com
 */

@Suppress("unused", "DuplicatedCode", "DEPRECATION")
@PlatformImplementation(Platform.BUNGEE)
class JsonMessageBungee : JsonMessage {

    override fun sendClickMessage(sender: ProxyPlayer, base: String, commands: Array<ClickCommand>) {
        val player = sender.cast<ProxiedPlayer>()
        val msg = TextComponent(base)
        commands.forEach {
            msg.addExtra(" ")
            val click = TextComponent(it.message)
            click.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder(it.hover).create())
            click.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, it.command)
            msg.addExtra(click)
        }
        player.sendMessage(msg)
    }

    override fun sendClickPageBar(sender: ProxyPlayer, pageBar: String, prev: ClickCommand, next: ClickCommand) {
        val player = sender.cast<ProxiedPlayer>()
        val prevArr = pageBar.split("{prev}")
        val nextArr = prevArr[1].split("{next}")
        val prevClick = TextComponent(prev.message)
        prevClick.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder(prev.hover).create())
        prevClick.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, prev.command)
        val nextClick = TextComponent(next.message)
        nextClick.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder(next.hover).create())
        nextClick.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, next.command)

        val message = TextComponent(prevArr[0])
        message.addExtra(prevClick)
        message.addExtra(nextArr[0])
        message.addExtra(nextClick)
        message.addExtra(nextArr[1])
        player.sendMessage(message)
    }
}