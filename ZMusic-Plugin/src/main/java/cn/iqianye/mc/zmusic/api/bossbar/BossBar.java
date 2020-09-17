package cn.iqianye.mc.zmusic.api.bossbar;

public interface BossBar {

    public void showTitle();

    public String getTitle();

    public void setTitle(String title);

    public BarColor getBarColor();

    public void setBarColor(BarColor barColor);

    public BarStyle getBarStyle();

    public void setBarStyle(BarStyle barStyle);

    public double getProgress();

    public void setProgress(double progress);

    public boolean hasPlayer(Object playerObj);

    public void addPlayer(Object playerObj);

    public void removePlayer(Object playerObj);

    public void removeAll();

    public boolean isVisible();

    public void setVisible(boolean visible);

}
