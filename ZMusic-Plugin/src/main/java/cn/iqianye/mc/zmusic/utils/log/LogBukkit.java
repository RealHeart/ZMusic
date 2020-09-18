package cn.iqianye.mc.zmusic.utils.log;

import cn.iqianye.mc.zmusic.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class LogBukkit implements Log {

    private final CommandSender sender;

    public LogBukkit(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendNormalMessage(String message) {
        sender.sendMessage(Config.prefix + ChatColor.GREEN + message);
    }

    @Override
    public void sendDebugMessage(String message) {
        if (Config.debug) {
            sender.sendMessage(Config.prefix + ChatColor.YELLOW + "[Debug] " + message);
        }
    }

    @Override
    public void sendErrorMessage(String message) {
        sender.sendMessage(Config.prefix + ChatColor.RED + message);
    }

    @Override
    public Object getSender() {
        return sender;
    }
}
