package me.zhenxin.zmusic.command

import me.zhenxin.zmusic.module.sender.SenderBC
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor

class CmdBC : Command("zm", null, "zmusic", "music"), TabExecutor {
    override fun execute(sender: CommandSender, args: Array<String>) {
        CmdEx.cmd(SenderBC(sender), args)
    }

    override fun onTabComplete(sender: CommandSender, args: Array<String>): Iterable<String> {
        return CmdEx.tab(SenderBC(sender), args)
    }
}
