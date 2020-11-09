package me.zhenxin.zmusic.command

import me.zhenxin.zmusic.module.sender.SenderBukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class CmdBukkit : TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): MutableList<String> {
        return CmdEx.tab(SenderBukkit(sender), args)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return CmdEx.cmd(SenderBukkit(sender), args)
    }

}
