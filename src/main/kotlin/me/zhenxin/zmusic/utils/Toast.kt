package me.zhenxin.zmusic.utils

import me.zhenxin.zmusic.logger
import org.bukkit.Material
import taboolib.common.platform.Platform
import taboolib.common.platform.function.runningPlatform


/**
 * 与显示虚拟成就(Toast)有关的内容
 *
 * @author 舞晗坤
 * @since 2021/9/18 12:16
 * @email whksoft@gmail.com
 */

/**
 * 随机获取一个Toast图标
 */
val toastIcon: Material
    get() {
        logger.debug("随机Toast图标")
        return if (runningPlatform == Platform.BUKKIT) {
            val list = listOf(
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
            list[(list.indices).random()]
        } else Material.AIR
    }