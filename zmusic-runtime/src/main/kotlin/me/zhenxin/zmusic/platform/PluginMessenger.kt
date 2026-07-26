package me.zhenxin.zmusic.platform

import me.zhenxin.zmusic.platform.entity.ZPlayer

/**
 * 插件消息通道网关。
 *
 * @author 真心
 * @since 5.0.0
 */
interface PluginMessenger {
    /**
     * 注册通道。
     *
     * @param channel 通道名
     */
    fun registerChannel(channel: String, listener: PluginMessageListener)

    /**
     * 注销通道。
     *
     * @param channel 通道名
     */
    fun unregisterChannel(channel: String)

    /**
     * 发送原始消息。
     *
     * @param player 目标玩家
     * @param channel 通道名
     * @param payload 原始消息内容
     */
    fun send(player: ZPlayer, channel: String, payload: ByteArray)
}

/**
 * 客户端 Mod 发给服务端 Plugin 的消息回调。
 *
 * @author 真心
 * @since 5.0.0
 */
fun interface PluginMessageListener {
    /**
     * 处理一条玩家插件消息。
     *
     * @param player 消息来源玩家
     * @param payload 原始协议帧
     */
    fun onMessage(player: ZPlayer, payload: ByteArray)
}
