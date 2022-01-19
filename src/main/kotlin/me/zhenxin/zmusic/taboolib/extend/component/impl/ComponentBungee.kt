package me.zhenxin.zmusic.taboolib.extend.component.impl

import me.zhenxin.adventure.platform.bungeecord.BungeeAudiences
import me.zhenxin.zmusic.platform.bungeePlugin
import me.zhenxin.zmusic.taboolib.extend.component.Component
import net.md_5.bungee.api.CommandSender
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyCommandSender

@Suppress("unused")
@PlatformImplementation(Platform.BUNGEE)
class ComponentBungee : Component {
    override fun sendMsg(sender: ProxyCommandSender, component: me.zhenxin.adventure.text.Component) {
        val s = sender.cast<CommandSender>()
        val bungeeAudiences = BungeeAudiences.create(bungeePlugin)
        bungeeAudiences.sender(s).sendMessage(component)
    }
}
