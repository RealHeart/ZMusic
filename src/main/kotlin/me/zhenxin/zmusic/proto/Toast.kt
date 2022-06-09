package me.zhenxin.zmusic.proto

import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.platform.bukkitPlugin
import me.zhenxin.zmusic.proto.packet.impl.*
import me.zhenxin.zmusic.utils.colored
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
 * @param msg 信息
 */
fun ProxyPlayer.sendToast(msg: String) {
    if (runningPlatform == Platform.BUKKIT) {
        val pkgName = bukkitPlugin.server.javaClass.`package`.name
        val nms = pkgName.substring(pkgName.lastIndexOf('.') + 1)
        val packet = when (nms) {
            "v1_19_R1" -> AdvancementPacket_1_19_R1(cast(), msg)
            "v1_18_R2" -> AdvancementPacket_1_18_R2(cast(), msg)
            "v1_18_R1" -> AdvancementPacket_1_18_R1(cast(), msg)
            "v1_17_R1" -> AdvancementPacket_1_17_R1(cast(), msg)
            "v1_16_R3" -> AdvancementPacket_1_16_R3(cast(), msg)
            "v1_16_R2" -> AdvancementPacket_1_16_R2(cast(), msg)
            "v1_16_R1" -> AdvancementPacket_1_16_R1(cast(), msg)
            "v1_15_R1" -> AdvancementPacket_1_15_R1(cast(), msg)
            "v1_14_R1" -> AdvancementPacket_1_14_R1(cast(), msg)
            "v1_13_R2" -> AdvancementPacket_1_13_R2(cast(), msg)
            "v1_12_R1" -> AdvancementPacket_1_12_R1(cast(), msg)
            else -> {
                logger.info("&cToast not support this NMS version: $nms".colored())
                return;
            }
        }
        packet.grant()
        packet.revoke()
    }
}