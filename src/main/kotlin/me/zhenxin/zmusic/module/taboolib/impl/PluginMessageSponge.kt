package me.zhenxin.zmusic.module.taboolib.impl

import me.zhenxin.zmusic.module.taboolib.PluginMessage
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyPlayer
import taboolib.platform.Sponge7Plugin

/**
 * 插件消息 Sponge 实现
 *
 * @author 真心
 * @since 2021/8/22 15:34
 * @email qgzhenxin@qq.com
 */

@Suppress("unused")
@PlatformImplementation(Platform.SPONGE_API_7)
class PluginMessageSponge : PluginMessage {
    private val plugin by lazy { Sponge7Plugin.getInstance() }
    override fun registerChannel(channel: String) {
        Sponge.getChannelRegistrar().createChannel(plugin, channel)
    }

    override fun sendMessage(sender: ProxyPlayer, channel: String, data: ByteArray) {
        val player by lazy { sender.cast<Player>() }
        player.messageChannel.send(Text.of(encodeBytes(data)))
    }
}