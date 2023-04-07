package me.zhenxin.zmusic

import me.zhenxin.zmusic.config.Config
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.exception.ZMusicException
import me.zhenxin.zmusic.login.NeteaseLogin
import me.zhenxin.zmusic.module.PlaceholderAPI
import me.zhenxin.zmusic.module.js.Http
import me.zhenxin.zmusic.module.js.Util
import me.zhenxin.zmusic.module.js.nashornSandbox
import me.zhenxin.zmusic.module.taboolib.registerChannel
import me.zhenxin.zmusic.status.playerState
import me.zhenxin.zmusic.utils.Logger
import me.zhenxin.zmusic.utils.checkUpdate
import me.zhenxin.zmusic.utils.initCookie
import me.zhenxin.zmusic.utils.isVip
import taboolib.common.platform.Platform.*
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.*
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin
import java.io.File


/**
 * ZMusic 主类
 *
 * @author 真心
 * @since 2021/8/14 14:33
 * @email qgzhenxin@qq.com
 */
@Suppress("unused")
object ZMusic : Plugin() {

    private const val logo = "" +
            "  ______  __  __                 _        \n" +
            " |___  / |  \\/  |               (_)       \n" +
            "    / /  | \\  / |  _   _   ___   _    ___ \n" +
            "   / /   | |\\/| | | | | | / __| | |  / __|\n" +
            "  / /__  | |  | | | |_| | \\__ \\ | | | (__ \n" +
            " /_____| |_|  |_|  \\__,_| |___/ |_|  \\___|\n"

    override fun onLoad() {
        // 初始化变量
        ZMusicData.VERSION_NAME = pluginVersion
        logger = Logger(console())
    }

    override fun onEnable() {
        logo.split("\n").forEach {
            logger.info("&b$it")
        }
        logger.info("\t&6v${ZMusicData.VERSION_NAME}\tby ZhenXin")

        logger.info(Lang.INIT_LOADING)

        // 注册bStats
        Metrics(7291, ZMusicData.VERSION_NAME, BUKKIT)
        Metrics(8864, ZMusicData.VERSION_NAME, BUNGEE)
        Metrics(12426, ZMusicData.VERSION_NAME, VELOCITY)

        // Bukkit相关
        if (runningPlatform == BUKKIT) {
            // PAPI
            val enabled = BukkitPlugin.getInstance()
                .server
                .pluginManager
                .isPluginEnabled("PlaceholderAPI")
            if (enabled) {
                PlaceholderAPI().register()
            }
        }

        // 注册通信频道
        registerChannel("zmusic:channel")
        registerChannel("allmusic:channel")

        // 初始化目录/文件
        initDirAndFiles()

        // Cookie初始化
        initCookie()

        // 初始化JS模块
        initJavaScript()

        Lang.INIT_LOADED.forEach {
            logger.info(
                it.replace("{0}", ZMusicData.VERSION_NAME)
                    .replace("{1}", runningPlatform.name.lowercase())
            )
        }

        if (Config.CHECK_UPDATE) {
            submit { checkUpdate(console()) }
        }

        submit {
            if (NeteaseLogin.isLogin()) {
                NeteaseLogin.refresh(console())
            } else {
                NeteaseLogin.guest(console())
            }
        }

        submit {
            ZMusicData.IS_VIP = isVip()
        }
    }

    override fun onDisable() {
        playerState.values.forEach {
            it.player?.stop()
        }
        playerState.clear()
    }

    private fun initDirAndFiles() {
        val dataDir = File(getDataFolder(), "data")
        if (!dataDir.exists()) {
            dataDir.mkdirs()
        }
        val cookies = File(getDataFolder(), "data/cookies.json")
        if (!cookies.exists()) {
            cookies.createNewFile()
        }
    }

    private fun initJavaScript() {
        nashornSandbox.allow(Http::class.java)
        nashornSandbox.allow(Util::class.java)
        nashornSandbox.inject("http", Http())
        nashornSandbox.inject("util", Util())

        val files = listOf(
            "scripts/platform_example.js"
        )

        files.forEach {
            if (it.contains("example")) {
                releaseResourceFile(it, true)
            } else {
                releaseResourceFile(it)
            }
        }

        val scripts = File(getDataFolder(), "scripts").listFiles() ?: throw ZMusicException("scripts path not found")
        scripts.forEach {
            if (it.name.contains("platform_") || !it.name.contains("example")) {
                // TODO: 音乐平台扩展
                logger.debug("TODO: 音乐平台扩展")
            }
        }
    }
}

lateinit var logger: Logger