package me.zhenxin.zmusic.platform

/**
 * 平台任务调度器。
 *
 * @author 真心
 * @since 5.0.0
 */
interface Scheduler {
    /**
     * 异步执行任务。
     *
     * @param task 任务
     */
    fun async(task: () -> Unit)

    /**
     * 在主线程或等价安全上下文执行任务。
     *
     * @param task 任务
     */
    fun main(task: () -> Unit)
}
