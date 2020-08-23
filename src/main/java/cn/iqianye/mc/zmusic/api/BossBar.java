package cn.iqianye.mc.zmusic.api;

import cn.iqianye.mc.zmusic.Main;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class BossBar {
    static Player p;
    static String title;
    static BarColor color;
    static BarStyle style;
    static double seconds;
    public final JavaPlugin plugin;
    static org.bukkit.boss.BossBar bar;

    public BossBar(Player p, String title, BarColor color, BarStyle style, double seconds) {
        BossBar.p = p;
        BossBar.title = title;
        BossBar.color = color;
        BossBar.style = style;
        BossBar.seconds = seconds;
        this.plugin = JavaPlugin.getPlugin(Main.class);
    }

    public void showTitle() {
        bar = Bukkit.getServer().createBossBar(title, color, style);
        bar.setVisible(true);
        bar.addPlayer(p);
        bar.setProgress(0);
        new bartimer(bar).runTaskAsynchronously(plugin);
    }

    public void setTitle(String title) {
        bar.setTitle(title);
    }

    public void removePlayer(Player player) {
        bar.removePlayer(player);
    }

    class bartimer extends BukkitRunnable {
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
