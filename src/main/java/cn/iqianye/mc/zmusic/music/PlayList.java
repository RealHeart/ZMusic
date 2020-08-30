package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.Main;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.music.searchSource.NeteaseCloudMusic;
import cn.iqianye.mc.zmusic.music.searchSource.QQMusic;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.*;
import com.google.gson.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PlayList {


    /**
     * 歌单子命令
     *
     * @param args    参数
     * @param cmdName 命令名称
     * @param player  玩家
     */
    public static void subCommand(String[] args, String cmdName, Player player) {

        switch (args[1]) {
            case "type":
                switch (args[2]) {
                    case "normal":
                        PlayerStatus.setPlayerPlayListType(player, args[2]);
                        MessageUtils.sendNormalMessage("成功将歌单播放类型设置为[§e顺序播放§a].", player);
                    case "loop":
                        PlayerStatus.setPlayerPlayListType(player, args[2]);
                        MessageUtils.sendNormalMessage("成功将歌单播放类型设置为[§e循环播放§a].", player);
                    case "random":
                        PlayerStatus.setPlayerPlayListType(player, args[2]);
                        MessageUtils.sendNormalMessage("成功将歌单播放类型设置为[§e随机播放§a].", player);
                        break;
                    default:
                        MessageUtils.sendErrorMessage("错误: 未知的播放类型", player);
                        MessageUtils.sendErrorMessage("/" + cmdName + " playlist type normal - 顺序播放", player);
                        MessageUtils.sendErrorMessage("/" + cmdName + " playlist type loop - 循环播放", player);
                        MessageUtils.sendErrorMessage("/" + cmdName + " playlist type random - 随机播放", player);
                        break;
                }
                return;
            default:
                break;
        }

        switch (args[2]) {
            case "import":
                importPlayList(args[3], args[1], player);
                break;
            case "list":
                showPlayList(args[1], player);
                break;
            case "play":
                OtherUtils.resetPlayerStatus(player);
                playPlayList(args[3], args[1], player);
                break;
            case "update":
                updatePlayListInfo(args[1], player);
                break;
            default:
                HelpUtils.sendHelp(cmdName, "playlist", player);
                break;
        }
    }

    /**
     * 导入歌单
     *
     * @param url      链接
     * @param platform 平台
     * @param player   玩家
     */
    public static void importPlayList(String url, String platform, Player player) {
        try {
            JsonObject json;
            String id;
            String platformName;
            switch (platform) {
                case "163":
                case "netease":
                    id = url.split("playlist\\?id=")[1].split("&")[0];
                    json = NeteaseCloudMusic.getMusicSongList(id);
                    platform = "netease";
                    platformName = "网易云音乐";
                    break;
                case "qq":
                    id = url.split("playlist/")[1].split(".html")[0];
                    LogUtils.sendNormalMessage(id);
                    json = QQMusic.getMusicSongList(id);
                    platformName = "QQ音乐";
                    break;
                default:
                    MessageUtils.sendErrorMessage("错误：未知的平台", player);
                    return;
            }
            String name = json.get("name").getAsString();
            int songs = json.get("songs").getAsInt();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            byte[] data = gson.toJson(json).getBytes();
            String filePath = JavaPlugin.getPlugin(Main.class).getDataFolder()
                    + "/playlist/" + platform + "/" + player.getName();
            File path = new File(filePath);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(filePath, id + ".json");
            file = file.getAbsoluteFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream.write(data);
            fileOutputStream.flush();
            fileOutputStream.close();
            MessageUtils.sendNormalMessage("成功在" + platformName + "导入(§e" + name + "§a)共计§e" + songs + "§a首。", player);
            MessageUtils.sendNormalMessage("其中可能包含无版权或VIP音乐。", player);
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.sendErrorMessage("导入失败", player);
        }
    }

    private static void showPlayList(String platform, Player player) {
        ArrayList<String> files;
        String filePath = JavaPlugin.getPlugin(Main.class).getDataFolder()
                + "/playlist/" + platform + "/" + player.getName();
        switch (platform) {
            case "163":
            case "netease":
                platform = "netease";
                filePath = JavaPlugin.getPlugin(Main.class).getDataFolder()
                        + "/playlist/" + platform + "/" + player.getName();
                files = OtherUtils.queryFileNames(filePath);
                break;
            case "qq":
                files = OtherUtils.queryFileNames(filePath);
                break;
            default:
                MessageUtils.sendErrorMessage("错误：未知的平台", player);
                return;
        }
        MessageUtils.sendNormalMessage("§6=========================================", player);
        int i = 0;
        for (String s : files) {
            i++;
            Gson gson = new GsonBuilder().create();
            File file = new File(filePath, s);
            String id = s.split(".json")[0];
            JsonObject j = gson.fromJson(OtherUtils.readFileToString(file), JsonObject.class);
            String name = j.get("name").getAsString();
            String songs = j.get("songs").getAsString();
            TextComponent message = new TextComponent(Config.prefix + "§a" + i + "." + id + " : " + name + "(§e共" + songs + "首§a)");
            TextComponent play = new TextComponent("§r[§e播放歌单§r]§r");
            play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " play " + id));
            play.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放此歌单").create()));
            message.addExtra(" ");
            message.addExtra(play);
            player.spigot().sendMessage(message);
        }
        MessageUtils.sendNormalMessage("§6=========================================", player);
    }

    private static void playPlayList(String id, String platform, Player player) {
        File file = new File(JavaPlugin.getPlugin(Main.class).getDataFolder()
                + "/playlist/" + platform + "/" + player.getName(), id + ".json");
        Gson gson = new GsonBuilder().create();
        JsonObject json = gson.fromJson(OtherUtils.readFileToString(file), JsonObject.class);
        String name = json.get("name").getAsString();
        JsonArray list = json.get("list").getAsJsonArray();
        List<JsonObject> playList = new ArrayList<JsonObject>();
        for (JsonElement j : list) {
            playList.add(j.getAsJsonObject());
        }
        PlayListPlayer plp = PlayerStatus.getPlayerPlayListPlayer(player);
        if (plp != null) {
            plp.isStop = true;
            PlayerStatus.setPlayerPlayListPlayer(player, null);
            OtherUtils.resetPlayerStatus(player);
            MusicUtils.stopSelf(player);
        }
        PlayListPlayer playListPlayer = new PlayListPlayer();
        String type = PlayerStatus.getPlayerPlayListType(player);
        if (type == null || type.isEmpty()) {
            type = "normal";
        }
        playListPlayer.playListName = name;
        playListPlayer.type = type;
        playListPlayer.playList = playList;
        playListPlayer.platform = platform;
        playListPlayer.player = player;
        playListPlayer.runTaskAsynchronously(JavaPlugin.getPlugin(Main.class));
        PlayerStatus.setPlayerPlayListPlayer(player, playListPlayer);
    }

    private static void updatePlayListInfo(String platform, Player player) {
        String filePath;
        File path;
        switch (platform) {
            case "163":
            case "netease":
                platform = "netease";
                break;
            default:
                break;
        }
        if (platform.equalsIgnoreCase("netease")) {
            filePath = JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlist/" + platform + "/" + player.getName();
            path = new File(filePath);
            if (path.exists()) {
                platform = "netease";
                ArrayList<String> files = OtherUtils.queryFileNames(filePath);
                for (String s : files) {
                    MessageUtils.sendNormalMessage("§6=========================================", player);
                    PlayList.importPlayList("playlist?id=" + s.split(".json")[0], platform, player);
                }
                MessageUtils.sendNormalMessage("§6=========================================", player);
            }
        } else if (platform.equalsIgnoreCase("qq")) {
            filePath = JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlist/" + platform + "/" + player.getName();
            path = new File(filePath);
            if (path.exists()) {
                ArrayList<String> files = OtherUtils.queryFileNames(filePath);
                for (String s : files) {
                    MessageUtils.sendNormalMessage("§6=========================================", player);
                    PlayList.importPlayList("playlist/" + s.split(".json")[0], platform, player);
                }
                MessageUtils.sendNormalMessage("§6=========================================", player);
            }
        }
    }
}
