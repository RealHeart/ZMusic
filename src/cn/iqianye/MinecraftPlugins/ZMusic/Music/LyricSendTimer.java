package cn.iqianye.MinecraftPlugins.ZMusic.Music;

import cn.iqianye.MinecraftPlugins.ZMusic.Config.Config;
import cn.iqianye.MinecraftPlugins.ZMusic.Other.Val;
import cn.iqianye.MinecraftPlugins.ZMusic.Player.PlayerStatus;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.MessageUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.MusicUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.OtherUtils;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class LyricSendTimer extends TimerTask {
    public Player player;
    public List<Map<Integer, String>> list;
    public int maxTime;
    public String name;
    public String url;
    public boolean isBoosBar;
    public boolean isActionBar;
    public boolean isTitle;
    public boolean isChat;
    public boolean isPlayList = false;
    public JsonArray playList;
    boolean playListSetEd = false;
    int playListLocation = 0;
    int maxPlayListLocation = 1;
    BossBar bossBar;
    TextComponent textComponent;
    int time = 0;

    @Override
    public void run() {
        time++;
        PlayerStatus.setPlayerCurrentTime(player, time);
        if (player.isOnline()) {

            if (isPlayList) {
                if (!playListSetEd) {
                    time--;
                    String tempId = playList.get(0).getAsJsonObject().get("id").getAsString();
                    Gson gson = new GsonBuilder().create();
                    JsonObject tempUrlJson = gson.fromJson(NetUtils.getNetString(Val.apiRoot + "song/url?id=" + tempId + "&br=320000&" +
                            "cookie=" + Val.neteaseCookie, null), JsonObject.class);
                    String tempUrl = tempUrlJson.get("data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
                    String tempName = playList.get(0).getAsJsonObject().get("name").getAsString() + "(" + playList.get(0).getAsJsonObject().get("singer").getAsString() + ")";
                    if (tempUrl == null) {
                        MessageUtils.sendErrorMessage("错误，无法获取当前音乐§r[§e" + tempName + "§r]§c，可能音乐无版权或为VIP音乐.", player);
                        return;
                    }
                    MessageUtils.sendNormalMessage("开始播放§r[§e" + tempName + "§r]§a.", player);
                    int tempMaxTime = playList.get(0).getAsJsonObject().get("time").getAsInt();
                    PlayerStatus.setPlayerMusicName(player, tempName);
                    PlayerStatus.setPlayerMaxTime(player, tempMaxTime);
                    maxTime = tempMaxTime;
                    name = tempName;
                    url = tempUrl;
                    list = OtherUtils.getLyricFor163(tempId);
                    playListLocation++;
                    maxPlayListLocation = playList.size();
                    playListSetEd = true;
                    MusicUtils.playSelf(tempUrl, player);
                }
            }


            if (isBoosBar) {
                if (bossBar == null) {
                    bossBar = PlayerStatus.getPlayerBoosBar(player);
                    if (bossBar == null) {
                        textComponent = new TextComponent(name);
                        textComponent.setColor(ChatColor.AQUA);
                        textComponent.setBold(true);
                        bossBar = BossBarAPI.addBar(player, textComponent, BossBarAPI.Color.BLUE, BossBarAPI.Style.NOTCHED_20, 1.0F, maxTime, 20);
                        PlayerStatus.setPlayerBoosBar(player, bossBar);
                    } else {
                        bossBar.removePlayer(player);
                        textComponent = new TextComponent(name);
                        textComponent.setColor(ChatColor.AQUA);
                        textComponent.setBold(true);
                        bossBar = BossBarAPI.addBar(player, textComponent, BossBarAPI.Color.BLUE, BossBarAPI.Style.NOTCHED_20, 1.0F, maxTime, 20);
                        PlayerStatus.setPlayerBoosBar(player, bossBar);
                    }
                }
            }
            if (PlayerStatus.getPlayerPlayStatus(player)) {
                if (time != maxTime) {
                    if (!list.isEmpty()) {
                        for (Map<Integer, String> map : list) {
                            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                                if (entry.getKey() == time) {
                                    if (!entry.getValue().isEmpty()) {
                                        if (Config.lyricEnable) {
                                            PlayerStatus.setPlayerLyric(player, entry.getValue());
                                            if (isBoosBar) {
                                                textComponent.setText(entry.getValue());
                                                bossBar.setTitle(textComponent);
                                            }
                                            if (isActionBar) {
                                                ActionBarAPI.sendActionBar(player, "§b§l" + entry.getValue());
                                            }
                                            if (isTitle) {
                                                try {
                                                    player.sendTitle("", "§b" + entry.getValue(), 0, 200, 20);
                                                } catch (NoSuchMethodError e) {
                                                    player.sendTitle("", "§b" + entry.getValue());
                                                }
                                            }
                                            if (isChat) {
                                                MessageUtils.sendNormalMessage("§b" + entry.getValue(), player);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (isPlayList) {
                        if (playListLocation == maxPlayListLocation) {
                            OtherUtils.resetPlayerStatus(player);
                            if (isBoosBar) {
                                bossBar.removePlayer(player);
                            }
                            cancel();
                        } else {
                            String tempId = playList.get(playListLocation).getAsJsonObject().get("id").getAsString();
                            Gson gson = new GsonBuilder().create();
                            JsonObject tempUrlJson = gson.fromJson(NetUtils.getNetString(Val.apiRoot + "song/url?id=" + tempId + "&br=320000&" +
                                    "cookie=" + Val.neteaseCookie, null), JsonObject.class);
                            String tempUrl = tempUrlJson.get("data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
                            String tempName = playList.get(playListLocation).getAsJsonObject().get("name").getAsString() + "(" + playList.get(playListLocation).getAsJsonObject().get("singer").getAsString() + ")";
                            if (tempUrl == null) {
                                MessageUtils.sendErrorMessage("错误，无法获取当前音乐§r[§e" + tempName + "§r]§c，可能音乐无版权或为VIP音乐.", player);
                                return;
                            }
                            MessageUtils.sendNormalMessage("开始播放§r[§e" + tempName + "§r]§a.", player);
                            int tempMaxTime = playList.get(playListLocation).getAsJsonObject().get("time").getAsInt();
                            PlayerStatus.setPlayerMusicName(player, tempName);
                            PlayerStatus.setPlayerMaxTime(player, tempMaxTime);
                            maxTime = tempMaxTime;
                            name = tempName;
                            url = tempUrl;
                            list = OtherUtils.getLyricFor163(tempId);
                            playListLocation++;
                            playListSetEd = true;
                            MusicUtils.stopSelf(player);
                            MusicUtils.playSelf(tempUrl, player);
                            if (isBoosBar) {
                                bossBar.removePlayer(player);
                                textComponent.setText(name);
                                bossBar = BossBarAPI.addBar(player, textComponent, BossBarAPI.Color.BLUE, BossBarAPI.Style.NOTCHED_20, 1.0F, maxTime, 20);
                                PlayerStatus.setPlayerBoosBar(player, bossBar);
                            }
                            time = 0;
                        }
                    } else if (PlayerStatus.getPlayerLoopPlay(player) != null && PlayerStatus.getPlayerLoopPlay(player)) {
                        MusicUtils.stopSelf(player);
                        MusicUtils.playSelf(url, player);
                        if (isBoosBar) {
                            bossBar.removePlayer(player);
                            textComponent.setText(name);
                            bossBar = BossBarAPI.addBar(player, textComponent, BossBarAPI.Color.BLUE, BossBarAPI.Style.NOTCHED_20, 1.0F, maxTime, 20);
                            PlayerStatus.setPlayerBoosBar(player, bossBar);
                        }
                        time = 0;
                    } else {
                        OtherUtils.resetPlayerStatus(player);
                        if (isBoosBar) {
                            bossBar.removePlayer(player);
                        }
                        cancel();
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

    }


}