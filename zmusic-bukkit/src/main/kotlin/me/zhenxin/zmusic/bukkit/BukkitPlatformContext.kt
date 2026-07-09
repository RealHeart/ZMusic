package me.zhenxin.zmusic.bukkit

import me.zhenxin.zmusic.platform.CommandRegistry
import me.zhenxin.zmusic.platform.Platform
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.PlatformLogger
import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.Scheduler
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * Bukkit 平台能力实现。
 *
 * @property plugin Bukkit 插件实例
 * @author 真心
 * @since 5.0.0
 */
class BukkitPlatformContext(private val plugin: JavaPlugin) : PlatformContext {
    /** 当前平台类型。 */
    override val platform: Platform = Platform.BUKKIT
    /** Bukkit 插件数据目录。 */
    override val dataFolder: File = plugin.dataFolder
    /** Bukkit logger 适配器。 */
    override val logger: PlatformLogger = BukkitPlatformLogger(plugin)
    /** Bukkit 命令注册器。 */
    override val commandRegistry: CommandRegistry = BukkitCommandRegistry(plugin)
    /** Bukkit 在线玩家查询。 */
    override val players: PlayerGateway = BukkitPlayerGateway()
    /** Bukkit 插件消息通道。 */
    override val pluginMessenger: PluginMessenger = BukkitPluginMessenger(plugin)
    /** Bukkit 调度器。 */
    override val scheduler: Scheduler = BukkitScheduler(plugin)
}
