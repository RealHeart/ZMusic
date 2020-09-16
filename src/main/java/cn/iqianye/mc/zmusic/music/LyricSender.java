package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.api.BossBar;
import cn.iqianye.mc.zmusic.api.ProgressBar;
import cn.iqianye.mc.zmusic.config.Conf;
import cn.iqianye.mc.zmusic.nms.ActionBar;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.other.OtherUtils;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.TextComponent;

public class LyricSender extends Thread {
    public Object player;
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
    //Version version = new Version();
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
        /*
        if (version.isEquals("1.8")) {
            ZMusic.log.sendDebugMessage("[播放器](ID:" + getId() + ") 检测为服务端版本1.8,使用NMS发送ActionBar.");
            actionBar = new ActionBar_1_8_R3();
            is1_8 = true;
        }
        */
        if (Conf.supportBossBar) {
            bossBar = PlayerStatus.getPlayerBoosBar(player);
            if (bossBar == null) {
                bossBar = new BossBar(player, Conf.lyricColor + fullName, BossBar.BarColor.BLUE, BossBar.BarStyle.SEGMENTED_20, maxTime);
                bossBar.showTitle();
                PlayerStatus.setPlayerBoosBar(player, bossBar);
            } else {
                bossBar.removePlayer(player);
                bossBar = new BossBar(player, Conf.lyricColor + fullName, BossBar.BarColor.BLUE, BossBar.BarStyle.SEGMENTED_20, maxTime);
                bossBar.showTitle();
                PlayerStatus.setPlayerBoosBar(player, bossBar);
            }
        }
        if (Conf.supportHud) {
            ZMusic.send.sendAM(player, Conf.hudInfoX, Conf.hudInfoY, Conf.hudLyricX, Conf.hudLyricY);
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
            if (ZMusic.player.isOnline(player)) {
                time++;
                if (PlayerStatus.getPlayerPlayStatus(player)) {
                    PlayerStatus.setPlayerCurrentTime(player, time);
                    if (Conf.supportHud) {
                        updateHudTime(time);
                    }
                    if (!(time > maxTime)) {
                        if (Conf.lyricEnable) {
                            ZMusic.runTask.start(() -> {
                                if (lyric != null) {
                                    JsonObject j = lyric.getAsJsonObject(String.valueOf(time));
                                    if (j != null) {
                                        sendLyric(j);
                                        if (Conf.supportHud) {
                                            sendHud(j);
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        OtherUtils.resetPlayerStatus(player);
                        if (Conf.supportHud) {
                            ZMusic.send.sendAM(player, "[Lyric]");
                            ZMusic.send.sendAM(player, "[Info]");
                        }
                        if (!isPlayList) {
                            ZMusic.log.sendDebugMessage("[播放器](ID:" + getId() + ") 非歌单模式 检测循环播放状态");
                            Boolean loop = PlayerStatus.getPlayerLoopPlay(player);
                            if (loop != null && loop) {
                                ZMusic.log.sendDebugMessage("[播放器](ID:" + getId() + ") 循环播放开启 重新播放当前音乐");
                                time = -1;
                                PlayerStatus.setPlayerPlayStatus(player, true);
                                ZMusic.music.play(url, player);
                                if (Conf.supportBossBar) {
                                    bossBar = new BossBar(player, "§b§l" + fullName, BossBar.BarColor.BLUE, BossBar.BarStyle.SEGMENTED_20, maxTime);
                                    bossBar.showTitle();
                                }
                            } else {
                                ZMusic.log.sendDebugMessage("[播放器](ID:" + getId() + ") 循环播放关闭 线程停止");
                                stopThis();
                            }
                        } else {
                            ZMusic.log.sendDebugMessage("[播放器](ID:" + getId() + ") 歌单模式 线程停止");
                            playListPlayer.singleIsPlayEd = true;
                            stopThis();
                        }
                    }
                } else {
                    ZMusic.log.sendDebugMessage("[播放器](ID:" + getId() + ") 播放状态改变，线程停止");
                    stopThis();
                }
            } else {
                ZMusic.log.sendDebugMessage("[播放器](ID:" + getId() + ") 玩家离线，线程停止");
                stopThis();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ZMusic.log.sendDebugMessage("[播放器](ID:" + getId() + ") 线程已停止");
    }

    private void sendHud(JsonObject j) {
        String l = j.get("lrc").getAsString();
        String lT = j.get("lrcTr").getAsString();
        StringBuilder sb = new StringBuilder();
        if (!l.isEmpty()) {
            l = Conf.lyricColor + l;
            sb.append(l);
            if (Conf.showLyricTr) {
                if (!lT.isEmpty()) {
                    lT = Conf.lyricColor + lT;
                    sb.append("\n").append(lT);
                }
            }
            String data = sb.toString().replaceAll("\n",
                    "\n" + Conf.lyricColor);
            ZMusic.send.sendAM(player, "[Lyric]" + data);
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
            if (Conf.showLyricTr) {
                if (!lyricTr.isEmpty()) {
                    lyricToInOne = lyric + "(" + lyricTr + ")";
                }
            }
            if (!lyricToInOne.isEmpty()) {
                PlayerStatus.setPlayerLyric(player, lyricToInOne);
            }
            if (Conf.supportBossBar) {
                if (!lyricToInOne.isEmpty()) {
                    bossBar.setTitle(Conf.lyricColor + lyricToInOne);
                }
            }
            if (Conf.supportActionBar) {
                if (!lyricToInOne.isEmpty()) {
                    if (!is1_8) {
                        ZMusic.message.sendActionBarMessage(new TextComponent(Conf.lyricColor + lyricToInOne), player);
                    } else {
                        actionBar.sendActionBar(player, Conf.lyricColor + lyricToInOne);
                    }
                }
            }
            if (Conf.supportTitle) {
                if (!lyricToInOne.isEmpty()) {
                    ZMusic.message.sendTitleMessage("", Conf.lyricColor + lyricToInOne, player);
                }
            }
            if (Conf.supportChat) {
                ZMusic.message.sendNormalMessage(Conf.lyricColor + lyric, player);
                if (Conf.showLyricTr) {
                    if (!lyricTr.isEmpty()) {
                        ZMusic.message.sendNormalMessage(Conf.lyricColor + lyricTr, player);
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
            sb.append(Conf.lyricColor).append(s).append("\n");
        }
        String info = sb.substring(0, sb.length() - 1);
        ZMusic.send.sendAM(player, "[Info]" + info);
    }

    public void stopThis() {
        OtherUtils.resetPlayerStatus(player);
        isStop = true;
    }
}