package me.zhenxin.zmusic.taboolib.extend.component.impl

import me.zhenxin.zmusic.platform.bukkitPlugin
import me.zhenxin.zmusic.taboolib.extend.component.Component
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.command.CommandSender
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyCommandSender

@Suppress("unused")
@PlatformImplementation(Platform.BUKKIT)
class ComponentBukkit : Component {
    override fun sendMsg(sender: ProxyCommandSender, component: net.kyori.adventure.text.Component) {
        val s = sender.cast<CommandSender>()
        val bukkitAudiences = BukkitAudiences.create(bukkitPlugin)
        bukkitAudiences.sender(s).sendMessage(component)
    }
}
