package me.zhenxin.zmusic.velocity

import com.velocitypowered.api.proxy.ProxyServer
import me.zhenxin.zmusic.platform.CommandRegistry
import me.zhenxin.zmusic.platform.Platform
import me.zhenxin.zmusic.platform.PlatformContext
import me.zhenxin.zmusic.platform.PlatformLogger
import me.zhenxin.zmusic.platform.PlayerGateway
import me.zhenxin.zmusic.platform.PluginMessenger
import me.zhenxin.zmusic.platform.Scheduler
import org.slf4j.Logger
import java.io.File
import java.nio.file.Path

/**
 * Velocity 平台能力实现。
 *
 * @property server Velocity 代理服务
 * @param logger Velocity logger
 * @param dataDirectory 插件数据目录
 * @param plugin Velocity 插件实例
 * @author 真心
 * @since 5.0.0
 */
class VelocityPlatformContext(
    private val server: ProxyServer,
    logger: Logger,
    dataDirectory: Path,
    plugin: Any
) : PlatformContext {
    /** 当前平台类型。 */
    override val platform: Platform = Platform.VELOCITY
    /** Velocity 插件数据目录。 */
    override val dataFolder: File = dataDirectory.toFile()
    /** Velocity logger 适配器。 */
    override val logger: PlatformLogger = VelocityPlatformLogger(logger)
    /** Velocity 命令注册器。 */
    override val commandRegistry: CommandRegistry = VelocityCommandRegistry(server)
    /** Velocity 玩家查询。 */
    override val players: PlayerGateway = VelocityPlayerGateway(server)
    /** Velocity 插件消息通道。 */
    override val pluginMessenger: PluginMessenger = VelocityPluginMessenger(server)
    /** Velocity 调度器。 */
    override val scheduler: Scheduler = VelocityScheduler(server, plugin)
}
