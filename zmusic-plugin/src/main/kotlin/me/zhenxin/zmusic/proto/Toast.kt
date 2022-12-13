package me.zhenxin.zmusic.proto

import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.proto.packet.impl.*
import me.zhenxin.zmusic.utils.nmsVersion
import me.zhenxin.zmusic.utils.sendBridgeToast
import taboolib.common.platform.Platform
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.runningPlatform

/**
 * 吐司
 *
 * @author 真心
 * @since 2022/6/9 3:45
 */

/**
 * 发送Toast
 * @param title 信息
 */
fun ProxyPlayer.sendToast(title: String) {
    when (runningPlatform) {
        Platform.BUKKIT -> {
            val packet = when (nmsVersion()) {
                "v1_19_R2" -> AdvancementPacket_1_19_R2(cast(), title)
                "v1_19_R1" -> AdvancementPacket_1_19_R1(cast(), title)
                "v1_18_R2" -> AdvancementPacket_1_18_R2(cast(), title)
                "v1_18_R1" -> AdvancementPacket_1_18_R1(cast(), title)
                "v1_17_R1" -> AdvancementPacket_1_17_R1(cast(), title)
                "v1_16_R3" -> AdvancementPacket_1_16_R3(cast(), title)
                "v1_16_R2" -> AdvancementPacket_1_16_R2(cast(), title)
                "v1_16_R1" -> AdvancementPacket_1_16_R1(cast(), title)
                "v1_15_R1" -> AdvancementPacket_1_15_R1(cast(), title)
                "v1_14_R1" -> AdvancementPacket_1_14_R1(cast(), title)
                "v1_13_R2" -> AdvancementPacket_1_13_R2(cast(), title)
                "v1_12_R1" -> AdvancementPacket_1_12_R1(cast(), title)
                else -> {
                    logger.debug("Toast not support this NMS version: ${nmsVersion()}")
                    return;
                }
            }
            packet.grant()
            packet.revoke()
        }

        Platform.BUNGEE, Platform.VELOCITY -> sendBridgeToast(title)
        else -> {}
    }
}