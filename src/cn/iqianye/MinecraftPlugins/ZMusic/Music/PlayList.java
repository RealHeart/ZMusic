package cn.iqianye.MinecraftPlugins.ZMusic.Music;

import cn.iqianye.MinecraftPlugins.ZMusic.Main;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.LogUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.MessageUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;
import com.google.gson.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PlayList {

    public static void savePlayList() {

    }

    /**
     * 获取播放列表(歌单) For 网易云音乐
     *
     * @param url 歌单链接
     * @return 歌曲列表
     */
    public static List<String> importPlayList(String url, Player player) {
        MessageUtils.sendNormalMessage("正在导入歌单，可能时间较长，请耐心等待...", player);
        String playListId = url.split("playlist\\?id=")[1];
        url = "http://music.163.com/api/playlist/detail?id=" + playListId;
        String jsonText = NetUtils.getNetString(url, null);
        File configFile = new File(JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlists", player.getName() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        Gson gson = new GsonBuilder().create();
        JsonObject json = gson.fromJson(jsonText, JsonObject.class);
        JsonArray tracks = json.getAsJsonObject("result").getAsJsonArray("tracks");
        String songs = json.getAsJsonObject("result").get("trackCount").getAsString();
        String playListName = json.getAsJsonObject("result").get("name").getAsString();
        config.set(playListId + ".info.name", playListName);
        for (JsonElement jsonElement : tracks) {
            String songName = jsonElement.getAsJsonObject().get("name").getAsString();
            JsonArray artists = jsonElement.getAsJsonObject().get("artists").getAsJsonArray();
            String songId = jsonElement.getAsJsonObject().get("id").getAsString();
            String artist = "";
            for (JsonElement js : artists) {
                artist += js.getAsJsonObject().get("name").getAsString() + "/";
            }
            artist = artist.substring(0, artist.length() - 1);
            config.set(playListId + "." + songId + ".name", songName);
            config.set(playListId + "." + songId + ".artist", artist);
            config.set(playListId + "." + songId + ".url", "http://music.163.com/song/media/outer/url?id=" + songId + ".mp3");
            config.set(playListId + "." + songId + ".playing", false);
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.sendErrorMessage(e.getMessage());
        }
        MessageUtils.sendNormalMessage("成功导入(§e" + playListName + "§a)共计§e" + songs + "§a首。", player);
        MessageUtils.sendNormalMessage("其中可能包含无版权或VIP音乐。", player);
        return null;
    }

    public static void playPlayList(String id, Player player) {
        File configFile = new File(JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlists", player.getName() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        Map<String, Object> rootMap = config.getValues(true);
        for (Map.Entry<String, Object> listMap : rootMap.entrySet()) {
            if (listMap.getValue() instanceof String) {
                LogUtils.sendNormalMessage(listMap.getKey() + " : " + listMap.getValue().toString());
            }
        }
    }
}
