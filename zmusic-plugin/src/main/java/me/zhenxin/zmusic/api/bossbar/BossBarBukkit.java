package me.zhenxin.zmusic.api.bossbar;

import me.zhenxin.zmusic.utils.runtask.BukkitTaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;


public class BossBarBukkit implements BossBar {

    private final Player p;
    private volatile String title;
    private final double seconds;
    private final BarColor color;
    private final BarStyle style;
    private org.bukkit.boss.BossBar bar;
    private Runnable cancelProgressTask;

    public BossBarBukkit(Object p, String title, BarColor color, BarStyle style, float seconds) {
        Player player = (Player) p;
        this.p = player;
        this.title = title;
        this.color = color;
        this.style = style;
        this.seconds = seconds;
    }

    @Override
    public void showTitle() {
        BukkitTaskScheduler.run(p, this::showTitleSync);
    }

    private void showTitleSync() {
        if (bar == null) {
            bar = Bukkit.createBossBar(title, org.bukkit.boss.BarColor.valueOf(color.name()),
                    org.bukkit.boss.BarStyle.valueOf(style.name()));
        }
        bar.setVisible(true);
        bar.setProgress(0);
        bar.addPlayer(p);
        Runnable tick = () -> {
            if (!bar.isVisible()) {
                cancelTask();
                return;
            }
            double step = 1F / seconds;
            double prog = Math.min(bar.getProgress() + step, 1.0);
            if (prog >= 1.0) {
                bar.setProgress(1.0);
                bar.setVisible(false);
                cancelTask();
                return;
            }
            bar.setProgress(prog);
        };
        cancelProgressTask = BukkitTaskScheduler.runAtFixedRate(p, tick, 20L, 20L);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        BukkitTaskScheduler.run(p, () -> {
            if (bar != null) {
                bar.setTitle(title);
            }
        });
    }

    @Override
    public void removePlayer(Object player) {
        Player p = (Player) player;
        BukkitTaskScheduler.run(p, () -> {
            if (bar != null) {
                bar.removePlayer(p);
            }
            cancelTask();
        });
    }

    @Override
    public void removeAll() {
        BukkitTaskScheduler.run(p, () -> {
            if (bar != null) {
                bar.removeAll();
            }
            cancelTask();
        });
    }

    @Override
    public boolean isVisible() {
        return bar != null && bar.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        BukkitTaskScheduler.run(p, () -> {
            if (bar != null) {
                bar.setVisible(visible);
            }
            if (!visible) {
                cancelTask();
            }
        });
    }

    private void cancelTask() {
        if (cancelProgressTask != null) {
            cancelProgressTask.run();
            cancelProgressTask = null;
        }
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.valueOf(bar.getColor().name());
    }

    @Override
    public void setBarColor(BarColor barColor) {
        bar.setColor(org.bukkit.boss.BarColor.valueOf(barColor.name()));
    }

    @Override
    public BarStyle getBarStyle() {
        return BarStyle.valueOf(bar.getStyle().name());
    }

    @Override
    public void setBarStyle(BarStyle barStyle) {
        bar.setStyle(org.bukkit.boss.BarStyle.valueOf(barStyle.name()));
    }

    @Override
    public double getProgress() {
        return (float) bar.getProgress();
    }

    @Override
    public void setProgress(double progress) {
        BukkitTaskScheduler.run(p, () -> {
            if (bar != null) {
                bar.setProgress(progress);
            }
        });
    }

    @Override
    public boolean hasPlayer(Object playerObj) {
        List<Player> players = bar.getPlayers();
        for (Player player : players) {
            if (player == playerObj)
                return true;
        }
        return false;
    }

    @Override
    public void addPlayer(Object playerObj) {
        Player player = (Player) playerObj;
        BukkitTaskScheduler.run(player, () -> {
            if (bar != null) {
                bar.addPlayer(player);
            }
        });
    }
}
