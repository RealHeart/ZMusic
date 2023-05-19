package me.zhenxin.zmusic.module.command.impl

import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper

/**
 * 帮助命令
 *
 * @author 真心
 * @since 2021/8/14 21:43
 * @email qgzhenxin@qq.com
 */

val helpCommand = subCommand {
    createHelper()
}