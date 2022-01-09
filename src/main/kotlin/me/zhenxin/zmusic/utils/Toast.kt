package me.zhenxin.zmusic.utils

import me.zhenxin.zmusic.logger
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.Platform
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.runningPlatform
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.sendToast


/**
 * 与显示虚拟成就(Toast)有关的内容
 *
 * @author 舞晗坤
 * @since 2021/9/18 12:16
 * @email whksoft@gmail.com
 */


/**
 * 发送Toast
 * @param msg 信息
 */
fun ProxyPlayer.sendToast(msg: String) {
    if (runningPlatform == Platform.BUKKIT) {
        val player = cast<Player>()
        val list = mutableListOf<Material>()
        if (MinecraftVersion.majorLegacy in 11201..11699) {
            list.addAll(
                listOf(
                    Material.MUSIC_DISC_11,
                    Material.MUSIC_DISC_CAT,
                    Material.MUSIC_DISC_BLOCKS,
                    Material.MUSIC_DISC_CHIRP,
                    Material.MUSIC_DISC_FAR,
                    Material.MUSIC_DISC_MALL,
                    Material.MUSIC_DISC_MELLOHI,
                    Material.MUSIC_DISC_STRAD,
                    Material.MUSIC_DISC_WARD,
                    Material.MUSIC_DISC_WAIT,
                )
            )
            val icon = list[(list.indices).random()]
            player.sendToast(icon, msg)
        }else{
            logger.debug("当前版本: ${MinecraftVersion.runningVersion}(${MinecraftVersion.majorLegacy}) 不支持Toast")
        }
    }
}