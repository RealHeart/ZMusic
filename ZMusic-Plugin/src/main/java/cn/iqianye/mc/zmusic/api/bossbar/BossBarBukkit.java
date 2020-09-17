package cn.iqianye.mc.zmusic.api.bossbar;

import cn.iqianye.mc.zmusic.ZMusic;
import org.bukkit.entity.Player;

import java.util.List;


public class BossBarBukkit implements BossBar {
    private final Player p;
    private final String title;
    private final double seconds;
    private final org.bukkit.boss.BossBar bar;

    public BossBarBukkit(Object p, String title, BarColor color, BarStyle style, float seconds) {
        Player player = (Player) p;
        this.bar = org.bukkit.Bukkit.getServer().createBossBar(title, org.bukkit.boss.BarColor.valueOf(color.name()), org.bukkit.boss.BarStyle.valueOf(style.name()));
        this.p = player;
        this.title = title;
        this.seconds = seconds;
    }

    @Override
    public void showTitle() {
        bar.setVisible(true);
        bar.setProgress(0);
        bar.addPlayer(p);
        ZMusic.runTask.runAsync(() -> {
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
        });
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        bar.setTitle(title);
    }

    @Override
    public void removePlayer(Object player) {
        Player p = (Player) player;
        bar.removePlayer(p);
    }

    @Override
    public void removeAll() {
        bar.removeAll();
    }

    @Override
    public boolean isVisible() {
        return bar.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        bar.setVisible(visible);
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
        bar.setProgress(progress);
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
        bar.addPlayer((Player) playerObj);
    }
}
