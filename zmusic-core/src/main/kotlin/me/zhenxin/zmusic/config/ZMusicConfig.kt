package me.zhenxin.zmusic.config

/**
 * ZMusic 配置快照。
 *
 * @property language 语言代码
 * @property debug 是否启用调试输出
 * @property channel 插件消息通道名
 * @property api WebSocket 后端连接配置
 * @author 真心
 * @since 5.0.0
 */
data class ZMusicConfig(
    val language: String,
    val debug: Boolean,
    val channel: String,
    val api: APIConfig
)

/**
 * Plugin 连接 ZMusic API 的配置。
 *
 * @property enabled 是否启用实时后端连接
 * @property webSocketUrl WebSocket 完整地址
 * @property deviceToken 设备授权流程签发的 device_token
 * @author 真心
 * @since 5.0.0
 */
data class APIConfig(
    val enabled: Boolean,
    val webSocketUrl: String,
    val deviceToken: String
)
