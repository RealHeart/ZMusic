package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.common.command.CmdExec
import me.zhenxin.zmusic.common.modules.sender.impl.BungeeSender
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor

/**
 * 命令执行器 Bungee
 *
 * @author 真心
 * @since 2021/8/4 12:44
 * @email qgzhenxin@qq.com
 */
class CmdExec : Command(
    "zm",
    null,
    "zmusic",
    "music"
), TabExecutor {
    override fun execute(sender: CommandSender, args: Array<String>) {
        CmdExec.onCommand(BungeeSender(sender), args)
    }

    override fun onTabComplete(sender: CommandSender, args: Array<String>): MutableIterable<String> {
        return CmdExec.onTabComplete(BungeeSender(sender), args)
    }
}