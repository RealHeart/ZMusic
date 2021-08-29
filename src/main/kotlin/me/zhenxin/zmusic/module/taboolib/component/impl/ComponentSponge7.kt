package me.zhenxin.zmusic.module.taboolib.component.impl

import com.google.inject.Inject
import me.zhenxin.zmusic.module.taboolib.component.Component
import net.kyori.adventure.platform.spongeapi.SpongeAudiences
import org.spongepowered.api.command.CommandSource
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyCommandSender

@Suppress("unused")
@PlatformImplementation(Platform.SPONGE_API_7)
class ComponentSponge7 @Inject constructor(private var spongeAudiences: SpongeAudiences) : Component {

    private lateinit var adventure: SpongeAudiences
    override fun sendMsg(sender: ProxyCommandSender, component: net.kyori.adventure.text.Component) {
        val s = sender.cast<CommandSource>()
        spongeAudiences.receiver(s).sendMessage(component)
    }
}
