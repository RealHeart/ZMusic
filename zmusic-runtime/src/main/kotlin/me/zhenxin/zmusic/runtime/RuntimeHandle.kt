package me.zhenxin.zmusic.runtime

/**
 * 平台入口保存的 runtime 句柄。
 *
 * @author 真心
 * @since 5.0.0
 */
interface RuntimeHandle {
    /**
     * 停止 core 并释放 runtime 持有的资源。
     */
    fun stop()
}
