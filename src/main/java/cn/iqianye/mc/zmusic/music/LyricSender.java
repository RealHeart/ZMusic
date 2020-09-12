package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.api.BossBar;
import cn.iqianye.mc.zmusic.api.Version;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.nms.ActionBar;
import cn.iqianye.mc.zmusic.nms.ActionBar_1_8_R3;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.LogUtils;
import cn.iqianye.mc.zmusic.utils.MessageUtils;
import cn.iqianye.mc.zmusic.utils.MusicUtils;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LyricSender extends BukkitRunnable {
    public Player player;
    public JsonObject lyric;
    public long maxTime;
    public String name;
    public String url;
    public boolean isBoosBar;
    public boolean isActionBar;
    public boolean isTitle;
    public boolean isChat;
    public boolean isPlayList = false;
    public boolean isStop = false;
    BossBar bossBar;
    long time = -1;

    Version version = new Version();
    ActionBar actionBar = null;
    boolean is1_8 = false;

    {
        if (version.isEquals("1.8")) {
            LogUtils.sendDebugMessage("[ActionBar] 检测为服务端版本1.8,使用NMS发送.");
            actionBar = new ActionBar_1_8_R3();
            is1_8 = true;
        }
    }


    @Override
    public void run() {
        while (player.isOnline()) {
            if (isStop) {
                LogUtils.sendDebugMessage("[播放器] 来自外部的关闭请求，线程已停止");
                break;
            }
            time++;
            if (isBoosBar) {
                if (bossBar == null) {
                    bossBar = PlayerStatus.getPlayerBoosBar(player);
                    if (bossBar == null) {
                        bossBar = new BossBar(player, "§b§l" + name, BarColor.BLUE, BarStyle.SEGMENTED_20, maxTime);
                        bossBar.showTitle();
                        PlayerStatus.setPlayerBoosBar(player, bossBar);
                    } else {
                        bossBar.removePlayer(player);
                        bossBar = new BossBar(player, "§b§l" + name, BarColor.BLUE, BarStyle.SEGMENTED_20, maxTime);
                        bossBar.showTitle();
                        PlayerStatus.setPlayerBoosBar(player, bossBar);
                    }
                }
            }
            if (PlayerStatus.getPlayerPlayStatus(player)) {
                PlayerStatus.setPlayerCurrentTime(player, time);
                if (!(time > maxTime)) {
                    if (lyric != null) {
                        JsonObject j = lyric.getAsJsonObject(String.valueOf(time));
                        if (j != null) {
                            String lrc = j.get("lrc").getAsString();
                            String lrcTr = j.get("lrcTr").getAsString();
                            String[] lrcs = lrc.split("\n");
                            String[] lrcTrs = {""};
                            if (!lrcTr.isEmpty()) {
                                lrcTrs = lrcTr.split("\n");
                            }
                            for (int i = 0; i < lrcs.length; i++) {
                                String lyric;
                                String lyricTr;
                                lyric = lrcs[i];
                                try {
                                    lyricTr = lrcTrs[i];
                                } catch (Exception e) {
                                    lyricTr = "";
                                }
                                lyric = lyric.replaceAll("\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})\\]", "");
                                lyricTr = lyricTr.replaceAll("\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})\\]", "");
                                if (Config.showLyricTr) {
                                    if (!lyricTr.isEmpty()) {
                                        lyric = lyric + "(" + lyricTr + ")";
                                    }
                                }
                                if (Config.lyricEnable) {
                                    if (!lyric.isEmpty()) {
                                        PlayerStatus.setPlayerLyric(player, lyric);
                                    }
                                    if (isBoosBar) {
                                        if (!lyric.isEmpty()) {
                                            bossBar.setTitle("§b§l" + lyric);
                                        }
                                    }
                                    if (isActionBar) {
                                        if (!lyric.isEmpty()) {
                                            if (!is1_8) {
                                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§b§l" + lyric));
                                            } else {
                                                actionBar.sendActionBar(player, "§b§l" + lyric);
                                            }
                                        }
                                    }
                                    if (isTitle) {
                                        if (!lyric.isEmpty()) {
                                            try {
                                                player.sendTitle("", "§b" + lyric, 0, 200, 20);
                                            } catch (NoSuchMethodError e) {
                                                player.sendTitle("", "§b" + lyric);
                                            }
                                        }
                                    }
                                    if (isChat) {
                                        lyric = lrcs[i];
                                        try {
                                            lyricTr = lrcTrs[i];
                                        } catch (Exception e) {
                                            lyricTr = "";
                                        }
                                        lyric = lyric.replaceAll("\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})\\]", "");
                                        lyricTr = lyricTr.replaceAll("\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})\\]", "");
                                        MessageUtils.sendNormalMessage("§b" + lyric, player);
                                        if (Config.showLyricTr) {
                                            if (!lyricTr.isEmpty()) {
                                                MessageUtils.sendNormalMessage("§b" + lyricTr, player);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (isBoosBar) {
                        bossBar.removePlayer(player);
                    }
                    if (!isPlayList) {
                        LogUtils.sendDebugMessage("[播放器] 非歌单模式 检测循环播放状态");
                        Boolean loop = PlayerStatus.getPlayerLoopPlay(player);
                        if (loop != null && loop) {
                            LogUtils.sendDebugMessage("[播放器] 循环播放开启 重新播放当前音乐");
                            time = -1;
                            MusicUtils.stopSelf(player);
                            MusicUtils.playSelf(url, player);
                            if (isBoosBar) {
                                bossBar = new BossBar(player, "§b§l" + name, BarColor.BLUE, BarStyle.SEGMENTED_20, maxTime);
                                bossBar.showTitle();
                            }
                            PlayerStatus.setPlayerCurrentTime(player, time);
                        } else {
                            LogUtils.sendDebugMessage("[播放器] 循环播放关闭 线程已停止");
                            OtherUtils.resetPlayerStatusSelf(player);
                            break;
                        }
                    } else {
                        LogUtils.sendDebugMessage("[播放器] 歌单模式 线程已停止");
                        OtherUtils.resetPlayerStatusSelf(player);
                        break;
                    }
                }
            } else {
                OtherUtils.resetPlayerStatusSelf(player);
                if (isBoosBar) {
                    bossBar.removePlayer(player);
                }
                LogUtils.sendDebugMessage("[播放器] 播放状态改变，线程已停止");
                break;
            }
            try {
                Thread.sleep(1000L);
            } catch (
                    Exception e) {
                e.printStackTrace();
            }
        }
    }
}
