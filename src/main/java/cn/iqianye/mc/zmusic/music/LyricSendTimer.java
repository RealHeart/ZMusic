package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.Main;
import cn.iqianye.mc.zmusic.api.AdvancementAPI;
import cn.iqianye.mc.zmusic.api.BossBar;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.MessageUtils;
import cn.iqianye.mc.zmusic.utils.MusicUtils;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class LyricSendTimer extends TimerTask {
    public Player player;
    public List<Map<Integer, String>> lyric;
    public List<Map<Integer, String>> lyricTr;
    public int maxTime;
    public String name;
    public String url;
    public boolean isBoosBar;
    public boolean isActionBar;
    public boolean isTitle;
    public boolean isChat;
    BossBar bossBar;
    int time = 0;

    @Override
    public void run() {
        time++;
        PlayerStatus.setPlayerCurrentTime(player, time);
        if (player.isOnline()) {
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
                if (!(time > maxTime)) {
                    if (!lyric.isEmpty()) {
                        for (int i = 0; i < lyric.size(); i++) {
                            Map<Integer, String> map = lyric.get(i);
                            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                                if (entry.getKey() == time) {
                                    if (!entry.getValue().isEmpty()) {
                                        if (Config.lyricEnable) {
                                            if (Config.showLyricTr) {
                                                if (!lyricTr.isEmpty()) {
                                                    PlayerStatus.setPlayerLyric(player, entry.getValue() + "(" + lyricTr.get(i).get(entry.getKey()) + ")");
                                                } else {
                                                    PlayerStatus.setPlayerLyric(player, entry.getValue());
                                                }
                                            } else {
                                                PlayerStatus.setPlayerLyric(player, entry.getValue());
                                            }
                                            if (isBoosBar) {
                                                if (Config.showLyricTr) {
                                                    if (!lyricTr.isEmpty()) {
                                                        bossBar.setTitle("§b§l" + entry.getValue() + "(" + lyricTr.get(i).get(entry.getKey()) + ")");
                                                    } else {
                                                        bossBar.setTitle("§b§l" + entry.getValue());
                                                    }
                                                } else {
                                                    bossBar.setTitle("§b§l" + entry.getValue());
                                                }
                                            }
                                            if (isActionBar) {
                                                if (Config.showLyricTr) {
                                                    if (!lyricTr.isEmpty()) {
                                                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§b§l" + entry.getValue() + "(" + lyricTr.get(i).get(entry.getKey()) + ")"));
                                                    } else {
                                                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§b§l" + entry.getValue()));
                                                    }
                                                } else {
                                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§b§l" + entry.getValue()));
                                                }
                                            }
                                            if (isTitle) {
                                                if (Config.showLyricTr) {
                                                    if (!lyricTr.isEmpty()) {
                                                        try {
                                                            player.sendTitle("", "§b" + entry.getValue() + "(" + lyricTr.get(i).get(entry.getKey()) + ")", 0, 200, 20);
                                                        } catch (NoSuchMethodError e) {
                                                            player.sendTitle("", "§b" + entry.getValue() + "(" + lyricTr.get(i).get(entry.getKey()) + ")");
                                                        }
                                                    } else {
                                                        try {
                                                            player.sendTitle("", "§b" + entry.getValue(), 0, 200, 20);
                                                        } catch (NoSuchMethodError e) {
                                                            player.sendTitle("", "§b" + entry.getValue());
                                                        }
                                                    }
                                                } else {
                                                    try {
                                                        player.sendTitle("", "§b" + entry.getValue(), 0, 200, 20);
                                                    } catch (NoSuchMethodError e) {
                                                        player.sendTitle("", "§b" + entry.getValue());
                                                    }
                                                }
                                            }
                                            if (isChat) {
                                                if (Config.showLyricTr) {
                                                    MessageUtils.sendNormalMessage("§b" + entry.getValue(), player);
                                                    if (!lyricTr.isEmpty()) {
                                                        MessageUtils.sendNormalMessage("§b" + lyricTr.get(i).get(entry.getKey()), player);
                                                    }
                                                } else {
                                                    MessageUtils.sendNormalMessage("§b" + entry.getValue(), player);
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    OtherUtils.resetPlayerStatus(player);
                    if (isBoosBar) {
                        bossBar.removePlayer(player);
                    }
                    cancel();
                }
            } else {
                OtherUtils.resetPlayerStatus(player);
                if (isBoosBar) {
                    bossBar.removePlayer(player);
                }
                cancel();
            }
        } else {
            OtherUtils.resetPlayerStatus(player);
            if (isBoosBar) {
                bossBar.removePlayer(player);
            }
            cancel();
        }
    }
}