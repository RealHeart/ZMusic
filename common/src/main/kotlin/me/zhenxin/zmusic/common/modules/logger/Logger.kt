package me.zhenxin.zmusic.common.modules.logger

import me.zhenxin.zmusic.common.config.Config
import java.util.logging.Logger

/**
 * 日志模块基本接口
 *
 * @author 真心
 * @since 2021/7/19 11:43
 * @email qgzhenxin@qq.com
 */
class Logger(private val logger: Logger) {
    /**
     * 普通信息日志
     *
     * @param msg 日志内容
     */
    fun info(msg: String) {
        logger.info(msg.replace("&", "§"))
    }

    /**
     * 警告信息日志
     *
     * @param msg 日志内容
     */
    fun warn(msg: String) {
        logger.warning(msg.replace("&", "§"))
    }

    /**
     * 调试信息日志
     *
     * @param msg 日志内容
     */
    fun debug(msg: String) {
        if (Config.Debug) {
            warn(msg)
        }
    }
}