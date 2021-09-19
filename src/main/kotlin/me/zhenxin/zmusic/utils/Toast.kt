package me.zhenxin.zmusic.utils

import me.zhenxin.zmusic.logger
import org.bukkit.Material
import taboolib.common.platform.Platform
import taboolib.common.platform.function.runningPlatform


/**
 * 与显示虚拟成就(Toast)有关的类
 *
 * @author 舞晗坤
 * @since 2021/9/18 12:16
 * @email whksoft@gmail.com
 */
class Toast() {
    lateinit var icons: List<Material>

    init {
        logger.debug("初始化Toast图标")
        if (runningPlatform == Platform.BUKKIT) {
            icons = listOf(
                Material.valueOf("MUSIC_DISC_11"),
                Material.valueOf("MUSIC_DISC_CAT"),
                Material.valueOf("MUSIC_DISC_BLOCKS"),
                Material.valueOf("MUSIC_DISC_CHIRP"),
                Material.valueOf("MUSIC_DISC_FAR"),
                Material.valueOf("MUSIC_DISC_MALL"),
                Material.valueOf("MUSIC_DISC_MELLOHI"),
                Material.valueOf("MUSIC_DISC_STRAD"),
                Material.valueOf("MUSIC_DISC_WARD"),
                Material.valueOf("MUSIC_DISC_WAIT"),
            )
        }
    }
}