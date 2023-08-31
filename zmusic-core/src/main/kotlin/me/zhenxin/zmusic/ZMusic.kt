package me.zhenxin.zmusic

import me.zhenxin.zmusic.config.initConfig
import me.zhenxin.zmusic.platform.Logger
import java.io.File

/**
 * ZMusic
 *
 * @author 真心
 * @since 2023/5/23 16:57
 * @email qgzhenxin@qq.com
 */
object ZMusic {

    private const val logo = "" +
            "  ______  __  __                 _        \n" +
            " |___  / |  \\/  |               (_)       \n" +
            "    / /  | \\  / |  _   _   ___   _    ___ \n" +
            "   / /   | |\\/| | | | | | / __| | |  / __|\n" +
            "  / /__  | |  | | | |_| | \\__ \\ | | | (__ \n" +
            " /_____| |_|  |_|  \\__,_| |___/ |_|  \\___|\n"

    /**
     * 版本号
     */
    const val VERSION_NAME = "#VERSION_NAME#"

    /**
     * 插件启用
     */
    fun onEnable() {
        logo.split("\n").forEach { logger.info("&b$it") }
        logger.info("\t&6v$VERSION_NAME\tby ZhenXin")
        logger.info("")
        logger.info("ZMusic is loading...")

        initConfig()

        logger.info("ZMusic is enabled.")
    }

    /**
     * 插件禁用
     */
    fun onDisable() {
        logger.info("ZMusic is disabled.")
    }
}

/**
 * 插件数据文件夹
 */
lateinit var dataFolder: File

/**
 * 日志
 */
lateinit var logger: Logger
