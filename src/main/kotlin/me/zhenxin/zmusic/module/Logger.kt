package me.zhenxin.zmusic.module

interface Logger {
    /**
     * 发送普通日志
     *
     * @param msg 消息
     */
    fun normal(msg: String)

    /**
     * 发送调试日志
     *
     * @param msg 消息
     */
    fun debug(msg: String)

    /**
     * 发送错误日志
     *
     * @param msg 消息
     */
    fun error(msg: String)

}
