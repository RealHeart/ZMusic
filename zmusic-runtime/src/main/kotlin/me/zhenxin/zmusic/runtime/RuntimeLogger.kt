package me.zhenxin.zmusic.runtime

/**
 * runtime 使用的日志出口。
 *
 * @author 真心
 * @since 5.0.0
 */
interface RuntimeLogger {
    /**
     * 输出普通信息。
     *
     * @param message 日志内容
     */
    fun info(message: String)

    /**
     * 输出警告信息。
     *
     * @param message 日志内容
     */
    fun warn(message: String)

    /**
     * 输出错误信息。
     *
     * @param message 日志内容
     * @param throwable 关联异常，可以为 null
     */
    fun error(message: String, throwable: Throwable? = null)
}
