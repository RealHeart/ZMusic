package me.zhenxin.zmusic.platform

import me.zhenxin.zmusic.platform.entity.ZCommandSender

/**
 * core 暴露给平台命令系统的处理器。
 *
 * @author 真心
 * @since 5.0.0
 */
interface PlatformCommand {
    /**
     * 执行命令。
     *
     * @param sender 命令发送者
     * @param label 实际命令标签
     * @param args 命令参数
     * @return true 表示命令已处理
     */
    fun execute(sender: ZCommandSender, label: String, args: List<String>): Boolean

    /**
     * 返回补全候选。
     *
     * @param sender 命令发送者
     * @param args 当前参数
     * @return 补全候选列表
     */
    fun suggest(sender: ZCommandSender, args: List<String>): List<String>
}
