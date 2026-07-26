package me.zhenxin.zmusic.config

import com.electronwill.nightconfig.core.file.CommentedFileConfig
import java.io.File

/**
 * 读取和初始化 ZMusic 配置。
 *
 * @property dataFolder 插件数据目录
 * @author 真心
 * @since 5.0.0
 */
class ConfigLoader(private val dataFolder: File) {
    /**
     * 加载配置文件；不存在时先写入默认配置。
     *
     * @return 配置快照
     */
    fun load(): ZMusicConfig {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }

        val file = dataFolder.resolve("config.toml")
        if (!file.exists()) {
            file.writeText(DEFAULT_CONFIG)
        }

        CommentedFileConfig.builder(file).charset(Charsets.UTF_8).build().use { config ->
            config.load()
            return ZMusicConfig(
                language = config.get<String>("language") ?: "zh-CN",
                debug = config.get<Boolean>("debug") ?: false,
                channel = config.get<String>("plugin-message.channel") ?: "zmusic:packet",
                api = APIConfig(
                    enabled = config.get<Boolean>("api.enabled") ?: false,
                    webSocketUrl = config.get<String>("api.websocket-url") ?: "ws://localhost:8389/api/v1/plugin/ws",
                    deviceToken = config.get<String>("api.device-token") ?: ""
                )
            )
        }
    }

    private companion object {
        /** 首次启动写入的默认配置。 */
        private const val DEFAULT_CONFIG = """
language = "zh-CN"
debug = false

[plugin-message]
channel = "zmusic:packet"

[api]
enabled = false
websocket-url = "ws://localhost:8389/api/v1/plugin/ws"
device-token = ""
"""
    }
}
