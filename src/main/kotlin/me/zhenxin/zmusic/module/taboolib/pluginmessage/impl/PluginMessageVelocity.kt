package me.zhenxin.zmusic.module.taboolib.pluginmessage.impl

import com.velocitypowered.api.proxy.Player
import me.zhenxin.zmusic.module.taboolib.pluginmessage.PluginMessage
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformImplementation
import taboolib.common.platform.ProxyPlayer
import taboolib.platform.VelocityPlugin

/**
 * 插件消息 Velocity 实现
 *
 * @author 真心
 * @since 2021/8/22 15:28
 * @email qgzhenxin@qq.com
 */

@Suppress("unused")
@PlatformImplementation(Platform.VELOCITY)
class PluginMessageVelocity : PluginMessage {
    private val plugin by lazy { VelocityPlugin.getInstance() }
    override fun registerChannel(channel: String) {
        plugin.server.channelRegistrar.register({ channel })
    }

    override fun sendMessage(sender: ProxyPlayer, channel: String, data: ByteArray) {
        val player by lazy { sender.cast<Player>() }
        player.sendPluginMessage({ channel }, encodeBytes(data))
    }
}