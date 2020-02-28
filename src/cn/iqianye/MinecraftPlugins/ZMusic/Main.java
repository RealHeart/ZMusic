package cn.iqianye.MinecraftPlugins.ZMusic;

import cn.iqianye.MinecraftPlugins.ZMusic.Config.Config;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.PlayMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Other.Var;
import cn.iqianye.MinecraftPlugins.ZMusic.PApi.PApiExp;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.*;
import com.locydragon.abf.api.AudioBufferAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        LogUtils.sendNormalMessage("正在加载中....");
        getServer().getPluginManager().registerEvents(this, this);
        OtherUtils.checkUpdate(Var.thisVer, null);
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            saveDefaultConfig();
            LogUtils.sendErrorMessage("无法找到配置文件,正在创建!");
        }
        reloadConfig();
        LogUtils.sendNormalMessage("成功加载配置文件!");
        if (Bukkit.getPluginManager().isPluginEnabled("ActionBarAPI")) {
            LogUtils.sendNormalMessage("已检测到§eActionBarAPI§a, §eActionBar§a功能生效.");
        } else {
            LogUtils.sendErrorMessage("未找到§eActionBarAPI§c, §ePlaceholderAPI§c相关功能不生效.");
            Config.realSupportActionBar = false;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("BossBarAPI")) {
            LogUtils.sendNormalMessage("已检测到§eBossBarAPI§a, §eBossBar§a功能生效.");
        } else {
            LogUtils.sendErrorMessage("未找到§eBossBarAPI§c, §eBossBarAPI§c相关功能不生效.");
            Config.realSupportBossBar = false;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            LogUtils.sendNormalMessage("已检测到§ePlaceholderAPI§a, 正在注册...");
            boolean success = new PApiExp().register();
            if (success) {
                LogUtils.sendNormalMessage("-- §r[§ePlaceholderAPI§r] §a注册成功!");
            } else {
                LogUtils.sendErrorMessage("-- §r[§ePlaceholderAPI§r] §c注册失败!");
            }
        } else {
            LogUtils.sendErrorMessage("未找到§ePlaceholderAPI§c, §ePlaceholderAPI§c相关功能不生效.");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            LogUtils.sendNormalMessage("已检测到Vault, 经济功能生效.");
        } else {
            LogUtils.sendErrorMessage("未找到Vault, 经济相关功能不生效.");
        }
        Config.load(getConfig());
        LogUtils.sendNormalMessage("插件作者: 真心");
        LogUtils.sendNormalMessage("博客：www.zhenxin.xyz");
        LogUtils.sendNormalMessage("QQ：1307993674");
        LogUtils.sendNormalMessage("插件交流群：1032722724");
        LogUtils.sendNormalMessage("插件已加载完成!");
    }

    @Override
    public void onDisable() {
        LogUtils.sendNormalMessage("正在卸载中....");
        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        if (!players.isEmpty()) {
            OtherUtils.resetPlayerStatusAll(players);
            MusicUtils.stopAll(players);
        }
        LogUtils.sendNormalMessage("插件作者: 真心");
        LogUtils.sendNormalMessage("博客：www.zhenxin.xyz");
        LogUtils.sendNormalMessage("QQ：1307993674");
        LogUtils.sendNormalMessage("插件交流群：1032722724");
        LogUtils.sendNormalMessage("插件已卸载完成!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { //指令输出
        if (cmd.getName().equalsIgnoreCase("zm")) {
            if (sender.hasPermission("zmusic.use") || sender.isOp()) {
                if (args.length == 0) {
                    MessageUtils.sendNull(cmd.getName(), sender);
                    return true;
                } else if (args.length >= 1) {
                    switch (args[0]) {
                        case "music":
                            if (sender instanceof Player) {
                                if (args.length >= 2) {
                                    new Thread(() -> {
                                        PlayMusic.play(OtherUtils.argsXin1(args), args[1], (Player) sender, "music", null);
                                    }).start();
                                    return true;
                                } else {
                                    HelpUtils.sendHelp(cmd.getName(), "music", sender);
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
                        case "stop":
                            MusicUtils.stopSelf((Player) sender);
                            OtherUtils.resetPlayerStatus((Player) sender);
                            MessageUtils.sendNormalMessage("停止播放音乐成功!", sender);
                            return true;
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
                        case "volume":
                            if (args.length == 2) {
                                if (sender instanceof Player) {
                                    try {
                                        float volume = Float.parseFloat(args[1]);
                                        System.out.println(args[1]);
                                        System.out.println(volume);
                                        if (volume == 0.0 ||
                                                volume == 0.1 ||
                                                volume == 0.2 ||
                                                volume == 0.3 ||
                                                volume == 0.4 ||
                                                volume == 0.5 ||
                                                volume == 0.6 ||
                                                volume == 0.7 ||
                                                volume == 0.8 ||
                                                volume == 0.9 ||
                                                volume == 1.0) {
                                            AudioBufferAPI.setVolume((Player) sender, volume);

                                        } else {
                                            throw new Exception("秒啊= =");
                                        }
                                    } catch (Exception e) {
                                        LogUtils.sendErrorMessage(e.getMessage());
                                        MessageUtils.sendErrorMessage("请使用0.0-1.0之间的数", sender);
                                    }
                                } else {
                                    MessageUtils.sendErrorMessage("命令只能由玩家使用.", sender);
                                }
                            } else {
                                MessageUtils.sendNull(cmd.getName(), sender);
                            }
                            return true;
                        case "playAll":
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
                        case "stopAll":
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
                                reloadConfig();
                                Config.load(getConfig());
                                MessageUtils.sendNormalMessage("配置文件重载完毕!", sender);
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
                    commandList = new String[]{"help", "play", "music", "admin"};
                } else {
                    commandList = new String[]{"help", "play", "music"};
                }
                return Arrays.stream(commandList).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
            } else if (args[0].equalsIgnoreCase("play")
                    ||
                    args[0].equalsIgnoreCase("music")) {
                if (args.length == 2) {
                    commandList = new String[]{"qq", "163", "kugou"};
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 2) {
                    if (sender.hasPermission("ZMusic.admin") || sender.isOp()) {
                        commandList = new String[]{"play", "music", "admin"};
                    } else {
                        commandList = new String[]{"play", "music"};
                    }
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
            } else {
                return new ArrayList<>();
            }
        }
        //筛选所有可能的补全列表，并返回
        return Arrays.stream(commandList).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("zmusic.admin") || player.isOp()) {
            if (!Var.isLatest) {
                MessageUtils.sendNormalMessage("发现新版本 V" + Var.latestVer, player);
                MessageUtils.sendNormalMessage("更新日志:", player);
                String[] updateLog = Var.updateLog.split("\\n");
                for (String s : updateLog) {
                    MessageUtils.sendNormalMessage(s, player);
                }
                MessageUtils.sendNormalMessage("下载地址: " + ChatColor.YELLOW + ChatColor.UNDERLINE + Var.downloadUrl, player);
            }
        }
    }

}
