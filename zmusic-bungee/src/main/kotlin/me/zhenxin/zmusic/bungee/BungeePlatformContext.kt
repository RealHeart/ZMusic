package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.platform.CommandRegistry
import me.zhenxin.zmusic.platform.Platform
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.PlatformLogger
import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.Scheduler
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

/**
 * BungeeCord 平台能力实现。
 *
 * @property plugin BungeeCord 插件实例
 * @author 真心
 * @since 5.0.0
 */
class BungeePlatformContext(private val plugin: Plugin) : PlatformContext {
    /** 当前平台类型。 */
    override val platform: Platform = Platform.BUNGEE
    /** BungeeCord 插件数据目录。 */
    override val dataFolder: File = plugin.dataFolder
    /** BungeeCord logger 适配器。 */
    override val logger: PlatformLogger = BungeePlatformLogger(plugin)
    /** BungeeCord 命令注册器。 */
    override val commandRegistry: CommandRegistry = BungeeCommandRegistry(plugin)
    /** BungeeCord 玩家查询。 */
    override val players: PlayerGateway = BungeePlayerGateway()
    /** BungeeCord 插件消息通道。 */
    override val pluginMessenger: PluginMessenger = BungeePluginMessenger()
    /** BungeeCord 调度器。 */
    override val scheduler: Scheduler = BungeeScheduler(plugin)
}
