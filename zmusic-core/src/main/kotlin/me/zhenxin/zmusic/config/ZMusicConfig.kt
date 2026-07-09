package me.zhenxin.zmusic.config

/**
 * ZMusic 配置快照。
 *
 * @property language 语言代码
 * @property debug 是否启用调试输出
 * @property channel 插件消息通道名
 * @author 真心
 * @since 5.0.0
 */
data class ZMusicConfig(
    val language: String,
    val debug: Boolean,
    val channel: String
)
