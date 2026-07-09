package me.zhenxin.zmusic.platform

import me.zhenxin.zmusic.runtime.RuntimeLogger

/**
 * 平台日志适配器。
 *
 * @author 真心
 * @since 5.0.0
 */
interface PlatformLogger : RuntimeLogger {
    /**
     * 输出普通信息。
     *
     * @param message 日志内容
     */
    override fun info(message: String)

    /**
     * 输出警告信息。
     *
     * @param message 日志内容
     */
    override fun warn(message: String)

    /**
     * 输出错误信息。
     *
     * @param message 日志内容
     * @param throwable 关联异常，可以为 null
     */
    override fun error(message: String, throwable: Throwable?)
}
