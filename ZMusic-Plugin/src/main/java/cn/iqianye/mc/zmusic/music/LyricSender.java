package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.api.ProgressBar;
import cn.iqianye.mc.zmusic.api.bossbar.*;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.data.PlayerData;
import cn.iqianye.mc.zmusic.nms.ActionBar;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
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
    //Version version = new Version();
    ActionBar actionBar = null;
    boolean is1_8 = false;
    StringBuilder hudInfo = new StringBuilder();
    ProgressBar progressBar;
    private boolean isStop = false;

    public void init() {
        PlayerData.setPlayerPlayStatus(player, true);
        PlayerData.setPlayerMusicName(player, name);
        PlayerData.setPlayerMusicSinger(player, singer);
        PlayerData.setPlayerPlaySource(player, src);
        PlayerData.setPlayerPlatform(player, platform);
        PlayerData.setPlayerMaxTime(player, maxTime);
        PlayerData.setPlayerCurrentTime(player, 0L);
        progressBar = new ProgressBar('■', '□', maxTime);
        /*
        if (version.isEquals("1.8")) {
            ZMusic.log.sendDebugMessage("[播放器](ID:" + getId() + ") 检测为服务端版本1.8,使用NMS发送ActionBar.");
            actionBar = new ActionBar_1_8_R3();
            is1_8 = true;
        }
        */
        if (Config.supportBossBar) {
            bossBar = PlayerData.getPlayerBoosBar(player);
            if (bossBar == null) {
                initBossBar();
            } else {
                bossBar.removePlayer(player);
                initBossBar();
            }
        }
        if (Config.supportHud) {
            ZMusic.send.sendAM(player, Config.hudInfoX, Config.hudInfoY, Config.hudLyricX, Config.hudLyricY);
            hudInfo.append("歌名: ").append(name).append("\n");
            hudInfo.append("歌手: ").append(singer).append("\n");
            hudInfo.append("平台: ").append(platform).append("\n");
            hudInfo.append("来源: ").append(src).append("\n");
            hudInfo.append("进度: 00:00/").append(OtherUtils.formatTime(maxTime / 100));
            if (isPlayList) {
                hudInfo.append("\n下一首: ").append(nextMusicName);
            }
        }

    }

    private void initBossBar() {
        if (ZMusic.isBC) {
            bossBar = new BossBarBC(player, Config.lyricColor + fullName, BarColor.BLUE, BarStyle.SEGMENTED_20, maxTime);
        } else {
            bossBar = new BossBarBukkit(player, Config.lyricColor + fullName, BarColor.BLUE, BarStyle.SEGMENTED_20, maxTime);
        }
        bossBar.showTitle();
        PlayerData.setPlayerBoosBar(player, bossBar);
    }

    @Override
    public void run() {
        while (!isStop) {
            if (ZMusic.player.isOnline(player)) {
                time++;
                if (PlayerData.getPlayerPlayStatus(player)) {
                    PlayerData.setPlayerCurrentTime(player, time);
                    if (Config.supportHud) {
                        updateHudTime(time);
                    }
                    if (!(time > maxTime)) {
                        if (Config.lyricEnable) {
                            ZMusic.runTask.runAsync(() -> {
                                if (lyric != null) {
                                    JsonObject j = lyric.getAsJsonObject(String.valueOf(time));
                                    if (j != null) {
                                        sendLyric(j);
                                        if (Config.supportHud) {
                                            sendHud(j);
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        OtherUtils.resetPlayerStatus(player);
                        if (Config.supportHud) {
                            ZMusic.send.sendAM(player, "[Lyric]");
                            ZMusic.send.sendAM(player, "[Info]");
                            hudInfo = new StringBuilder();
                        }
                        if (!isPlayList) {
                            ZMusic.log.sendDebugMessage("[播放器](ID:" + getId() + ") 非歌单模式 检测循环播放状态");
                            Boolean loop = PlayerData.getPlayerLoopPlay(player);
                            if (loop != null && loop) {
                                ZMusic.log.sendDebugMessage("[播放器](ID:" + getId() + ") 循环播放开启 重新播放当前音乐");
                                time = -1;
                                ZMusic.music.play(url, player);
                                init();
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
            if (Config.showLyricTr) {
                if (!lyricTr.isEmpty()) {
                    lyricToInOne = lyric + "(" + lyricTr + ")";
                }
            }
            if (!lyricToInOne.isEmpty()) {
                PlayerData.setPlayerLyric(player, lyricToInOne);
            }
            if (Config.supportBossBar) {
                if (!lyricToInOne.isEmpty()) {
                    bossBar.setTitle(Config.lyricColor + lyricToInOne);
                }
            }
            if (Config.supportActionBar) {
                if (!lyricToInOne.isEmpty()) {
                    if (!is1_8) {
                        ZMusic.message.sendActionBarMessage(new TextComponent(Config.lyricColor + lyricToInOne), player);
                    } else {
                        actionBar.sendActionBar(player, Config.lyricColor + lyricToInOne);
                    }
                }
            }
            if (Config.supportTitle) {
                if (!lyricToInOne.isEmpty()) {
                    ZMusic.message.sendTitleMessage("", Config.lyricColor + lyricToInOne, player);
                }
            }
            if (Config.supportChat) {
                ZMusic.message.sendNormalMessage(Config.lyricColor + lyric, player);
                if (Config.showLyricTr) {
                    if (!lyricTr.isEmpty()) {
                        ZMusic.message.sendNormalMessage(Config.lyricColor + lyricTr, player);
                    }
                }
            }
        }
    }

    void updateHudTime(long time) {
        String str = hudInfo.toString();
        String[] strs = str.split("\n");
        progressBar.setProgress(time);
        strs[4] = "进度: " + OtherUtils.formatTime(time) + "/" + OtherUtils.formatTime(maxTime) +
                " " + progressBar.toString();
        StringBuilder sb = new StringBuilder();
        hudInfo = new StringBuilder();
        for (String s : strs) {
            hudInfo.append(s).append("\n");
            sb.append(Config.lyricColor).append(s).append("\n");
        }
        String info = sb.substring(0, sb.length() - 1);
        ZMusic.send.sendAM(player, "[Info]" + info);
    }

    public void stopThis() {
        OtherUtils.resetPlayerStatus(player);
        isStop = true;
    }
}