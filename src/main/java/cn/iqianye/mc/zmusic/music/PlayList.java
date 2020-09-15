package cn.iqianye.mc.zmusic.music;

import cn.iqianye.mc.zmusic.ZMusicBukkit;
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
            case "next":
                MessageUtils.sendNormalMessage("正在切换到下一首歌曲,请稍后...", player);
                PlayListPlayer plp = PlayerStatus.getPlayerPlayListPlayer(player);
                if (plp != null) {
                    plp.nextMusic = true;
                } else {
                    MessageUtils.sendErrorMessage("错误: 当前未在播放歌单", player);
                }
                return;
            case "prev":
                MessageUtils.sendNormalMessage("正在切换到上一首歌曲,请稍后...", player);
                PlayListPlayer plp2 = PlayerStatus.getPlayerPlayListPlayer(player);
                if (plp2 != null) {
                    plp2.prevMusic = true;
                } else {
                    MessageUtils.sendErrorMessage("错误: 当前未在播放歌单", player);
                }
                return;
            case "jump":
                int jumpSong = 1;
                String id = "";
                try {
                    jumpSong = Integer.parseInt(args[2]);
                } catch (Exception ignored) {
                }
                MessageUtils.sendNormalMessage("正在跳转到当前歌单中ID为§r[§e" + jumpSong + "§r]§a的歌曲,请稍后...", player);
                PlayListPlayer plp3 = PlayerStatus.getPlayerPlayListPlayer(player);
                if (plp3 != null) {
                    try {
                        id = args[3];
                    } catch (Exception ignored) {
                    }
                    if (!id.isEmpty()) {
                        if (!id.equals(plp3.id)) {
                            MessageUtils.sendErrorMessage("错误: 当前未在播放此歌单", player);
                            return;
                        }
                    }
                    plp3.jumpSong = jumpSong;
                    plp3.jumpMusic = true;
                } else {
                    MessageUtils.sendErrorMessage("错误: 当前未在播放歌单", player);
                }
                return;
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
                            showPlayListList(platform, player, true);
                            break;
                        case "show":
                            int start = 0;
                            try {
                                start = Integer.parseInt(args[5]);
                            } catch (Exception ignored) {
                            }
                            showPlayList(args[4], platform, player, true, start);
                            break;
                        case "play":
                            OtherUtils.resetPlayerStatus(player);
                            playPlayList(args[4], platform, player, new ArrayList<>(), true);
                            break;
                        case "playall":
                            List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                            for (Player p : players) {
                                OtherUtils.resetPlayerStatus(p);
                            }
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
                    showPlayListList(args[1], player, false);
                    break;
                case "show":
                    int start = 0;
                    try {
                        start = Integer.parseInt(args[4]);
                    } catch (Exception ignored) {
                    }
                    showPlayList(args[3], args[1], player, false, start);
                    break;
                case "play":
                    OtherUtils.resetPlayerStatus(player);
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
        }
    }

    private static void importPlayList(String url, String platform, Player player, boolean isGlobal) {
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
                filePath = ZMusicBukkit.plugin.getDataFolder()
                        + "/playlist/" + platform;
            } else {
                filePath = ZMusicBukkit.plugin.getDataFolder()
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

    private static void showPlayListList(String platform, Player player, boolean isGlobal) {
        ArrayList<String> files;
        String filePath = "";
        if (isGlobal) {
            filePath = ZMusicBukkit.plugin.getDataFolder()
                    + "/playlist/" + platform;
        } else {
            filePath = ZMusicBukkit.plugin.getDataFolder()
                    + "/playlist/" + platform + "/" + player.getName();
        }
        switch (platform) {
            case "163":
            case "netease":
            case "global/netease":
                platform = "netease";
                if (isGlobal) {
                    filePath = ZMusicBukkit.plugin.getDataFolder()
                            + "/playlist/" + platform;
                } else {
                    filePath = ZMusicBukkit.plugin.getDataFolder()
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
        if (files != null) {
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
                TextComponent play = new TextComponent("§r[§e播放§r]§r");
                TextComponent show = new TextComponent("§r[§e查看§r]§r");
                if (isGlobal) {
                    play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform.replace("global/", "") + " play " + id));
                    show.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform.replace("global/", "") + " show " + id));
                } else {
                    play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " play " + id));
                    show.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " show " + id));
                }
                play.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放此歌单").create()));
                show.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击查看此歌单").create()));
                message.addExtra(" ");
                message.addExtra(play);
                message.addExtra(" ");
                message.addExtra(show);
                if (player.hasPermission("zmusic.admin")) {
                    TextComponent playAll = new TextComponent("§r[§e全服播放§r]§r");
                    if (isGlobal) {
                        playAll.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform.replace("global/", "") + " playall " + id));
                        playAll.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击为全体玩家播放此歌单").create()));
                        message.addExtra(" ");
                        message.addExtra(playAll);
                    }
                }
                player.spigot().sendMessage(message);
            }
        } else {
            MessageUtils.sendErrorMessage("错误: 在当前平台未导入过任何歌单", player);
        }
        MessageUtils.sendNormalMessage("§6=========================================", player);
    }

    private static void showPlayList(String id, String platform, Player player, boolean isGlobal, int start) {
        JsonObject json;
        Gson gson = new GsonBuilder().create();
        String filePath = "";
        File file;
        if (isGlobal) {
            filePath = ZMusicBukkit.plugin.getDataFolder()
                    + "/playlist/" + platform + "/" + id + ".json";
        } else {
            filePath = ZMusicBukkit.plugin.getDataFolder()
                    + "/playlist/" + platform + "/" + player.getName() + "/" + id + ".json";
        }
        switch (platform) {
            case "163":
            case "netease":
            case "global/netease":
                platform = "netease";
                if (isGlobal) {
                    filePath = ZMusicBukkit.plugin.getDataFolder()
                            + "/playlist/" + platform + "/" + id + ".json";
                } else {
                    filePath = ZMusicBukkit.plugin.getDataFolder()
                            + "/playlist/" + platform + "/" + player.getName() + "/" + id + ".json";
                }
                file = new File(filePath);
                if (!file.exists()) {
                    MessageUtils.sendErrorMessage("错误: 指定歌单不存在", player);
                    return;
                }
                json = gson.fromJson(OtherUtils.readFileToString(file), JsonObject.class);
                break;
            case "global/qq":
            case "qq":
                platform = "qq";
                file = new File(filePath);
                if (!file.exists()) {
                    MessageUtils.sendErrorMessage("错误: 指定歌单不存在", player);
                    return;
                }
                json = gson.fromJson(OtherUtils.readFileToString(file), JsonObject.class);
                break;
            default:
                MessageUtils.sendErrorMessage("错误：未知的平台", player);
                return;
        }
        JsonArray list = json.get("list").getAsJsonArray();
        TextComponent messageStart = new TextComponent(Config.prefix + "§6================");
        TextComponent messageEnd = new TextComponent("§6=================");
        TextComponent prev = new TextComponent("§r[§e上一页§r]§r");
        if ((start - 10) >= 0) {
            if (isGlobal) {
                prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform + " show " + id + " " + (start - 10)));
            } else {
                prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " show " + id + " " + (start - 10)));
            }
            prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击返回上一页").create()));
        } else {
            prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b已经到达第一页").create()));
        }
        TextComponent next = new TextComponent("§r[§e下一页§r]§r");
        if ((start + 10) < list.size()) {
            if (isGlobal) {
                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist global " + platform + " show " + id + " " + (start + 10)));
            } else {
                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist " + platform + " show " + id + " " + (start + 10)));
            }
            next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击前往下一页").create()));
        } else {
            next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b已经到达最后一页").create()));
        }
        TextComponent hr = new TextComponent();
        hr.addExtra(messageStart);
        hr.addExtra(prev);
        hr.addExtra(messageEnd);
        player.spigot().sendMessage(hr);
        for (int i = start; i < list.size(); i++) {
            if (i == start + 10) {
                break;
            }
            JsonObject info = list.get(i).getAsJsonObject();
            String name = info.get("name").getAsString();
            String singer = info.get("singer").getAsString();
            String mid = info.get("id").getAsString();
            TextComponent message = new TextComponent(Config.prefix + "§a" + (i + 1) + "." + name + " - " + singer);
            TextComponent play = new TextComponent("§r[§e播放§r]§r");
            TextComponent music = new TextComponent("§r[§e点歌§r]§r");
            TextComponent jump = new TextComponent("§r[§e跳转§r]§r");
            play.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm play " + platform + " -id:" + mid));
            play.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放").create()));
            music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm music " + platform + " -id:" + mid));
            music.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击点歌").create()));
            jump.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm playlist jump " + (i + 1) + " " + id));
            jump.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击跳转").create()));
            message.addExtra(" ");
            message.addExtra(play);
            message.addExtra(" ");
            message.addExtra(music);
            message.addExtra(" ");
            message.addExtra(jump);
            player.spigot().sendMessage(message);
        }
        hr = new TextComponent();
        hr.addExtra(messageStart);
        hr.addExtra(next);
        hr.addExtra(messageEnd);
        player.spigot().sendMessage(hr);
    }

    private static void playPlayList(String id, String platform, Player p, List<Player> players, boolean isGlobal) {
        if (p != null) {
            players.add(p);
        }
        for (Player player : players) {
            String filePath = "";
            if (isGlobal) {
                filePath = ZMusicBukkit.plugin.getDataFolder() + "/playlist/" + platform;
                platform = platform.replace("global/", "");
            } else {
                filePath = ZMusicBukkit.plugin.getDataFolder() + "/playlist/" + platform + "/" + player.getName();
            }
            File file = new File(filePath, id + ".json");
            Gson gson = new GsonBuilder().create();
            if (!file.exists()) {
                MessageUtils.sendErrorMessage("错误: 指定歌单不存在", player);
                return;
            }
            JsonObject json = gson.fromJson(OtherUtils.readFileToString(file), JsonObject.class);
            String name = json.get("name").getAsString();
            JsonArray list = json.get("list").getAsJsonArray();
            List<JsonObject> playList = new ArrayList<>();
            for (JsonElement j : list) {
                playList.add(j.getAsJsonObject());
            }
            PlayListPlayer plp = PlayerStatus.getPlayerPlayListPlayer(player);
            if (plp != null) {
                plp.cancel();
                PlayerStatus.setPlayerPlayListPlayer(player, null);
                OtherUtils.resetPlayerStatus(player);
                MusicUtils.stop(player);
            }
            PlayListPlayer playListPlayer = new PlayListPlayer();
            String type = PlayerStatus.getPlayerPlayListType(player);
            if (type == null || type.isEmpty()) {
                type = "normal";
            }
            playListPlayer.id = id;
            playListPlayer.playListName = name;
            playListPlayer.type = type;
            playListPlayer.playList = playList;
            playListPlayer.platform = platform;
            playListPlayer.player = player;
            playListPlayer.init();
            playListPlayer.runTaskTimerAsynchronously(ZMusicBukkit.plugin, 0L, 20L);
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
                filePath = ZMusicBukkit.plugin.getDataFolder() + "/playlist/" + platform + "/";
            } else {
                filePath = ZMusicBukkit.plugin.getDataFolder() + "/playlist/" + platform + "/" + player.getName();
            }
            path = new File(filePath);
            if (path.exists()) {
                platform = "netease";
                ArrayList<String> files = OtherUtils.queryFileNames(filePath);
                if (files != null) {
                    for (String s : files) {
                        MessageUtils.sendNormalMessage("§6=========================================", player);
                        PlayList.importPlayList("playlist?id=" + s.split(".json")[0], platform, player, false);
                    }
                    MessageUtils.sendNormalMessage("§6=========================================", player);
                } else {
                    MessageUtils.sendErrorMessage("错误: 在当前平台未导入过任何歌单", player);
                }
            }
        } else if (platform.equalsIgnoreCase("qq") || platform.equalsIgnoreCase("global/qq")) {
            if (isGlobal) {
                filePath = ZMusicBukkit.plugin.getDataFolder() + "/playlist/" + platform + "/";
            } else {
                filePath = ZMusicBukkit.plugin.getDataFolder() + "/playlist/" + platform + "/" + player.getName();
            }
            path = new File(filePath);
            if (path.exists()) {
                ArrayList<String> files = OtherUtils.queryFileNames(filePath);
                if (files != null) {
                    for (String s : files) {
                        MessageUtils.sendNormalMessage("§6=========================================", player);
                        PlayList.importPlayList("playlist/" + s.split(".json")[0], platform, player, false);
                    }
                    MessageUtils.sendNormalMessage("§6=========================================", player);
                } else {
                    MessageUtils.sendErrorMessage("错误: 在当前平台未导入过任何歌单", player);
                }
            }
        }
    }
}
