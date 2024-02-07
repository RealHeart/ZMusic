package me.zhenxin.zmusic.config

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.dataFolder
import me.zhenxin.zmusic.logger

/**
 * 配置文件
 *
 * @author 真心
 * @since 2023/7/24 11:06
 * @email qgzhenxin@qq.com
 */
object Config {
    /**
     * 配置文件版本
     */
    val version: Int
        get() = config.get("version") ?: 0

    /**
     * 调试模式
     *
     */
    val debug: Boolean
        get() = config.get("debug") ?: false

    /**
     * 检查更新
     */
    val checkUpdate: Boolean
        get() = config.get("check-update") ?: true

    /**
     * 语言
     */
    val language: String
        get() = config.get("language") ?: "auto"

    /**
     * 消息前缀
     */
    val prefix: String
        get() = config.get("prefix") ?: "&bZMusic &e>>> &r"

    /**
     * 网易云音乐API
     */
    val neteaseApi: String
        get() = config.get("api.netease-api") ?: ""

    /** VIP QQ
     *
     */
    val vipQQ: String
        get() = config.get("vip.qq") ?: ""

    /**
     *  VIP Key
     */
    val vipKey: String
        get() = config.get("vip.key") ?: ""

    /**
     * 启用代理
     */
    val proxyEnable: Boolean
        get() = config.get("proxy.enable") ?: false

    /**
     * 代理类型
     */
    val proxyType: String
        get() = config.get("proxy.type") ?: "HTTP"

    /**
     * 代理主机
     */
    val proxyHost: String
        get() = config.get("proxy.host") ?: ""

    /**
     * 代理端口
     */
    val proxyPort: Int
        get() = config.get("proxy.port") ?: 0
}

fun initConfig() {
    val configPath = dataFolder.resolve("config.toml")
    if (!configPath.exists()) {
        val stream = ZMusic::class.java.getResourceAsStream("/config.toml")
        stream?.copyTo(configPath.outputStream())
    }

    config = CommentedFileConfig.builder(configPath).autoreload().charset(Charsets.UTF_8).build()
    (config as CommentedFileConfig).load()

    val currentVersion = 15
    if (Config.version != currentVersion) {
        logger.error("Config is outdated, please delete the config file and restart the server.")
        // TODO: disablePlugin()
        return
    } else {
        logger.info("Configuration is initialized.")
    }
}

private var config: CommentedConfig = CommentedConfig.inMemory()