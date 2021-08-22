package me.zhenxin.zmusic

import me.zhenxin.zmusic.module.Config
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.Logger
import me.zhenxin.zmusic.module.taboolib.registerChannel
import taboolib.common.platform.Platform.*
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.runningPlatform
import taboolib.common.platform.function.submit
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
    const val logo = ""
    override fun onEnable() {
        // 初始化变量
        VERSION = pluginVersion

        // 初始化日志模块
        logger = Logger(console())

        Config.init() // 加载配置
        Lang.init(console()) // 初始化语言系统
        logger.log(Lang.INIT_LOADING)
        submit {
            logger.log("跨平台任务接口测试")
        }
        // 注册bStats
        Metrics(7291, VERSION, BUKKIT)
        Metrics(8864, VERSION, BUNGEE)
        Metrics(12426, VERSION, VELOCITY)
        Metrics(12457, VERSION, SPONGE_API_7)

        // 注册通信频道
        console().registerChannel("zmusic:channel")
        console().registerChannel("allmusic:channel")

        logger.debug(runningPlatform.name)
        Lang.INIT_LOADED.forEach {
            logger.log(
                it.replace("{0}", VERSION)
                    .replace("{1}", runningPlatform.name.lowercase())
            )
        }
    }
}

lateinit var logger: Logger