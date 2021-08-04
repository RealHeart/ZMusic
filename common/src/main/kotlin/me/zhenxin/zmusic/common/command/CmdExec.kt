package me.zhenxin.zmusic.common.command

import me.zhenxin.zmusic.common.logger
import me.zhenxin.zmusic.common.modules.sender.Sender

/**
 * 命令执行器
 *
 * @author 真心
 * @since 2021/8/4 12:36
 * @email qgzhenxin@qq.com
 */
object CmdExec {

    fun onCommand(sender: Sender, args: Array<String>): Boolean {
        logger.debug("[命令] ${sender.name} 执行了 命令 ${args.contentToString()}")
        return true
    }

    fun onTabComplete(sender: Sender, args: Array<String>): MutableList<String> {
        logger.debug("[命令] ${sender.name} 触发了 补全 ${args.contentToString()}")
        return mutableListOf()
    }
}