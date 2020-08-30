package cn.iqianye.mc.zmusic.utils;

import cn.iqianye.mc.zmusic.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

public class LogUtils {

    private static CommandSender sender = Bukkit.getConsoleSender();

    /**
     * 发送普通日志
     *
     * @param message 消息
     */
    public static void sendNormalMessage(String message) {
        sender.sendMessage(Config.prefix + ChatColor.GREEN + message);
    }

    /**
     * 发送调试日志
     *
     * @param message 消息
     */
    public static void sendDebugMessage(String message) {
        sender.sendMessage(Config.prefix + ChatColor.YELLOW + "[Debug] " + message);
    }

    /**
     * 发送错误日志
     *
     * @param message 消息
     */
    public static void sendErrorMessage(String message) {
        sender.sendMessage(Config.prefix + ChatColor.RED + message);
    }
}
