package cn.iqianye.mc.zmusic.api;

import cn.iqianye.mc.zmusic.ZMusicBukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class BossBar {
    static Player p;
    static String title;
    static BarColor color;
    static BarStyle style;
    static double seconds;
    static org.bukkit.boss.BossBar bar;
    public final JavaPlugin plugin;

    public BossBar(Object p, String title, BarColor color, BarStyle style, double seconds) {
        Player player = (Player) p;
        BossBar.p = player;
        BossBar.title = title;
        BossBar.color = color;
        BossBar.style = style;
        BossBar.seconds = seconds;
        this.plugin = ZMusicBukkit.plugin;
    }

    public void showTitle() {
        bar = org.bukkit.Bukkit.getServer().createBossBar(title, org.bukkit.boss.BarColor.valueOf(color.name()), org.bukkit.boss.BarStyle.valueOf(style.name()));
        bar.setVisible(true);
        bar.setProgress(0);
        bar.addPlayer(p);
        new bartimer(bar).runTaskAsynchronously(plugin);
    }

    public void removePlayer(Object player) {
        Player p = (Player) player;
        bar.removePlayer(p);
    }

    public enum BarStyle {
        SOLID,
        SEGMENTED_6,
        SEGMENTED_10,
        SEGMENTED_12,
        SEGMENTED_20;
    }

    public void setTitle(String title) {
        bar.setTitle(title);
    }

    public enum BarColor {
        PINK,
        BLUE,
        RED,
        GREEN,
        YELLOW,
        PURPLE,
        WHITE;
    }

    static class bartimer extends BukkitRunnable {
        org.bukkit.boss.BossBar bar;

        public bartimer(org.bukkit.boss.BossBar bar1) {
            bar = bar1;
        }

        @Override
        public void run() {
            double step = 1F / seconds;
            double prog = bar.getProgress();
            while (prog >= 0 || prog <= 1) {
                prog += step;
                if (prog > 1) break;
                bar.setProgress(prog);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            bar.setVisible(false);
        }
    }
}
