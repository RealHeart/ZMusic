package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import cn.iqianye.MinecraftPlugins.ZMusic.Other.Var;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpUtils {

    /**
     * 发送帮助
     *
     * @param cmdName 主命令名称
     * @param type    帮助类型
     * @param player  玩家
     */
    public static void sendHelp(String cmdName, String type, CommandSender player) {
        switch (type) {
            case "main":
                player.sendMessage(Var.prefix + ChatColor.AQUA + "点歌系统-主帮助 by: 真心.");
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " help 查看主帮助(当前).");
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " play - 查看播放帮助.");
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " music - 查看点歌帮助.");
                if (player.hasPermission("ZMusic.admin") || player.isOp()) {
                    player.sendMessage(Var.prefix + ChatColor.GREEN + "==============================");
                    player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " admin - 查看管理员帮助.");
                }
                break;
            case "admin":
                player.sendMessage(Var.prefix + ChatColor.AQUA + "点歌系统-管理员帮助 by: 真心.");
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " admin playAll 强制为所有玩家播放音乐.");
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " admin stopAll - 强制为所有玩家停止播放音乐.");
                break;
            case "play":
                player.sendMessage(Var.prefix + ChatColor.AQUA + "点歌系统-播放帮助 by: 真心.");
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " play qq <歌名> - QQ音乐播放§a.");
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " play netease <歌名>- 网易云音乐播放§a.");
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " play kugou <歌名>- 酷狗音乐播放§a.");
                break;
            case "music":
                player.sendMessage(Var.prefix + ChatColor.AQUA + "点歌系统-点歌帮助 by: 真心.");
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " music qq <歌名> - QQ音乐点歌§a.");
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " music netease <歌名>- 网易云音乐点歌§a.");
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " music kugou <歌名>- 酷狗音乐点歌§a.");
                break;
            case "url":
                player.sendMessage(Var.prefix + ChatColor.GREEN + "/" + cmdName + " url <音乐直链> - 播放链接的音乐§a.");
                break;
            default:
                MessageUtils.sendNull(cmdName, player);
        }
    }
}