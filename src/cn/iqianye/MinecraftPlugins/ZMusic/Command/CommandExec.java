package cn.iqianye.MinecraftPlugins.ZMusic.Command;

import cn.iqianye.MinecraftPlugins.ZMusic.Config.Config;
import cn.iqianye.MinecraftPlugins.ZMusic.Main;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.PlayList;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.PlayMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Player.PlayerStatus;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.HelpUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.MessageUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.MusicUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.OtherUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class CommandExec implements TabExecutor {

    Map<Player, Integer> cooldown = new HashMap<>();
    List<Player> cooldownStats = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { //指令输出
        if (cmd.getName().equalsIgnoreCase("zm")) {
            if (sender.hasPermission("zmusic.use") || sender.isOp()) {
                if (args.length == 0) {
                    MessageUtils.sendNull(cmd.getName(), sender);
                    return true;
                } else if (args.length >= 1) {
                    switch (args[0].toLowerCase()) {
                        case "music":
                            if (sender instanceof Player) {
                                int cooldownSec = Config.cooldown;
                                Thread startPlayThread = new Thread(() -> {
                                    if (args.length >= 2) {
                                        new Thread(() -> {
                                            List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                            PlayMusic.play(OtherUtils.argsXin1(args), args[1], (Player) sender, "music", players);
                                        }).start();
                                    } else {
                                        HelpUtils.sendHelp(cmd.getName(), "music", sender);
                                    }
                                });
                                if (!sender.hasPermission("zmusic.bypass") || !sender.isOp()) {
                                    if (!cooldownStats.contains(sender)) {
                                        if (Config.realSupportVault) {
                                            if (Config.money > 0) {
                                                Economy econ;
                                                RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
                                                econ = economyProvider.getProvider();
                                                double money = econ.getBalance(((Player) sender).getPlayer());
                                                if ((money - Config.money) >= 0) {
                                                    econ.withdrawPlayer(((Player) sender).getPlayer(), Config.money);
                                                    money = econ.getBalance(((Player) sender).getPlayer());
                                                    MessageUtils.sendNormalMessage("点歌花费§e" + econ.format(Config.money) + "§a,已扣除,扣除后余额: §e" + econ.format(money) + "§a.", sender);
                                                } else {
                                                    MessageUtils.sendErrorMessage("金币不足,需要§e" + econ.format(Config.money) + "§c,你有§e" + econ.format(money) + "§c.", sender);
                                                    return true;
                                                }
                                            }
                                        }
                                        startPlayThread.start();
                                        if (cooldownSec > 0) {
                                            cooldownStats.add((Player) sender);
                                            cooldown.put((Player) sender, cooldownSec);
                                            Timer timer = new Timer();
                                            TimerTask timerTask = new TimerTask() {
                                                @Override
                                                public void run() {
                                                    int sec = cooldown.get(sender);
                                                    if (sec != 1) {
                                                        sec--;
                                                        cooldown.put((Player) sender, sec);
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
                                        MessageUtils.sendErrorMessage("冷却时间未到,还有§e " + cooldown.get(sender) + "§c 秒", sender);
                                        return true;
                                    }
                                } else {
                                    startPlayThread.start();
                                    return true;
                                }
                            }
                        case "play":
                            if (sender instanceof Player) {
                                if (args.length >= 2) {
                                    new Thread(() -> {
                                        PlayMusic.play(OtherUtils.argsXin1(args), args[1], (Player) sender, "self", null);
                                    }).start();
                                    return true;
                                } else {
                                    HelpUtils.sendHelp(cmd.getName(), "play", sender);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("命令只能由玩家使用!", sender);
                                return true;
                            }
                        case "search":
                            if (sender instanceof Player) {
                                if (args.length >= 2) {
                                    new Thread(() -> {
                                        SearchMusic.sendList(OtherUtils.argsXin1(args), args[1], (Player) sender);
                                    }).start();
                                    return true;
                                } else {
                                    HelpUtils.sendHelp(cmd.getName(), "search", sender);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("命令只能由玩家使用!", sender);
                                return true;
                            }
                        case "stop":
                            MusicUtils.stopSelf((Player) sender);
                            OtherUtils.resetPlayerStatus((Player) sender);
                            MessageUtils.sendNormalMessage("停止播放音乐成功!", sender);
                            return true;
                        case "loop":
                            if (PlayerStatus.getPlayerLoopPlay((Player) sender) != null && PlayerStatus.getPlayerLoopPlay((Player) sender)) {
                                PlayerStatus.setPlayerLoopPlay((Player) sender, false);
                                MessageUtils.sendNormalMessage("循环播放已关闭!", sender);
                            } else {
                                PlayerStatus.setPlayerLoopPlay((Player) sender, true);
                                MessageUtils.sendNormalMessage("循环播放已开启!", sender);
                            }
                            return true;
                        case "playlist":
                            if (sender instanceof Player) {
                                if (args.length >= 2) {
                                    if (args[1].equalsIgnoreCase("list")) {
                                        new Thread(() -> {
                                            PlayList.showPlayList((Player) sender);
                                        }).start();
                                        return true;
                                    } else if (args.length >= 3) {
                                        if (args[1].equalsIgnoreCase("import")) {
                                            new Thread(() -> {
                                                PlayList.importPlayList(args[2], (Player) sender);
                                            }).start();
                                        } else if (args[1].equalsIgnoreCase("play")) {
                                            new Thread(() -> {
                                                PlayList.playPlayList(args[2], (Player) sender);
                                            }).start();
                                        }
                                        return true;
                                    }
                                    HelpUtils.sendHelp(cmd.getName(), "playlist", sender);
                                    return true;
                                } else {
                                    HelpUtils.sendHelp(cmd.getName(), "playlist", sender);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("命令只能由玩家使用!", sender);
                                return true;
                            }
                        case "url":
                            if (sender instanceof Player) {
                                if (args.length == 2) {
                                    MusicUtils.stopSelf((Player) sender);
                                    MusicUtils.playSelf(args[1], (Player) sender);
                                    MessageUtils.sendNormalMessage("播放成功!", sender);
                                    return true;
                                } else {
                                    HelpUtils.sendHelp(cmd.getName(), "url", sender);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("命令只能由玩家使用!", sender);
                                return true;
                            }
                        case "playall":
                            if (sender instanceof Player) {
                                if (sender.hasPermission("zmusic.admin") || sender.isOp()) {
                                    List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                    if (args.length >= 2) {
                                        new Thread(() -> {
                                            PlayMusic.play(OtherUtils.argsXin1(args), args[1], (Player) sender, "all", players);
                                        }).start();
                                        return true;
                                    } else {
                                        HelpUtils.sendHelp(cmd.getName(), "admin", sender);
                                        return true;
                                    }
                                } else {
                                    MessageUtils.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("命令只能由玩家使用!", sender);
                                return true;
                            }
                        case "stopall":
                            if (sender instanceof Player) {
                                if (sender.hasPermission("zmusic.admin") || sender.isOp()) {
                                    List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                    MusicUtils.stopAll(players);
                                    OtherUtils.resetPlayerStatusAll(players);
                                    MessageUtils.sendNormalMessage("强制全部玩家停止播放音乐成功!", sender);
                                    return true;
                                } else {
                                    MessageUtils.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                    return true;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("命令只能由玩家使用!", sender);
                                return true;
                            }
                        case "help":
                            if (args.length == 2) {
                                HelpUtils.sendHelp(cmd.getName(), args[1], sender);
                                return true;
                            } else {
                                HelpUtils.sendHelp(cmd.getName(), "main", sender);
                                return true;
                            }
                        case "reload":
                            if (sender.hasPermission("zmusic.admin") || sender.isOp()) {
                                JavaPlugin.getPlugin(Main.class).reloadConfig();
                                Config.load(JavaPlugin.getPlugin(Main.class).getConfig());
                                MessageUtils.sendNormalMessage("配置文件重载完毕!", sender);
                                return true;
                            } else {
                                MessageUtils.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                return true;
                            }
                        case "163hot":
                            if (args.length == 2) {
                                OtherUtils.neteaseHotComments((Player) sender, OtherUtils.argsXin1(args, ""));
                                return true;
                            } else {
                                HelpUtils.sendHelp(cmd.getName(), "main", sender);
                                return true;
                            }
                        case "163relogin":
                            if (sender.hasPermission("zmusic.admin") || sender.isOp()) {
                                OtherUtils.loginNetease((Player) sender);
                                return true;
                            } else {
                                MessageUtils.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
                                return true;
                            }
                        default:
                            MessageUtils.sendNull(cmd.getName(), sender);
                            return true;
                    }
                } else {
                    MessageUtils.sendNull(cmd.getName(), sender);
                    return true;
                }
            } else {
                MessageUtils.sendErrorMessage("权限不足，你需要 zmusic.use 权限此使用命令.", sender);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        String[] commandList = new String[0];
        if (args.length == 0) {
            //如果不是能够补全的长度，则返回空列表
            return new ArrayList<>();
        } else if (args.length >= 1) {
            if (args.length == 1) {
                if (sender.hasPermission("ZMusic.admin") || sender.isOp()) {
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
                            "163relogin",
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
                    commandList = new String[]{"qq",
                            "163",
                            "netease",
                            "kugou",
                            "kuwo"};
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 2) {
                    if (sender.hasPermission("ZMusic.admin") || sender.isOp()) {
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
                    commandList = new String[]{"play", "import", "list"};
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
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
