package cn.iqianye.mc.zmusic.utils;

import cn.iqianye.mc.zmusic.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

public class LogUtils {

    private static Logger logger = Bukkit.getLogger();

    /**
     * 发送普通日志
     *
     * @param message 消息
     */
    public static void sendNormalMessage(String message) {
        logger.info(Config.prefix + ChatColor.GREEN + message);
    }

    /**
     * 发送错误日志
     *
     * @param message 消息
     */
    public static void sendErrorMessage(String message) {
        logger.warning(Config.prefix + ChatColor.RED + message);
    }
}
