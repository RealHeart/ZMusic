package me.zhenxin.zmusic.command;

import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.api.ProgressBar;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.config.LoadConfig;
import me.zhenxin.zmusic.data.PlayerData;
import me.zhenxin.zmusic.language.LoadLang;
import me.zhenxin.zmusic.login.NeteaseLogin;
import me.zhenxin.zmusic.music.PlayList;
import me.zhenxin.zmusic.music.PlayListPlayer;
import me.zhenxin.zmusic.music.PlayMusic;
import me.zhenxin.zmusic.music.SearchMusic;
import me.zhenxin.zmusic.utils.CookieUtils;
import me.zhenxin.zmusic.utils.HelpUtils;
import me.zhenxin.zmusic.utils.OtherUtils;
import me.zhenxin.zmusic.utils.Vault;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

public class Cmd {

    static Map<Object, Integer> cooldown = new HashMap<>();
    static List<Object> cooldownStats = new ArrayList<>();

    public static boolean cmd(Object sender, String[] args) { //指令输出
        if (ZMusic.isEnableEd) {
            if (ZMusic.isEnable) {
                boolean isUse;
                boolean isAdmin;
                boolean isPlayAll;
                if (ZMusic.player.isPlayer(sender)) {
                    isUse = ZMusic.player.hasPermission(sender, "zmusic.use");
                    isAdmin = ZMusic.player.hasPermission(sender, "zmusic.admin");
                    isPlayAll = ZMusic.player.hasPermission(sender, "zmusic.playall");
                } else {
                    isUse = true;
                    isAdmin = true;
                    isPlayAll = true;
                }
                if (isUse) {
                    if (args.length == 0) {
                        ZMusic.message.sendNull(sender);
                    } else {
                        switch (args[0].toLowerCase()) {
                            case "music":
                                if (ZMusic.player.isPlayer(sender)) {
                                    int cooldownSec = Config.cooldown;
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
                                            if (!ZMusic.isBC) {
                                                if (Config.realSupportVault) {
                                                    if (Config.money > 0) {
                                                        if (!Vault.take(sender)) {
                                                            return true;
                                                        }
                                                    }
                                                }
                                            }
                                            ZMusic.runTask.runAsync(startPlay);
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
                                            } else {
                                                break;
                                            }
                                        } else {
                                            ZMusic.message.sendErrorMessage("冷却时间未到,还有§e " + cooldown.get(sender) + "§c 秒", sender);
                                        }
                                    } else {
                                        ZMusic.runTask.runAsync(startPlay);
                                    }
                                } else {
                                    ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
                                }
                                break;
                            case "play":
                                if (ZMusic.player.isPlayer(sender)) {
                                    if (args.length >= 2) {
                                        ZMusic.runTask.runAsync(() -> {
                                            PlayMusic.play(OtherUtils.argsXin1(args), args[1], sender, "self", null);
                                        });
                                    } else {
                                        HelpUtils.sendHelp("play", sender);
                                    }
                                } else {
                                    ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
                                }
                                break;
                            case "search":
                                if (ZMusic.player.isPlayer(sender)) {
                                    if (args.length >= 2) {
                                        ZMusic.runTask.runAsync(() -> {
                                            SearchMusic.sendList(OtherUtils.argsXin1(args), args[1], sender);
                                        });
                                    } else {
                                        HelpUtils.sendHelp("search", sender);
                                    }
                                    break;
                                } else {
                                    ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
                                }
                                break;
                            case "stop":
                                if (ZMusic.player.isPlayer(sender)) {
                                    PlayListPlayer plp = PlayerData.getPlayerPlayListPlayer(sender);
                                    if (plp != null) {
                                        plp.isStop = true;
                                        PlayerData.setPlayerPlayListPlayer(sender, null);
                                        OtherUtils.resetPlayerStatus(sender);
                                    }
                                    OtherUtils.resetPlayerStatus(sender);
                                    ZMusic.message.sendNormalMessage("停止播放音乐成功!", sender);
                                    break;
                                } else {
                                    ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
                                }
                                break;
                            case "loop":
                                if (ZMusic.player.isPlayer(sender)) {
                                    if (PlayerData.getPlayerLoopPlay(sender) != null && PlayerData.getPlayerLoopPlay(sender)) {
                                        PlayerData.setPlayerLoopPlay(sender, false);
                                        ZMusic.message.sendNormalMessage("循环播放已关闭!", sender);
                                    } else {
                                        PlayerData.setPlayerLoopPlay(sender, true);
                                        ZMusic.message.sendNormalMessage("循环播放已开启!", sender);
                                    }
                                } else {
                                    ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
                                }
                                break;
                            case "playlist":
                                if (ZMusic.player.isPlayer(sender)) {
                                    if (args.length >= 2) {
                                        ZMusic.runTask.runAsync(() -> {
                                            PlayList.subCommand(args, sender);
                                        });
                                    } else {
                                        HelpUtils.sendHelp("playlist", sender);
                                    }
                                } else {
                                    ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
                                }
                                break;
                            case "url":
                                if (ZMusic.player.isPlayer(sender)) {
                                    if (args.length == 2) {
                                        ZMusic.runTask.runAsync(() -> {
                                            ZMusic.music.stop(sender);
                                            try {
                                                Thread.sleep(10);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            ZMusic.music.play(args[1], sender);
                                            ZMusic.message.sendNormalMessage("播放成功!", sender);
                                        });
                                    } else {
                                        HelpUtils.sendHelp("url", sender);
                                    }
                                } else {
                                    ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
                                }
                                break;
                            case "playall":
                                if (isPlayAll || isAdmin) {
                                    List<Object> players = ZMusic.player.getOnlinePlayerList();
                                    if (args.length >= 2) {
                                        ZMusic.runTask.runAsync(() -> {
                                            PlayMusic.play(OtherUtils.argsXin1(args), args[1], sender, "all", players);
                                        });
                                    } else {
                                        HelpUtils.sendHelp("admin", sender);
                                    }
                                } else {
                                    ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                }
                                break;
                            case "stopall":
                                if (isAdmin) {
                                    List<Object> players = ZMusic.player.getOnlinePlayerList();
                                    for (Object player : players) {
                                        OtherUtils.resetPlayerStatus(player);
                                    }
                                    ZMusic.message.sendNormalMessage("强制全部玩家停止播放音乐成功!", sender);
                                } else {
                                    ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                }
                                break;
                            case "login":
                                if (isAdmin) {
                                    ZMusic.runTask.runAsync(() -> {
                                        try {
                                            String key = NeteaseLogin.key();
                                            String qrcode = NeteaseLogin.create(key);
                                            ZMusic.message.sendNormalMessage("请打开如下网址扫描二维码登录:", sender);
                                            ZMusic.message.sendNormalMessage(qrcode, sender);
                                            ZMusic.runTask.runAsync(() -> {
                                                boolean cancel = false;
                                                while (!cancel) {
                                                    try {
                                                        Thread.sleep(3000);
                                                        int code = NeteaseLogin.check(key);
                                                        switch (code) {
                                                            case 800:
                                                                ZMusic.message.sendErrorMessage("二维码已过期!", sender);
                                                                cancel = true;
                                                                break;
                                                            case 802:
                                                                ZMusic.message.sendNormalMessage("请在手机上确认登录!", sender);
                                                                break;
                                                            case 803:
                                                                ZMusic.message.sendNormalMessage(NeteaseLogin.welcome(), sender);
                                                                CookieUtils.saveCookies();
                                                                cancel = true;
                                                                break;
                                                            default:
                                                        }
                                                    } catch (InterruptedException e) {
                                                        ZMusic.message.sendErrorMessage("登录失败! 请检查后台错误.", sender);
                                                        e.printStackTrace();
                                                        cancel = true;
                                                    }
                                                }
                                            });
                                        } catch (UnsupportedEncodingException e) {
                                            ZMusic.message.sendErrorMessage("登录失败! 请检查后台错误.", sender);
                                            e.printStackTrace();
                                        }
                                    });
                                } else {
                                    ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                }
                                break;
                            case "help":
                                if (args.length == 2) {
                                    HelpUtils.sendHelp(args[1], sender);
                                } else {
                                    HelpUtils.sendHelp("main", sender);
                                }
                                break;
                            case "reload":
                                if (isAdmin) {
                                    new LoadConfig().reload(sender);
                                    ZMusic.runTask.runAsync(() -> new LoadLang().load());
                                    ZMusic.message.sendNormalMessage(NeteaseLogin.refresh(), sender);
                                } else {
                                    ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                }
                                break;
                            case "update":
                                if (isAdmin) {
                                    OtherUtils.checkUpdate(sender, true);
                                } else {
                                    ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                }
                                break;
                            case "163hot":
                                if (args.length == 2) {
                                    OtherUtils.neteaseHotComments(sender, OtherUtils.argsXin1(args, ""));
                                } else {
                                    HelpUtils.sendHelp("main", sender);
                                }
                                break;
                            case "test":
                                if (ZMusic.player.isPlayer(sender)) {
                                    ZMusic.runTask.runAsync(() -> {
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
                                } else {
                                    ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
                                }
                                break;
                            default:
                                ZMusic.message.sendNull(sender);
                                break;
                        }
                    }
                } else {
                    ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.use 权限此使用命令.", sender);
                }
            } else {
                ZMusic.message.sendErrorMessage("错误: 请删除AudioBuffer/AllMusic插件.", sender);
            }
        } else {
            ZMusic.message.sendErrorMessage("错误: 请等待插件加载完毕.", sender);
        }
        return true;
    }

    public static List<String> tab(Object sender, String[] args) {
        boolean isAdmin;
        if (ZMusic.player.isPlayer(sender))
            isAdmin = ZMusic.player.hasPermission(sender, "zmusic.admin");
        else isAdmin = true;
        String[] commandList = new String[0];
        if (args.length == 0) {
            //如果不是能够补全的长度，则返回空列表
            return new ArrayList<>();
        } else if (args.length >= 1) {
            if (args.length == 1) {
                if (isAdmin) {
                    commandList = new String[]{"help",
                            "play",
                            "playlist",
                            "music",
                            "stop",
                            "loop",
                            "login",
                            "search",
                            "url",
                            "playAll",
                            "stopAll",
                            "163hot",
                            "update",
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
                            "kuwo",
                            "bilibili"};
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 2) {
                    if (isAdmin) {
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
                    if (args[2].equalsIgnoreCase("qq") ||
                            args[2].equalsIgnoreCase("163") ||
                            args[2].equalsIgnoreCase("netease")) {
                        commandList = new String[]{"import", "play", "list", "update", "show"};
                        return Arrays.stream(commandList).filter(s -> s.startsWith(args[3])).collect(Collectors.toList());
                    } else {
                        return new ArrayList<>();
                    }
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
