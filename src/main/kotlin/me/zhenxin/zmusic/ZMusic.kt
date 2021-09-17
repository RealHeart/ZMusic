package me.zhenxin.zmusic

import me.zhenxin.zmusic.module.Config
import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.Logger
import me.zhenxin.zmusic.module.taboolib.registerChannel
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform.*
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.runningPlatform
import taboolib.module.metrics.Metrics
import java.util.*


/**
 * ZMusic 主类
 *
 * @author 真心
 * @since 2021/8/14 14:33
 * @email qgzhenxin@qq.com
 */
@Suppress("unused")
object ZMusic {
    lateinit var VERSION: String

    val PLATFORMS = listOf(
        "netease",
        "qq",
        "bilibili",
        "kugou",
        "xima",
        "soundcloud"
    )
    private const val logo = ""

    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        // 初始化变量
        VERSION = pluginVersion

        // 初始化日志模块
        logger = Logger(console())
    }

    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        Config.init() // 加载配置

        try {
            val lang = Config.LANGUAGE.split("_")
            Locale.setDefault(Locale(lang[0], lang[1]))
        } catch (e: Exception) {
        }

        Lang.init(console()) // 初始化语言系统
        logger.info(Lang.INIT_LOADING)
        // 注册bStats
        Metrics(7291, VERSION, BUKKIT)
        Metrics(8864, VERSION, BUNGEE)
        Metrics(12426, VERSION, VELOCITY)
        Metrics(12457, VERSION, SPONGE_API_7)

        // 注册通信频道
        registerChannel("zmusic:channel")
        registerChannel("allmusic:channel")

        Lang.INIT_LOADED.forEach {
            logger.info(
                it.replace("{0}", VERSION)
                    .replace("{1}", runningPlatform.name.lowercase())
            )
        }
    }
}

lateinit var logger: Logger