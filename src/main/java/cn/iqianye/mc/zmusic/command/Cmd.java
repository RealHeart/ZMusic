package cn.iqianye.mc.zmusic.command;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.api.ProgressBar;
import cn.iqianye.mc.zmusic.config.Conf;
import cn.iqianye.mc.zmusic.music.PlayList;
import cn.iqianye.mc.zmusic.music.PlayListPlayer;
import cn.iqianye.mc.zmusic.music.PlayMusic;
import cn.iqianye.mc.zmusic.music.SearchMusic;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.HelpUtils;
import cn.iqianye.mc.zmusic.utils.other.OtherUtils;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;
import java.util.stream.Collectors;

public class Cmd {

    static Map<Object, Integer> cooldown = new HashMap<>();
    static List<Object> cooldownStats = new ArrayList<>();

    public static boolean cmd(Object sender, String[] args) { //指令输出
        if (Val.isEnable) {
            if (ZMusic.player.hasPermission(sender, "zmusic.use")) {
                if (args.length == 0) {
                    ZMusic.message.sendNull(sender);
                    return true;
                } else if (args.length >= 1) {
                    switch (args[0].toLowerCase()) {
                        case "music":
                            int cooldownSec = Conf.cooldown;
                            Runnable startPlay = () -> {
                                if (args.length >= 2) {
                                    List<Object> players = ZMusic.player.getOnlinePlayerList();
                                    PlayMusic.play(OtherUtils.argsXin1(args), args[1], sender, "music", players);
                                } else {
                                    HelpUtils.sendHelp("music", sender);
                                }
                            };
                            if (!ZMusic.player.hasPermission(sender, "zmusic.bypass")) {
                                if (!cooldownStats.contains(sender)) {
                                    ZMusic.runTask.start(startPlay);
                                    if (cooldownSec > 0) {
                                        cooldownStats.add(sender);
                                        cooldown.put(sender, cooldownSec);
                                        Timer timer = new Timer();
                                        TimerTask timerTask = new TimerTask() {
                                            @Override
                                            public void run() {
                                                int sec = cooldown.get(sender);
                                                if (sec != 1) {
                                                    sec--;
                                                    cooldown.put(sender, sec);
                                                } else {
                                                    cooldownStats.remove(sender);
                                                    cancel();
                                                }
                                            }
                                        };
                                        timer.schedule(timerTask, 1000L, 1000L);
                                        return true;
                                    } else {
                                        return true;
                                    }
                                } else {
                                    ZMusic.message.sendErrorMessage("冷却时间未到,还有§e " + cooldown.get(sender) + "§c 秒", sender);
                                    return true;
                                }
                            } else {
                                ZMusic.runTask.start(startPlay);
                                return true;
                            }
                        case "play":
                            if (args.length >= 2) {
                                ZMusic.runTask.start(() -> {
                                    PlayMusic.play(OtherUtils.argsXin1(args), args[1], sender, "self", null);
                                });
                                return true;
                            } else {
                                HelpUtils.sendHelp("play", sender);
                                return true;
                            }
                        case "search":
                            if (args.length >= 2) {
                                ZMusic.runTask.start(() -> {
                                    SearchMusic.sendList(OtherUtils.argsXin1(args), args[1], sender);
                                });
                                return true;
                            } else {
                                HelpUtils.sendHelp("search", sender);
                                return true;
                            }
                        case "stop":
                            PlayListPlayer plp = PlayerStatus.getPlayerPlayListPlayer(sender);
                            if (plp != null) {
                                plp.isStop = true;
                                PlayerStatus.setPlayerPlayListPlayer(sender, null);
                                OtherUtils.resetPlayerStatus(sender);
                            }
                            OtherUtils.resetPlayerStatus(sender);
                            ZMusic.message.sendNormalMessage("停止播放音乐成功!", sender);
                            return true;
                        case "loop":
                            if (PlayerStatus.getPlayerLoopPlay(sender) != null && PlayerStatus.getPlayerLoopPlay(sender)) {
                                PlayerStatus.setPlayerLoopPlay(sender, false);
                                ZMusic.message.sendNormalMessage("循环播放已关闭!", sender);
                            } else {
                                PlayerStatus.setPlayerLoopPlay(sender, true);
                                ZMusic.message.sendNormalMessage("循环播放已开启!", sender);
                            }
                            return true;
                        case "playlist":
                            if (args.length >= 2) {
                                ZMusic.runTask.start(() -> {
                                    PlayList.subCommand(args, sender);
                                });
                                return true;
                            }
                            HelpUtils.sendHelp("playlist", sender);
                            return true;
                        case "url":
                            if (args.length == 2) {
                                ZMusic.runTask.start(() -> {
                                    ZMusic.music.stop(sender);
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    ZMusic.music.play(args[1], sender);
                                    ZMusic.message.sendNormalMessage("播放成功!", sender);
                                });
                                return true;
                            } else {
                                HelpUtils.sendHelp("url", sender);
                                return true;
                            }
                        case "playall":
                            if (ZMusic.player.hasPermission(sender, "zmusic.admin")) {
                                List<Object> players = ZMusic.player.getOnlinePlayerList();
                                if (args.length >= 2) {
                                    ZMusic.runTask.start(() -> {
                                        PlayMusic.play(OtherUtils.argsXin1(args), args[1], sender, "all", players);
                                    });
                                    return true;
                                } else {
                                    HelpUtils.sendHelp("admin", sender);
                                    return true;
                                }
                            } else {
                                ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                return true;
                            }
                        case "stopall":
                            if (ZMusic.player.hasPermission(sender, "zmusic.admin")) {
                                List<Object> players = new ArrayList<>(org.bukkit.Bukkit.getServer().getOnlinePlayers());
                                for (Object player : players) {
                                    OtherUtils.resetPlayerStatus(player);
                                }
                                ZMusic.message.sendNormalMessage("强制全部玩家停止播放音乐成功!", sender);
                                return true;
                            } else {
                                ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                return true;
                            }
                        case "help":
                            if (args.length == 2) {
                                HelpUtils.sendHelp(args[1], sender);
                                return true;
                            } else {
                                HelpUtils.sendHelp("main", sender);
                                return true;
                            }
                        case "reload":
                            if (ZMusic.player.hasPermission(sender, "zmusic.admin")) {
                                ZMusic.server.reload(sender);
                                return true;
                            } else {
                                ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                return true;
                            }
                        case "163hot":
                            if (args.length == 2) {
                                OtherUtils.neteaseHotComments(sender, OtherUtils.argsXin1(args, ""));
                                return true;
                            } else {
                                HelpUtils.sendHelp("main", sender);
                                return true;
                            }
                        case "test":
                            ZMusic.runTask.start(() -> {
                                ProgressBar progressBar = new ProgressBar('■', '□', 100 - 1);
                                for (int i = 0; i < 100; i++) {
                                    progressBar.setProgress(i);
                                    try {
                                        ZMusic.message.sendActionBarMessage(new TextComponent(progressBar.getString()), sender);
                                        Thread.sleep(10);
                                    } catch (Exception ignored) {
                                    }
                                }
                            });

                            return true;
                        default:
                            ZMusic.message.sendNull(sender);
                            return true;
                    }
                } else {
                    ZMusic.message.sendNull(sender);
                    return true;
                }
            } else {
                ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.use 权限此使用命令.", sender);
                return true;
            }
        } else {
            ZMusic.message.sendErrorMessage("错误: 请删除AudioBuffer/AllMusic插件.", sender);
            return true;
        }

    }

    public static List<String> tab(Object sender, String[] args) {
        String[] commandList = new String[0];
        if (args.length == 0) {
            //如果不是能够补全的长度，则返回空列表
            return new ArrayList<>();
        } else if (args.length >= 1) {
            if (args.length == 1) {
                if (ZMusic.player.hasPermission(sender, "zmusic.admin")) {
                    commandList = new String[]{"help",
                            "play",
                            "playlist",
                            "music",
                            "stop",
                            "loop",
                            "search",
                            "url",
                            "admin",
                            "playAll",
                            "stopAll",
                            "163hot",
                            "reload"};
                } else {
                    commandList = new String[]{"help",
                            "play",
                            "playlist",
                            "music",
                            "stop",
                            "loop",
                            "search",
                            "163hot",
                            "url"};
                }
                return Arrays.stream(commandList).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
            } else if (args[0].equalsIgnoreCase("play")
                    ||
                    args[0].equalsIgnoreCase("music")
                    ||
                    args[0].equalsIgnoreCase("search")
                    ||
                    args[0].equalsIgnoreCase("playAll")) {
                if (args.length == 2) {
                    commandList = new String[]{
                            "qq",
                            "163",
                            "netease",
                            "kugou",
                            "kuwo",
                            "bilibili"};
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 2) {
                    if (ZMusic.player.hasPermission(sender, "zmusic.admin")) {
                        commandList = new String[]{"play", "playlist", "music", "search", "url", "admin"};
                    } else {
                        commandList = new String[]{"play", "playlist", "music", "search", "url"};
                    }
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
            } else if (args[0].equalsIgnoreCase("playlist")) {
                if (args.length == 2) {
                    commandList = new String[]{"qq", "netease", "163", "type", "global", "next", "prev", "jump"};
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                } else if (args.length == 3) {
                    if (args[1].equalsIgnoreCase("type")) {
                        commandList = new String[]{"random", "normal", "loop"};
                        return Arrays.stream(commandList).filter(s -> s.startsWith(args[2])).collect(Collectors.toList());
                    } else if (args[1].equalsIgnoreCase("global")) {
                        commandList = new String[]{"qq", "netease", "163"};
                        return Arrays.stream(commandList).filter(s -> s.startsWith(args[2])).collect(Collectors.toList());
                    } else {
                        commandList = new String[]{"import", "play", "list", "update", "show"};
                        return Arrays.stream(commandList).filter(s -> s.startsWith(args[2])).collect(Collectors.toList());
                    }
                } else if (args.length == 4) {
                    commandList = new String[]{"import", "play", "list", "update", "show"};
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[3])).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
            } else {
                return new ArrayList<>();
            }
        }
        return Arrays.stream(commandList).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
    }
}
