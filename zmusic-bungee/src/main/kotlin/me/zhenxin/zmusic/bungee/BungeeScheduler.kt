package me.zhenxin.zmusic.bungee

import me.zhenxin.zmusic.platform.Scheduler
import net.md_5.bungee.api.plugin.Plugin
import java.util.concurrent.TimeUnit

/**
 * BungeeCord 调度器适配器。
 *
 * @property plugin BungeeCord 插件实例
 * @author 真心
 * @since 5.0.0
 */
class BungeeScheduler(private val plugin: Plugin) : Scheduler {
    /**
     * 使用 BungeeCord 异步任务。
     *
     * @param task 任务
     */
    override fun async(task: () -> Unit) {
        plugin.proxy.scheduler.runAsync(plugin, Runnable(task))
    }

    /**
     * 使用 0 延迟调度执行任务。
     *
     * @param task 任务
     */
    override fun main(task: () -> Unit) {
        plugin.proxy.scheduler.schedule(plugin, Runnable(task), 0, TimeUnit.MILLISECONDS)
    }
}
