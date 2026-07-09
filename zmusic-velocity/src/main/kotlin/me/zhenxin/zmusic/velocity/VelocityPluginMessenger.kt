package me.zhenxin.zmusic.velocity

import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.entity.ZPlayer

/**
 * Velocity 插件消息实现。
 *
 * @property server Velocity 代理服务
 * @author 真心
 * @since 5.0.0
 */
class VelocityPluginMessenger(private val server: ProxyServer) : PluginMessenger {
    private val channels = mutableMapOf<String, MinecraftChannelIdentifier>()

    /**
     * 注册插件消息通道。
     *
     * @param channel 通道名
     */
    override fun registerChannel(channel: String) {
        channels.getOrPut(channel) {
            MinecraftChannelIdentifier.from(channel).also(server.channelRegistrar::register)
        }
    }

    /**
     * 注销插件消息通道。
     *
     * @param channel 通道名
     */
    override fun unregisterChannel(channel: String) {
        channels.remove(channel)?.let(server.channelRegistrar::unregister)
    }

    /**
     * 通过玩家当前连接的后端服务器发送插件消息。
     *
     * @param player 目标玩家
     * @param channel 通道名
     * @param payload 消息内容
     */
    override fun send(player: ZPlayer, channel: String, payload: ByteArray) {
        val velocityPlayer = (player as? VelocityPlayer)?.platformPlayer() ?: return
        val identifier = channels[channel] ?: return
        velocityPlayer.currentServer.ifPresent { serverConnection ->
            serverConnection.sendPluginMessage(identifier, payload)
        }
    }
}
