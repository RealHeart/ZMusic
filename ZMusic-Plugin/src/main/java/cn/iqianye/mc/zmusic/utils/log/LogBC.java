package cn.iqianye.mc.zmusic.utils.log;

import cn.iqianye.mc.zmusic.config.Config;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class LogBC implements Log {

    private final CommandSender sender;

    public LogBC(CommandSender sender) {
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
