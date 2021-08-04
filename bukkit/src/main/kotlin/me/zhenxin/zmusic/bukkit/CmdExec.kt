package me.zhenxin.zmusic.bukkit

import me.zhenxin.zmusic.common.command.CmdExec
import me.zhenxin.zmusic.common.modules.sender.impl.BukkitSender
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

/**
 * 命令执行器 Bukkit
 *
 * @author 真心
 * @since 2021/8/4 12:37
 * @email qgzhenxin@qq.com
 */
class CmdExec : TabExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return CmdExec.onCommand(BukkitSender(sender), args)
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): MutableList<String> {
        return CmdExec.onTabComplete(BukkitSender(sender), args)
    }
}