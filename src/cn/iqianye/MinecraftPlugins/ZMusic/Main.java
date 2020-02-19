package cn.iqianye.MinecraftPlugins.ZMusic;

import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.KuGouMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.NeteaseCloudMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.QQMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Other.Var;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.*;
import com.alibaba.fastjson.JSONObject;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Logger logger = getLogger();
        LogUtils.sendNormalMessage("正在加载中....", logger);
        LogUtils.sendNormalMessage("插件作者: 真心", logger);
        LogUtils.sendNormalMessage("主页：www.zhenxin.xyz", logger);
        LogUtils.sendNormalMessage("插件已加载完成!", logger);
    }

    @Override
    public void onDisable() {
        Logger logger = getLogger();
        LogUtils.sendNormalMessage("正在卸载中....", logger);
        LogUtils.sendNormalMessage("插件作者: 真心", logger);
        LogUtils.sendNormalMessage("主页：www.zhenxin.xyz", logger);
        LogUtils.sendNormalMessage("插件已卸载完成!", logger);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { //指令输出
        if (cmd.getName().equalsIgnoreCase("music") ||
                cmd.getName().equalsIgnoreCase("zmusic") ||
                cmd.getName().equalsIgnoreCase("zm")) {
            if (args.length == 0) {
                MessageUtils.sendNull(cmd.getName(), sender);
                return true;
            } else if (args.length >= 1) {
                switch (args[0]) {
                    case "music":
                        if (sender instanceof Player) {
                            if (args.length >= 2) {
                                OtherUtils.playMusic(sender, cmd, args, getServer(), "music");
                                return true;
                            } else {
                                HelpUtils.sendHelp(cmd.getName(), "music", sender);
                                return true;
                            }
                        }
                    case "play":
                        if (sender instanceof Player) {
                            if (args.length >= 2) {
                                OtherUtils.playMusic(sender, cmd, args, getServer(), "self");
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
                    case "admin":
                        if (sender.hasPermission("ZMusic.admin") || sender.isOp()) {
                            if (args.length == 2) {
                                if (args[1].equalsIgnoreCase("stopAll")) {
                                    List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                    MusicUtils.stopAll(players);
                                    MessageUtils.sendNormalMessage("强制全部玩家停止播放音乐成功!", sender);
                                    return true;
                                }
                            } else {
                                HelpUtils.sendHelp(cmd.getName(), "admin", sender);
                                return true;
                            }
                        }
                    case "help":
                        if (args.length == 2) {
                            HelpUtils.sendHelp(cmd.getName(), args[1], sender);
                            return true;
                        } else {
                            HelpUtils.sendHelp(cmd.getName(), "main", sender);
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

        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("music") ||
                cmd.getName().equalsIgnoreCase("zmusic") ||
                cmd.getName().equalsIgnoreCase("zm")) {
            String[] commandList = new String[0];
            if (args.length == 0) {
                //如果不是能够补全的长度，则返回空列表
                return new ArrayList<>();
            } else if (args.length >= 1) {
                if (args.length == 1) {
                    if (sender.hasPermission("ZMusic.admin") || sender.isOp()) {
                        commandList = new String[]{"help", "play", "admin"};
                    } else {
                        commandList = new String[]{"help", "play"};
                    }
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
                } else if (args[0].equalsIgnoreCase("play")) {
                    if (args.length == 2) {
                        commandList = new String[]{"qq", "netease", "kugou"};
                        return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                    } else {
                        return new ArrayList<>();
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (args.length == 2) {
                        if (sender.hasPermission("ZMusic.admin") || sender.isOp()) {
                            commandList = new String[]{"play", "admin"};
                        } else {
                            commandList = new String[]{"play"};
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
        return new ArrayList<>();
    }

}
