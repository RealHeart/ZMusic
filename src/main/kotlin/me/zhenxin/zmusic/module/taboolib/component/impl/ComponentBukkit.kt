package me.zhenxin.zmusic.module.taboolib.component.impl

import me.zhenxin.adventure.platform.bukkit.BukkitAudiences
import me.zhenxin.zmusic.module.taboolib.component.Component
import org.bukkit.command.CommandSender
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyCommandSender
import taboolib.platform.BukkitPlugin

@Suppress("unused")
@PlatformImplementation(Platform.BUKKIT)
class ComponentBukkit : Component {
    override fun sendMsg(sender: ProxyCommandSender, component: me.zhenxin.adventure.text.Component) {
        val s = sender.cast<CommandSender>()
        val bukkitAudiences = BukkitAudiences.create(BukkitPlugin.getInstance())
        bukkitAudiences.sender(s).sendMessage(component)
    }
}
