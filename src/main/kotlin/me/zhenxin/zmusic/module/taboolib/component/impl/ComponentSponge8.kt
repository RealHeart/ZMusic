package me.zhenxin.zmusic.module.taboolib.component.impl

import me.zhenxin.zmusic.module.taboolib.component.Component
import net.kyori.adventure.audience.Audience
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyCommandSender

@Suppress("unused")
@PlatformImplementation(Platform.SPONGE_API_8)
class ComponentSponge8 : Component {

    override fun sendMsg(sender: ProxyCommandSender, component: net.kyori.adventure.text.Component) {
        val s = sender.cast<Audience>()
        s.sendMessage(component)
    }
}
