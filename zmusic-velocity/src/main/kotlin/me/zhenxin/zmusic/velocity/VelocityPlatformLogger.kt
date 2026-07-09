package me.zhenxin.zmusic.velocity

import me.zhenxin.zmusic.platform.PlatformLogger
import org.slf4j.Logger

/**
 * Velocity logger 适配器。
 *
 * @property logger slf4j logger
 * @author 真心
 * @since 5.0.0
 */
class VelocityPlatformLogger(private val logger: Logger) : PlatformLogger {
    /**
     * 输出 info。
     *
     * @param message 日志内容
     */
    override fun info(message: String) {
        logger.info(message)
    }

    /**
     * 输出 warning。
     *
     * @param message 日志内容
     */
    override fun warn(message: String) {
        logger.warn(message)
    }

    /**
     * 输出 error。
     *
     * @param message 日志内容
     * @param throwable 关联异常，可以为 null
     */
    override fun error(message: String, throwable: Throwable?) {
        logger.error(message, throwable)
    }
}
