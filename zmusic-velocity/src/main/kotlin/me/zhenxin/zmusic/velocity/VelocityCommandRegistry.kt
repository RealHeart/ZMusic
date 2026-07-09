package me.zhenxin.zmusic.velocity

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.ProxyServer
import me.zhenxin.zmusic.platform.CommandRegistry
import me.zhenxin.zmusic.platform.PlatformCommand

/**
 * Velocity 命令注册器。
 *
 * @property server Velocity 代理服务
 * @author 真心
 * @since 5.0.0
 */
class VelocityCommandRegistry(private val server: ProxyServer) : CommandRegistry {
    /**
     * 注册 `/zmusic` 命令。
     *
     * @param platformCommand core 命令处理器
     */
    override fun register(platformCommand: PlatformCommand) {
        val meta = server.commandManager.metaBuilder("zmusic")
            .aliases("music", "zm")
            .plugin(this)
            .build()
        server.commandManager.register(meta, ZMusicVelocityCommand(platformCommand))
    }
}

/**
 * Velocity 命令桥接器。
 *
 * @property commandManager core 命令处理器
 * @author 真心
 * @since 5.0.0
 */
private class ZMusicVelocityCommand(
    private val commandManager: PlatformCommand
) : SimpleCommand {
    /**
     * 执行命令。
     *
     * @param invocation Velocity 命令调用信息
     */
    override fun execute(invocation: SimpleCommand.Invocation) {
        commandManager.execute(VelocityCommandSender(invocation.source()), "zmusic", invocation.arguments().toList())
    }

    /**
     * 返回补全候选。
     *
     * @param invocation Velocity 命令调用信息
     * @return 补全候选
     */
    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        return commandManager.suggest(VelocityCommandSender(invocation.source()), invocation.arguments().toList())
    }
}
