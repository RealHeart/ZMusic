package me.zhenxin.zmusic.platform

import java.io.File

/**
 * core 需要的平台能力集合。
 *
 * @author 真心
 * @since 5.0.0
 */
interface PlatformContext {
    /** 当前平台。 */
    val platform: Platform
    /** 当前服务端或代理端的平台版本。 */
    val platformVersion: String
    /** 插件数据目录。 */
    val dataFolder: File
    /** 日志适配器。 */
    val logger: PlatformLogger
    /** 命令注册器。 */
    val commandRegistry: CommandRegistry
    /** 玩家查询网关。 */
    val players: PlayerGateway
    /** 插件消息网关。 */
    val pluginMessenger: PluginMessenger
    /** 任务调度器。 */
    val scheduler: Scheduler
}
