package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.platform.CommandRegistry
import me.zhenxin.zmusic.platform.PlatformCommand
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.api.plugin.TabExecutor

/**
 * BungeeCord 命令注册器。
 *
 * @property plugin BungeeCord 插件实例
 * @author 真心
 * @since 5.0.0
 */
class BungeeCommandRegistry(private val plugin: Plugin) : CommandRegistry {
    /**
     * 注册 `/zmusic` 命令。
     *
     * @param platformCommand core 命令处理器
     */
    override fun register(platformCommand: PlatformCommand) {
        plugin.proxy.pluginManager.registerCommand(plugin, ZMusicBungeeCommand(platformCommand))
    }
}

/**
 * BungeeCord 命令桥接器。
 *
 * @property commandManager core 命令处理器
 * @author 真心
 * @since 5.0.0
 */
private class ZMusicBungeeCommand(
    private val commandManager: PlatformCommand
) : Command("zmusic", "zmusic.use", "music", "zm"), TabExecutor {
    /**
     * 执行命令。
     *
     * @param sender 命令发送者
     * @param args 命令参数
     */
    override fun execute(sender: CommandSender, args: Array<out String>) {
        commandManager.execute(BungeeCommandSender(sender), "zmusic", args.toList())
    }

    /**
     * 返回补全候选。
     *
     * @param sender 命令发送者
     * @param args 命令参数
     * @return 补全候选
     */
    override fun onTabComplete(sender: CommandSender, args: Array<out String>): Iterable<String> {
        return commandManager.suggest(BungeeCommandSender(sender), args.toList())
    }
}
