package me.zhenxin.zmusic.bukkit

import me.zhenxin.zmusic.platform.Scheduler
import org.bukkit.plugin.java.JavaPlugin

/**
 * Bukkit 调度器适配器。
 *
 * @property plugin Bukkit 插件实例
 * @author 真心
 * @since 5.0.0
 */
class BukkitScheduler(private val plugin: JavaPlugin) : Scheduler {
    /**
     * 使用 Bukkit 异步任务。
     *
     * @param task 任务
     */
    override fun async(task: () -> Unit) {
        plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable(task))
    }

    /**
     * 使用 Bukkit 主线程任务。
     *
     * @param task 任务
     */
    override fun main(task: () -> Unit) {
        plugin.server.scheduler.runTask(plugin, Runnable(task))
    }
}
