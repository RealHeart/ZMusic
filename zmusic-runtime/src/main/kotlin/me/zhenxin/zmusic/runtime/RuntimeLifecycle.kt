package me.zhenxin.zmusic.runtime

/**
 * core 应用暴露给 runtime 的生命周期。
 *
 * @author 真心
 * @since 5.0.0
 */
interface RuntimeLifecycle {
    /**
     * 启动 core 应用。
     */
    fun start()

    /**
     * 停止 core 应用。
     */
    fun stop()
}
