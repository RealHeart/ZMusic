package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import cn.iqianye.MinecraftPlugins.ZMusic.Config.Config;
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
        player.sendMessage(Config.prefix + ChatColor.GREEN + message);
    }

    /**
     * 发送错误消息
     *
     * @param message 消息
     * @param player  玩家
     */
    public static void sendErrorMessage(String message, CommandSender player) {
        player.sendMessage(Config.prefix + ChatColor.RED + message);
    }

    /**
     * 发送Null
     *
     * @param cmdName 主命令名称
     * @param player  玩家
     */
    public static void sendNull(String cmdName, CommandSender player) {
        player.sendMessage(Config.prefix + ChatColor.GREEN + "输入 /" + cmdName + " help 查看帮助.");
    }

    public static void sendPlayError(CommandSender sender, String musicName) {
        MessageUtils.sendErrorMessage("搜索§r[§e" + musicName + "§r]§c失败，可能为以下问题.", sender);
        MessageUtils.sendErrorMessage("1.搜索的音乐不存在或已下架", sender);
        MessageUtils.sendErrorMessage("2.搜索的音乐为付费音乐", sender);
        MessageUtils.sendErrorMessage("3.搜索的音乐为试听音乐", sender);
        MessageUtils.sendErrorMessage("4.服务器网络异常", sender);
    }
}
