package me.zhenxin.zmusic.platform

/**
 * 日志
 *
 * @author 真心
 * @since 2023/7/22 20:08
 * @email qgzhenxin@qq.com
 */
interface Logger {
    /**
     * 信息日志
     * @param msg String 日志信息
     */
    fun info(msg: String)

    /**
     * 警告日志
     * @param msg String 日志信息
     */
    fun warn(msg: String)

    /**
     * 错误日志
     * @param msg String 日志信息
     */
    fun error(msg: String)

    /**
     * 调试日志
     * @param msg String 日志信息
     */
    fun debug(msg: String)
}
