package me.zhenxin.zmusic.module.command.impl

import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.config.Lang
import me.zhenxin.zmusic.config.config
import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.module.taboolib.sendMsg
import me.zhenxin.zmusic.utils.isVip
import me.zhenxin.zmusic.utils.loginNetease
import me.zhenxin.zmusic.utils.setLocale
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.lang.Language

/**
 * 重载命令
 *
 * @author 真心
 * @since 2021/9/23 11:11
 * @email qgzhenxin@qq.com
 */
val reloadCommand = subCommand {
    execute<ProxyCommandSender> { sender, _, _ ->
        config.reload()
        Language.reload()
        setLocale()
        ZMusic.IS_VIP = isVip()
        sender.sendMsg(Lang.COMMAND_RELOAD_SUCCESS)
        submit(async = true) {
            logger.info("&a正在尝试登录网易云音乐...")
            val result = loginNetease()
            logger.info(result.message)
        }
    }
}