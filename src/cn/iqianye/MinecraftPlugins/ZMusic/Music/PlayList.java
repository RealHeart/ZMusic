package cn.iqianye.MinecraftPlugins.ZMusic.Music;

import cn.iqianye.MinecraftPlugins.ZMusic.Config.Config;
import cn.iqianye.MinecraftPlugins.ZMusic.Main;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.NeteaseCloudMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Player.PlayerStatus;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.*;
import com.google.gson.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class PlayList {

    public static void savePlayList() {

    }

    /**
     * 获取播放列表(歌单) For 网易云音乐
     *
     * @param url 歌单链接
     * @return 歌曲列表
     */
    public static void importPlayList(String url, Player player) {
        MessageUtils.sendNormalMessage("正在导入歌单，可能时间较长，请耐心等待...", player);
        String playListId = url.split("playlist\\?id=")[1];
        url = "http://music.163.com/weapi/v3/playlist/detail?csrf_token=";
        String text = "{\"id\": " + playListId + ",\"n\": 100000, \"s\": 8}";
        Map data = new HashMap();
        try {
            data = NCMEncryptUtils.getData(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = "encSecKey=" + data.get("encSecKey").toString() + "&params=" + data.get("params").toString();
        String jsonText = NetUtils.getNetStringPOST(url, "http://music.163.com/", s);
        if (jsonText.isEmpty()) {
            LogUtils.sendErrorMessage("错误: 获取歌单信息返回空，请等待2-5分钟后再试。");
            MessageUtils.sendErrorMessage("错误: 获取歌单信息返回空，请等待2-5分钟后再试。", player);
        }
        File configFile = new File(JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlists", player.getName() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        Gson gson = new GsonBuilder().create();
        JsonObject json = gson.fromJson(jsonText, JsonObject.class);
        JsonArray trackIds = json.getAsJsonObject("playlist").getAsJsonArray("trackIds");
        String songs = json.getAsJsonObject("playlist").get("trackCount").getAsString();
        String playListName = json.getAsJsonObject("playlist").get("name").getAsString();
        config.set(playListId + ".info.name", playListName);
        config.set(playListId + ".info.songs", songs);
        for (JsonElement jsonElement : trackIds) {
            JsonObject jsonObject = NeteaseCloudMusic.getMusicUrl(jsonElement.getAsJsonObject().get("id").getAsString());
            String songName = jsonObject.get("name").getAsString();
            int intSongTime = jsonObject.get("time").getAsInt();
            intSongTime = intSongTime / 1000;
            String songTime = String.valueOf(intSongTime);
            String singer = jsonObject.get("singer").getAsString();
            String songId = jsonObject.get("id").getAsString();
            config.set(playListId + "." + songId + ".name", songName);
            config.set(playListId + "." + songId + ".singer", singer);
            config.set(playListId + "." + songId + ".time", songTime);
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.sendErrorMessage(e.getMessage());
        }
        MessageUtils.sendNormalMessage("成功导入(§e" + playListName + "§a)共计§e" + songs + "§a首。", player);
        MessageUtils.sendNormalMessage("其中可能包含无版权或VIP音乐。", player);
    }

    public static void showPlayList(Player player) {
        File configFile = new File(JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlists", player.getName() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        Map<String, Object> rootMap = config.getValues(false);
        MessageUtils.sendNormalMessage("§6=========================================", player);
        int i = 0;
        for (Map.Entry<String, Object> listMap : rootMap.entrySet()) {
            i++;
            String name = config.getString(listMap.getKey() + ".info.name");
            String songs = config.getString(listMap.getKey() + ".info.songs");
            TextComponent message = new TextComponent(Config.prefix + "§a" + i + "." + listMap.getKey() + " : " + name + "(§e共" + songs + "首§a)");
            TextComponent play = new TextComponent("§r[§e播放歌单§r]§r");
            play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist play " + listMap.getKey()));
            play.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放此歌单").create()));
            message.addExtra(" ");
            message.addExtra(play);
            player.spigot().sendMessage(message);
        }
        MessageUtils.sendNormalMessage("§6=========================================", player);
    }

    public static void playPlayList(String id, Player player) {
        File configFile = new File(JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlists", player.getName() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        Map<String, Object> rootMap = config.getConfigurationSection(id).getValues(false);
        MessageUtils.sendNormalMessage("正在检索歌单数据，可能时间较长，请耐心等待...", player);
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<String, Object> listMap : rootMap.entrySet()) {
            if (!listMap.getKey().equalsIgnoreCase("info")) {
                Map<String, Object> songMap = config.getConfigurationSection(id + "." + listMap.getKey()).getValues(false);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", listMap.getKey());
                for (Map.Entry<String, Object> songMap2 : songMap.entrySet()) {
                    jsonObject.addProperty(songMap2.getKey(), songMap2.getValue().toString());
                }

                jsonArray.add(jsonObject);
            }
        }
        MessageUtils.sendNormalMessage("检索完毕，开始播放.", player);
        MusicUtils.stopSelf(player);
        OtherUtils.resetPlayerStatus(player);
        PlayerStatus.setPlayerPlayStatus(player, true);
        PlayerStatus.setPlayerMusicName(player, "获取中...");
        PlayerStatus.setPlayerMaxTime(player, 0);
        PlayerStatus.setPlayerCurrentTime(player, 0);
        LyricSendTimer lyricSendTimer = new LyricSendTimer();
        lyricSendTimer.player = player;
        lyricSendTimer.isPlayList = true;
        lyricSendTimer.playList = jsonArray;
        lyricSendTimer.isActionBar = Config.supportActionBar;
        lyricSendTimer.isBoosBar = Config.supportBossBar;
        lyricSendTimer.isTitle = Config.supportTitle;
        lyricSendTimer.isChat = Config.supportChat;
        Timer timer = PlayerStatus.getPlayerTimer(player);
        if (timer != null) {
            timer.cancel();
            timer = new Timer();
            timer.schedule(lyricSendTimer, 1000L, 1000L);
            PlayerStatus.setPlayerTimer(player, timer);
        } else {
            timer = new Timer();
            timer.schedule(lyricSendTimer, 1000L, 1000L);
            PlayerStatus.setPlayerTimer(player, timer);
        }
    }
}
