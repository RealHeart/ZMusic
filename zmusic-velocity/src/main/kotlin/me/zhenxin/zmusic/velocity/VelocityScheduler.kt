package me.zhenxin.zmusic.velocity

import com.velocitypowered.api.proxy.ProxyServer
import me.zhenxin.zmusic.platform.Scheduler

/**
 * Velocity 调度器适配器。
 *
 * @property server Velocity 代理服务
 * @property plugin Velocity 插件实例
 * @author 真心
 * @since 5.0.0
 */
class VelocityScheduler(
    private val server: ProxyServer,
    private val plugin: Any
) : Scheduler {
    /**
     * 使用 Velocity 调度器执行任务。
     *
     * @param task 任务
     */
    override fun async(task: () -> Unit) {
        server.scheduler.buildTask(plugin, Runnable(task)).schedule()
    }

    /**
     * Velocity 没有 Bukkit 式主线程调度，这里复用普通调度。
     *
     * @param task 任务
     */
    override fun main(task: () -> Unit) {
        async(task)
    }
}
