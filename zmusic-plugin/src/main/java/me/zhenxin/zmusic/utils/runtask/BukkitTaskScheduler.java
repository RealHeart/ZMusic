package me.zhenxin.zmusic.utils.runtask;

import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.ZMusicBukkit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * 在玩家所属线程执行 Bukkit API 调用。
 * Folia/Leaf 不允许从插件异步线程直接访问玩家对象，普通 Bukkit 则回到主线程。
 */
public final class BukkitTaskScheduler {

    private BukkitTaskScheduler() {
    }

    public static void run(Player player, Runnable task) {
        if (player == null) {
            return;
        }
        if (!ZMusic.isFolia && Bukkit.isPrimaryThread()) {
            task.run();
            return;
        }
        if (ZMusic.isFolia && runOnEntityScheduler(player, task)) {
            return;
        }
        if (!ZMusic.isFolia) {
            Bukkit.getScheduler().runTask(ZMusicBukkit.plugin, task);
            return;
        }
        ZMusic.log.sendDebugMessage("[任务调度] 无法将任务提交到玩家实体调度器");
    }

    public static Runnable runAtFixedRate(Player player, Runnable task, long delay, long period) {
        if (!ZMusic.isFolia) {
            org.bukkit.scheduler.BukkitTask scheduledTask = Bukkit.getScheduler()
                    .runTaskTimer(ZMusicBukkit.plugin, task, delay, period);
            return scheduledTask::cancel;
        }
        try {
            Method getScheduler = Player.class.getMethod("getScheduler");
            Object scheduler = getScheduler.invoke(player);
            for (Method method : getScheduler.getReturnType().getMethods()) {
                if (!method.getName().equals("runAtFixedRate") || method.getParameterCount() != 5) {
                    continue;
                }
                Object scheduledTask = method.invoke(scheduler, ZMusicBukkit.plugin,
                        (Consumer<Object>) ignored -> task.run(), null, delay, period);
                if (scheduledTask == null) {
                    return null;
                }
                Method cancel = method.getReturnType().getMethod("cancel");
                return () -> {
                    try {
                        cancel.invoke(scheduledTask);
                    } catch (ReflectiveOperationException | RuntimeException ignored) {
                    }
                };
            }
            ZMusic.log.sendDebugMessage("[任务调度] 当前服务端缺少玩家实体定时任务接口");
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            ZMusic.log.sendDebugMessage("[任务调度] 无法创建玩家实体定时任务");
        }
        return null;
    }

    private static boolean runOnEntityScheduler(Player player, Runnable task) {
        try {
            Method getScheduler = Player.class.getMethod("getScheduler");
            Object scheduler = getScheduler.invoke(player);
            Method execute = getScheduler.getReturnType().getMethod("execute",
                    org.bukkit.plugin.Plugin.class, Runnable.class, Runnable.class, long.class);
            return Boolean.TRUE.equals(execute.invoke(scheduler, ZMusicBukkit.plugin, task, null, 1L));
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return false;
        }
    }
}
