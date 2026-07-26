package me.zhenxin.zmusic.velocity

import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.PluginMessageEvent
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.PluginMessageListener
import me.zhenxin.zmusic.platform.entity.ZPlayer

/**
 * Velocity 插件消息实现。
 *
 * @property server Velocity 代理服务
 * @author 真心
 * @since 5.0.0
 */
class VelocityPluginMessenger(
    private val server: ProxyServer,
    plugin: Any
) : PluginMessenger {
    private val channels = mutableMapOf<String, MinecraftChannelIdentifier>()
    private val listeners = mutableMapOf<String, PluginMessageListener>()

    init {
        server.eventManager.register(plugin, this)
    }

    /**
     * 注册插件消息通道。
     *
     * @param channel 通道名
     */
    override fun registerChannel(channel: String, listener: PluginMessageListener) {
        channels.getOrPut(channel) {
            MinecraftChannelIdentifier.from(channel).also(server.channelRegistrar::register)
        }
        listeners[channel] = listener
    }

    /**
     * 注销插件消息通道。
     *
     * @param channel 通道名
     */
    override fun unregisterChannel(channel: String) {
        channels.remove(channel)?.let(server.channelRegistrar::unregister)
        listeners.remove(channel)
    }

    /**
     * 通过代理持有的玩家连接直接发送插件消息。
     *
     * @param player 目标玩家
     * @param channel 通道名
     * @param payload 消息内容
     */
    override fun send(player: ZPlayer, channel: String, payload: ByteArray) {
        val velocityPlayer = (player as? VelocityPlayer)?.platformPlayer() ?: return
        val identifier = channels[channel] ?: return
        velocityPlayer.sendPluginMessage(identifier, payload)
    }

    /**
     * 接收客户端发往代理端的 ZMusic 插件消息。
     *
     * @param event Velocity 插件消息事件
     */
    @Subscribe
    fun onPluginMessage(event: PluginMessageEvent) {
        val listener = listeners[event.identifier.id] ?: return
        val player = event.source as? Player ?: return
        event.result = PluginMessageEvent.ForwardResult.handled()
        listener.onMessage(VelocityPlayer(player), event.data)
    }
}
