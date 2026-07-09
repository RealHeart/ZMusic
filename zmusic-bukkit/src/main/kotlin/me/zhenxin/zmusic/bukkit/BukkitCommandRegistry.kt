package me.zhenxin.zmusic.bukkit

import me.zhenxin.zmusic.platform.CommandRegistry
import me.zhenxin.zmusic.platform.PlatformCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.plugin.java.JavaPlugin

/**
 * Bukkit 命令注册器。
 *
 * @property plugin Bukkit 插件实例
 * @author 真心
 * @since 5.0.0
 */
class BukkitCommandRegistry(private val plugin: JavaPlugin) : CommandRegistry {
    /**
     * 将 core 命令处理器桥接到 Bukkit 的 TabExecutor。
     *
     * @param platformCommand core 命令处理器
     */
    override fun register(platformCommand: PlatformCommand) {
        val executor = object : TabExecutor {
            /**
             * 执行 `/zmusic` 命令。
             *
             * @param sender 命令发送者
             * @param command Bukkit 命令对象
             * @param label 实际命令标签
             * @param args 命令参数
             * @return true 表示命令已处理
             */
            override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
                return platformCommand.execute(BukkitCommandSender(sender), label, args.toList())
            }

            /**
             * 返回 `/zmusic` 补全候选。
             *
             * @param sender 命令发送者
             * @param command Bukkit 命令对象
             * @param alias 当前别名
             * @param args 命令参数
             * @return 补全候选
             */
            override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
                return platformCommand.suggest(BukkitCommandSender(sender), args.toList())
            }
        }
        plugin.getCommand("zmusic")?.setExecutor(executor)
        plugin.getCommand("zmusic")?.tabCompleter = executor
    }
}
