package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import cn.iqianye.MinecraftPlugins.ZMusic.Other.Var;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

public class LogUtils {
    /**
     * 发送普通日志
     * @param message 消息
     * @param logger 日志
     */
    public static void sendNormalMessage(String message, Logger logger){
        logger.info(Var.prefix + ChatColor.GREEN + message);
    }

    /**
     * 发送错误日志
     * @param message 消息
     * @param logger 日志
     */
    public static void sendErrorMessage(String message, Logger logger){
        logger.warning(Var.prefix + ChatColor.RED + message);
    }
}
