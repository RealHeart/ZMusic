package me.zhenxin.zmusic.module.taboolib.component.impl

import com.velocitypowered.api.command.CommandSource
import me.zhenxin.zmusic.module.taboolib.component.Component
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyCommandSender

@Suppress("unused")
@PlatformImplementation(Platform.VELOCITY)
class ComponentVelocity : Component {

    override fun sendMsg(sender: ProxyCommandSender, component: net.kyori.adventure.text.Component) {
        val s = sender.cast<CommandSource>()
        s.sendMessage(component)
    }
}
