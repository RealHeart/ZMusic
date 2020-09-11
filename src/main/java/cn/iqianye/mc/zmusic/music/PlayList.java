package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.Main;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.music.searchSource.NeteaseCloudMusic;
import cn.iqianye.mc.zmusic.music.searchSource.QQMusic;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.HelpUtils;
import cn.iqianye.mc.zmusic.utils.MessageUtils;
import cn.iqianye.mc.zmusic.utils.MusicUtils;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import com.google.gson.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
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
                        break;
                    case "loop":
                        PlayerStatus.setPlayerPlayListType(player, args[2]);
                        MessageUtils.sendNormalMessage("成功将歌单播放类型设置为[§e循环播放§a].", player);
                        break;
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
            case "global":
                String platform = "";
                if (args.length >= 3) {
                    switch (args[2]) {
                        case "qq":
                            platform = "global/qq";
                            break;
                        case "163":
                        case "netease":
                            platform = "global/netease";
                            break;
                        default:
                            MessageUtils.sendErrorMessage("错误：未知的平台", player);
                            return;
                    }
                } else {
                    HelpUtils.sendHelp(cmdName, "playlist", player);
                    return;
                }
                if (args.length >= 4) {
                    switch (args[3]) {
                        case "import":
                            if (player.hasPermission("zmusic.admin") || player.isOp()) {
                                importPlayList(args[4], platform, player, true);
                                break;
                            } else {
                                MessageUtils.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", player);
                                break;
                            }
                        case "list":
                            showPlayList(platform, player, true);
                            break;
                        case "play":
                            OtherUtils.resetPlayerStatusSelf(player);
                            playPlayList(args[4], platform, player, new ArrayList<>(), true);
                            break;
                        case "playall":
                            List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                            OtherUtils.resetPlayerStatusAll(players);
                            playPlayList(args[4], platform, null, players, true);
                        case "update":
                            if (player.hasPermission("zmusic.admin") || player.isOp()) {
                                updatePlayListInfo(platform, player, true);
                                break;
                            } else {
                                MessageUtils.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", player);
                                break;
                            }
                        default:
                            HelpUtils.sendHelp(cmdName, "playlist", player);
                            break;
                    }
                } else {
                    HelpUtils.sendHelp(cmdName, "playlist", player);
                    return;
                }
                return;
            default:
                break;
        }
        if (args.length >= 3) {
            switch (args[2]) {
                case "import":
                    importPlayList(args[3], args[1], player, false);
                    break;
                case "list":
                    showPlayList(args[1], player, false);
                    break;
                case "play":
                    OtherUtils.resetPlayerStatusSelf(player);
                    playPlayList(args[3], args[1], player, new ArrayList<>(), false);
                    break;
                case "update":
                    updatePlayListInfo(args[1], player, false);
                    break;
                default:
                    HelpUtils.sendHelp(cmdName, "playlist", player);
                    break;
            }
        } else {
            HelpUtils.sendHelp(cmdName, "playlist", player);
            return;
        }
    }

    /**
     * 导入歌单
     *
     * @param url      链接
     * @param platform 平台
     * @param player   玩家
     */
    public static void importPlayList(String url, String platform, Player player, boolean isGlobal) {
        try {
            JsonObject json;
            String id;
            String platformName;
            switch (platform) {
                case "163":
                case "netease":
                case "global/netease":
                    id = url.split("playlist\\?id=")[1].split("&")[0];
                    json = NeteaseCloudMusic.getMusicSongList(id);
                    platform = "netease";
                    platformName = "网易云音乐";
                    break;
                case "qq":
                case "global/qq":
                    id = url.split("playlist/")[1].split(".html")[0];
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
            String filePath = "";
            if (isGlobal) {
                filePath = JavaPlugin.getPlugin(Main.class).getDataFolder()
                        + "/playlist/" + platform;
            } else {
                filePath = JavaPlugin.getPlugin(Main.class).getDataFolder()
                        + "/playlist/" + platform + "/" + player.getName();
            }
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
            MessageUtils.sendErrorMessage("导入失败,请检查链接格式是否正确.", player);
            MessageUtils.sendErrorMessage("QQ音乐: https://y.qq.com/n/yqq/playlist/1937967578.html", player);
            MessageUtils.sendErrorMessage("网易云音乐: https://music.163.com/#/my/m/music/playlist?id=363046232", player);
        }
    }

    private static void showPlayList(String platform, Player player, boolean isGlobal) {
        ArrayList<String> files;
        String filePath = "";
        if (isGlobal) {
            filePath = JavaPlugin.getPlugin(Main.class).getDataFolder()
                    + "/playlist/" + platform;
        } else {
            filePath = JavaPlugin.getPlugin(Main.class).getDataFolder()
                    + "/playlist/" + platform + "/" + player.getName();
        }
        switch (platform) {
            case "163":
            case "netease":
            case "global/netease":
                platform = "netease";
                if (isGlobal) {
                    filePath = JavaPlugin.getPlugin(Main.class).getDataFolder()
                            + "/playlist/" + platform;
                } else {
                    filePath = JavaPlugin.getPlugin(Main.class).getDataFolder()
                            + "/playlist/" + platform + "/" + player.getName();
                }
                files = OtherUtils.queryFileNames(filePath);
                break;
            case "global/qq":
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
            if (isGlobal) {
                play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform.replace("global/", "") + " play " + id));
            } else {
                play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " play " + id));
            }
            play.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放此歌单").create()));
            message.addExtra(" ");
            message.addExtra(play);
            if (isGlobal) {
                if (player.hasPermission("zmusic.admin")) {
                    TextComponent playAll = new TextComponent("§r[§e全服播放歌单§r]§r");
                    if (isGlobal) {
                        playAll.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform.replace("global/", "") + " play " + id));
                    } else {
                        playAll.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " playAll " + id));
                    }
                    playAll.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击为全体玩家播放此歌单").create()));
                    message.addExtra(" ");
                    message.addExtra(playAll);
                }
            }
            player.spigot().sendMessage(message);
        }
        MessageUtils.sendNormalMessage("§6=========================================", player);
    }

    private static void playPlayList(String id, String platform, Player p, List<Player> players, boolean isGlobal) {
        if (p != null) {
            players.add(p);
        }
        for (Player player : players) {
            String filePath = "";
            if (isGlobal) {
                filePath = JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlist/" + platform;
                platform = platform.replace("global/", "");
            } else {
                filePath = JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlist/" + platform + "/" + player.getName();
            }
            File file = new File(filePath, id + ".json");
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
                OtherUtils.resetPlayerStatusSelf(player);
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
    }

    private static void updatePlayListInfo(String platform, Player player, boolean isGlobal) {
        String filePath = "";
        File path;
        switch (platform) {
            case "163":
            case "netease":
                platform = "netease";
                break;
            default:
                break;
        }
        if (platform.equalsIgnoreCase("netease") || platform.equalsIgnoreCase("global/netease")) {
            if (isGlobal) {
                filePath = JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlist/" + platform + "/";
            } else {
                filePath = JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlist/" + platform + "/" + player.getName();
            }
            path = new File(filePath);
            if (path.exists()) {
                platform = "netease";
                ArrayList<String> files = OtherUtils.queryFileNames(filePath);
                for (String s : files) {
                    MessageUtils.sendNormalMessage("§6=========================================", player);
                    PlayList.importPlayList("playlist?id=" + s.split(".json")[0], platform, player, false);
                }
                MessageUtils.sendNormalMessage("§6=========================================", player);
            }
        } else if (platform.equalsIgnoreCase("qq") || platform.equalsIgnoreCase("global/qq")) {
            if (isGlobal) {
                filePath = JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlist/" + platform + "/";
            } else {
                filePath = JavaPlugin.getPlugin(Main.class).getDataFolder() + "/playlist/" + platform + "/" + player.getName();
            }
            path = new File(filePath);
            if (path.exists()) {
                ArrayList<String> files = OtherUtils.queryFileNames(filePath);
                for (String s : files) {
                    MessageUtils.sendNormalMessage("§6=========================================", player);
                    PlayList.importPlayList("playlist/" + s.split(".json")[0], platform, player, false);
                }
                MessageUtils.sendNormalMessage("§6=========================================", player);
            }
        }
    }
}
