package me.zhenxin.zmusic.module.taboolib.component.impl

import me.zhenxin.adventure.platform.bungeecord.BungeeAudiences
import me.zhenxin.zmusic.module.taboolib.component.Component
import net.md_5.bungee.api.CommandSender
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyCommandSender
import taboolib.platform.BungeePlugin

@Suppress("unused")
@PlatformImplementation(Platform.BUNGEE)
class ComponentBungee : Component {
    override fun sendMsg(sender: ProxyCommandSender, component: me.zhenxin.adventure.text.Component) {
        val s = sender.cast<CommandSender>()
        val bungeeAudiences = BungeeAudiences.create(BungeePlugin.getInstance())
        bungeeAudiences.sender(s).sendMessage(component)
    }
}
