package me.zhenxin.zmusic.utils

import me.zhenxin.zmusic.entity.BridgeMessage
import me.zhenxin.zmusic.entity.BridgeMusicInfo
import me.zhenxin.zmusic.enums.BridgeMessageType
import me.zhenxin.zmusic.module.sendBridgeMessage
import taboolib.common.platform.ProxyPlayer

/**
 * 通信桥
 *
 * @author 真心
 * @since 2022/7/28 11:27
 * @email qgzhenxin@qq.com
 */

fun ProxyPlayer.sendBridgeToast(title: String) {
    val message = BridgeMessage(
        type = BridgeMessageType.TOAST,
        title = title
    )
    val json = message.toJSONObject()
    sendBridgeMessage(json.toString().toByteArray())
}

fun ProxyPlayer.sendBridgeInfo(info: BridgeMusicInfo) {
    val message = BridgeMessage(
        type = BridgeMessageType.INFO,
        info = info
    )
    val json = message.toJSONObject()
    sendBridgeMessage(json.toString().toByteArray())
}