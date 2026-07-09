package me.zhenxin.zmusic.platform

/**
 * 平台命令注册器。
 *
 * @author 真心
 * @since 5.0.0
 */
fun interface CommandRegistry {
    /**
     * 注册 ZMusic 命令处理器。
     *
     * @param platformCommand 命令处理器
     */
    fun register(platformCommand: PlatformCommand)
}
