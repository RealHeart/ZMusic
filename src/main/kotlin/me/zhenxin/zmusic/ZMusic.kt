package me.zhenxin.zmusic

import me.zhenxin.zmusic.module.Config
import me.zhenxin.zmusic.module.Language
import me.zhenxin.zmusic.module.Logger
import me.zhenxin.zmusic.module.taboolib.registerChannel
import taboolib.common.platform.Platform
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.runningPlatform
import taboolib.common.platform.function.submit
import taboolib.module.chat.HexColor
import taboolib.module.metrics.Metrics


/**
 * ZMusic 主类
 *
 * @author 真心
 * @since 2021/8/14 14:33
 * @email qgzhenxin@qq.com
 */
object ZMusic : Plugin() {
    lateinit var VERSION: String
    lateinit var PLATFORM: Platform

    override fun onLoad() {
        println("test")
    }

    override fun onEnable() {
        // 初始化变量
        VERSION = pluginVersion
        PLATFORM = runningPlatform

        // 初始化日志模块
        logger = Logger(console())

        Config.init() // 加载配置
        Language.init(console()) // 初始化语言系统
        logger.log(Language.LOADING)
        submit {
            logger.log("跨平台任务接口测试")
        }
        // 注册bStats
        Metrics(7291, pluginVersion, Platform.BUKKIT)
        Metrics(8864, pluginVersion, Platform.BUNGEE)
        Metrics(12426, pluginVersion, Platform.VELOCITY)
        Metrics(12457, pluginVersion, Platform.SPONGE_API_7)

        // 注册通信频道
        console().registerChannel("zmusic:channel")

        logger.debug(runningPlatform.name)
    }
}

lateinit var logger: Logger