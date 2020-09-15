package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.ZMusicBukkit;
import cn.iqianye.mc.zmusic.api.BossBar;
import cn.iqianye.mc.zmusic.api.ProgressBar;
import cn.iqianye.mc.zmusic.api.Version;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.mod.Send;
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
    public String singer;
    public String fullName;
    public String url;
    public String platform;
    public String src;
    public boolean isPlayList = false;
    public String nextMusicName;
    public PlayListPlayer playListPlayer;
    BossBar bossBar;
    long time = -1;

    private boolean isStop = false;
    Version version = new Version();
    ActionBar actionBar = null;
    boolean is1_8 = false;

    StringBuilder hudInfo = new StringBuilder();
    ProgressBar progressBar;

    public void init() {
        PlayerStatus.setPlayerPlayStatus(player, true);
        PlayerStatus.setPlayerMusicName(player, name);
        PlayerStatus.setPlayerMusicSinger(player, singer);
        PlayerStatus.setPlayerPlaySource(player, src);
        PlayerStatus.setPlayerPlatform(player, platform);
        PlayerStatus.setPlayerMaxTime(player, maxTime);
        PlayerStatus.setPlayerCurrentTime(player, 0L);
        progressBar = new ProgressBar('■', '□', maxTime);
        if (version.isEquals("1.8")) {
            LogUtils.sendDebugMessage("[播放器](ID:" + getTaskId() + ") 检测为服务端版本1.8,使用NMS发送ActionBar.");
            actionBar = new ActionBar_1_8_R3();
            is1_8 = true;
        }
        if (Config.supportBossBar) {
            bossBar = PlayerStatus.getPlayerBoosBar(player);
            if (bossBar == null) {
                bossBar = new BossBar(player, Config.lyricColor + fullName, BarColor.BLUE, BarStyle.SEGMENTED_20, maxTime);
                bossBar.showTitle();
                PlayerStatus.setPlayerBoosBar(player, bossBar);
            } else {
                bossBar.removePlayer(player);
                bossBar = new BossBar(player, Config.lyricColor + fullName, BarColor.BLUE, BarStyle.SEGMENTED_20, maxTime);
                bossBar.showTitle();
                PlayerStatus.setPlayerBoosBar(player, bossBar);
            }
        }
        if (Config.supportHud) {
            Send.sendAM(player, Config.hudInfoX, Config.hudInfoY, Config.hudLyricX, Config.hudLyricY);
            hudInfo.append("歌名: ").append(name).append("\n");
            hudInfo.append("歌手: ").append(singer).append("\n");
            hudInfo.append("进度: 00:00/").append(OtherUtils.formatTime(maxTime / 100));
            if (isPlayList) {
                hudInfo.append("\n下一首: ").append(nextMusicName);
            }
        }
    }

    @Override
    public void run() {
        while (!isStop) {
            if (player.isOnline()) {
                time++;
                if (PlayerStatus.getPlayerPlayStatus(player)) {
                    PlayerStatus.setPlayerCurrentTime(player, time);
                    if (Config.supportHud) {
                        updateHudTime(time);
                    }
                    if (!(time > maxTime)) {
                        if (Config.lyricEnable) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (lyric != null) {
                                        JsonObject j = lyric.getAsJsonObject(String.valueOf(time));
                                        if (j != null) {
                                            sendLyric(j);
                                            if (Config.supportHud) {
                                                sendHud(j);
                                            }
                                        }
                                    }
                                }
                            }.runTaskAsynchronously(ZMusicBukkit.plugin);
                        }
                    } else {
                        OtherUtils.resetPlayerStatus(player);
                        if (Config.supportHud) {
                            Send.sendAM(player, "[Lyric]");
                            Send.sendAM(player, "[Info]");
                        }
                        if (!isPlayList) {
                            LogUtils.sendDebugMessage("[播放器](ID:" + getTaskId() + ") 非歌单模式 检测循环播放状态");
                            Boolean loop = PlayerStatus.getPlayerLoopPlay(player);
                            if (loop != null && loop) {
                                LogUtils.sendDebugMessage("[播放器](ID:" + getTaskId() + ") 循环播放开启 重新播放当前音乐");
                                time = -1;
                                PlayerStatus.setPlayerPlayStatus(player, true);
                                MusicUtils.play(url, player);
                                if (Config.supportBossBar) {
                                    bossBar = new BossBar(player, "§b§l" + fullName, BarColor.BLUE, BarStyle.SEGMENTED_20, maxTime);
                                    bossBar.showTitle();
                                }
                            } else {
                                LogUtils.sendDebugMessage("[播放器](ID:" + getTaskId() + ") 循环播放关闭 线程停止");
                                stop();
                            }
                        } else {
                            LogUtils.sendDebugMessage("[播放器](ID:" + getTaskId() + ") 歌单模式 线程停止");
                            playListPlayer.singleIsPlayEd = true;
                            stop();
                        }
                    }
                } else {
                    LogUtils.sendDebugMessage("[播放器](ID:" + getTaskId() + ") 播放状态改变，线程停止");
                    stop();
                }
            } else {
                LogUtils.sendDebugMessage("[播放器](ID:" + getTaskId() + ") 玩家离线，线程停止");
                stop();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LogUtils.sendDebugMessage("[播放器](ID:" + getTaskId() + ") 线程已停止");
    }

    private void sendHud(JsonObject j) {
        String l = j.get("lrc").getAsString();
        String lT = j.get("lrcTr").getAsString();
        StringBuilder sb = new StringBuilder();
        if (!l.isEmpty()) {
            l = Config.lyricColor + l;
            sb.append(l);
            if (Config.showLyricTr) {
                if (!lT.isEmpty()) {
                    lT = Config.lyricColor + lT;
                    sb.append("\n").append(lT);
                }
            }
            String data = sb.toString().replaceAll("\n",
                    "\n" + Config.lyricColor);
            Send.sendAM(player, "[Lyric]" + data);
        }
    }

    private void sendLyric(JsonObject j) {
        String lrc = j.get("lrc").getAsString();
        String lrcTr = j.get("lrcTr").getAsString();
        String[] lrcs = lrc.split("\n");
        String[] lrcTrs = {""};
        if (!lrcTr.isEmpty()) {
            lrcTrs = lrcTr.split("\n");
        }
        for (int i = 0; i < lrcs.length; i++) {
            String lyricToInOne;
            String lyricTr;
            String lyric;
            lyric = lrcs[i];
            try {
                lyricTr = lrcTrs[i];
            } catch (Exception e) {
                lyricTr = "";
            }
            lyricToInOne = lyric;
            if (Config.showLyricTr) {
                if (!lyricTr.isEmpty()) {
                    lyricToInOne = lyric + "(" + lyricTr + ")";
                }
            }
            if (!lyricToInOne.isEmpty()) {
                PlayerStatus.setPlayerLyric(player, lyricToInOne);
            }
            if (Config.supportBossBar) {
                if (!lyricToInOne.isEmpty()) {
                    bossBar.setTitle(Config.lyricColor + lyricToInOne);
                }
            }
            if (Config.supportActionBar) {
                if (!lyricToInOne.isEmpty()) {
                    if (!is1_8) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Config.lyricColor + lyricToInOne));
                    } else {
                        actionBar.sendActionBar(player, Config.lyricColor + lyricToInOne);
                    }
                }
            }
            if (Config.supportTitle) {
                if (!lyricToInOne.isEmpty()) {
                    try {
                        player.sendTitle("", Config.lyricColor + lyricToInOne, 0, 200, 20);
                    } catch (NoSuchMethodError e) {
                        player.sendTitle("", Config.lyricColor + lyricToInOne);
                    }
                }
            }
            if (Config.supportChat) {
                MessageUtils.sendNormalMessage(Config.lyricColor + lyric, player);
                if (Config.showLyricTr) {
                    if (!lyricTr.isEmpty()) {
                        MessageUtils.sendNormalMessage(Config.lyricColor + lyricTr, player);
                    }
                }
            }
        }
    }

    void updateHudTime(long time) {
        String str = hudInfo.toString();
        String[] strs = str.split("\n");
        progressBar.setProgress(time);
        strs[2] = "进度: " + OtherUtils.formatTime(time) + "/" + OtherUtils.formatTime(maxTime) +
                " " + progressBar.toString();
        StringBuilder sb = new StringBuilder();
        hudInfo = new StringBuilder();
        for (String s : strs) {
            hudInfo.append(s).append("\n");
            sb.append(Config.lyricColor).append(s).append("\n");
        }
        String info = sb.substring(0, sb.length() - 1);
        Send.sendAM(player, "[Info]" + info);
    }

    public void stop() {
        OtherUtils.resetPlayerStatus(player);
        isStop = true;
        cancel();
    }
}