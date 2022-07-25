package me.zhenxin.zmusic

import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.exception.ZMusicException
import me.zhenxin.zmusic.js.Http
import me.zhenxin.zmusic.js.Util
import me.zhenxin.zmusic.js.evalJS
import me.zhenxin.zmusic.js.nashornSandbox
import me.zhenxin.zmusic.status.getState
import me.zhenxin.zmusic.status.playerState
import me.zhenxin.zmusic.taboolib.extend.registerChannel
import me.zhenxin.zmusic.utils.*
import taboolib.common.platform.Platform.*
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.*
import taboolib.module.metrics.Metrics
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
    lateinit var VERSION_NAME: String

    private const val logo = "" +
            "  ______  __  __                 _        \n" +
            " |___  / |  \\/  |               (_)       \n" +
            "    / /  | \\  / |  _   _   ___   _    ___ \n" +
            "   / /   | |\\/| | | | | | / __| | |  / __|\n" +
            "  / /__  | |  | | | |_| | \\__ \\ | | | (__ \n" +
            " /_____| |_|  |_|  \\__,_| |___/ |_|  \\___|\n"

    override fun onLoad() {
        // 初始化变量
        VERSION_NAME = pluginVersion

        // 初始化JS模块
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

            }
        }
    }

    override fun onEnable() {
        setLocale()
        logo.split("\n").forEach {
            logger.info("&b$it")
        }
        logger.info("\t&6v$VERSION_NAME\tby ZhenXin")

        logger.info(Lang.INIT_LOADING)
        // 注册bStats
        Metrics(7291, VERSION_NAME, BUKKIT)
        Metrics(8864, VERSION_NAME, BUNGEE)
        Metrics(12426, VERSION_NAME, VELOCITY)

        // 注册通信频道
        registerChannel("zmusic:channel")
        registerChannel("allmusic:channel")

        Lang.INIT_LOADED.forEach {
            logger.info(
                it.replace("{0}", VERSION_NAME)
                    .replace("{1}", runningPlatform.name.lowercase())
            )
        }

        submit(async = true) { checkUpdate(console()) }

        submit(async = true) {
            logger.info("&a正在尝试登录网易云音乐...")
            val result = loginNetease()
            logger.info(result.message)
        }

        submit(async = true) {
            // JavaScript Test
            val js = """
                function test() {
                    var result = http.get('https://api.zplu.cc/version?plugin=zmusic&type=snapshot')
                    return result
                }
                test()
            """.trimIndent()
            val result = js.evalJS()
            result?.let { logger.debug(it) }
        }
    }

    override fun onDisable() {
        onlinePlayers().forEach {
            it.getState().player?.cancel()
            playerState.remove(it.name)
        }
    }
}

val logger by lazy { Logger(console()) }