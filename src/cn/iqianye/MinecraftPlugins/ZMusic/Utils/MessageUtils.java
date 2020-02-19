package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import cn.iqianye.MinecraftPlugins.ZMusic.Other.Var;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageUtils {

    /**
     * 发送普通消息
     *
     * @param message 消息
     * @param player  玩家
     */
    public static void sendNormalMessage(String message, CommandSender player) {
        player.sendMessage(Var.prefix + ChatColor.GREEN + message);
    }

    /**
     * 发送错误消息
     *
     * @param message 消息
     * @param player  玩家
     */
    public static void sendErrorMessage(String message, CommandSender player) {
        player.sendMessage(Var.prefix + ChatColor.RED + message);
    }

    /**
     * 发送Null
     *
     * @param cmdName 主命令名称
     * @param player  玩家
     */
    public static void sendNull(String cmdName, CommandSender player) {
        player.sendMessage(Var.prefix + ChatColor.GREEN + "输入 /" + cmdName + " help 查看帮助.");
    }
}
