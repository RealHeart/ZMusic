package me.zhenxin.zmusic

import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.module.Logger
import me.zhenxin.zmusic.module.Tasker
import java.io.File
import java.net.CookieHandler
import java.net.CookieManager

object ZMusic {

    var plugin = Any()

    var isBC = false
    var isVIP = false
    var isEnable: Boolean = true
    var isEnabled: Boolean = false

    var zLibIsOK: Boolean = true
    val zLibVer = "1.0"

    lateinit var logger: Logger
    lateinit var tasker: Tasker

    lateinit var dataFolder: File

    lateinit var thisVer: String
    var thisVerCode = 202101060

    fun enable() {
        if (!dataFolder.exists()) dataFolder.mkdir()
        val lang = File(dataFolder, "/language/")
        if (!lang.exists()) lang.mkdir()
        ("  ______  __  __                 _        \n" +
                " |___  / |  \\/  |               (_)       \n" +
                "    / /  | \\  / |  _   _   ___   _    ___ \n" +
                "   / /   | |\\/| | | | | | / __| | |  / __|\n" +
                "  / /__  | |  | | | |_| | \\__ \\ | | | (__ \n" +
                " /_____| |_|  |_|  \\__,_| |___/ |_|  \\___|\n").split("\n")
            .forEach { logger.log("§b$it") }
        logger.log("\t§6v$thisVer($thisVerCode)\tby ZhenXin")
        logger.log("")
        logger.log(Lang.Loading.loading)
        val manager = CookieManager()
        CookieHandler.setDefault(manager)
        Config.load()
        logger.log(Lang.Loading.loaded)
        isEnabled = true
    }
}
