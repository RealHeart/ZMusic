package me.zhenxin.zmusic.entity

import me.zhenxin.zmusic.enums.BridgeMessageType

/**
 * 通信Toast消息
 *
 * @author 真心
 * @since 2022/7/28 11:31
 * @email qgzhenxin@qq.com
 */
data class BridgeMessage(
    val type: BridgeMessageType? = null,
    val info: BridgeMusicInfo? = null,
    val title: String? = null,
)
