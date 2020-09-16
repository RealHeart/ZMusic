package cn.iqianye.mc.zmusic.utils.log;

import cn.iqianye.mc.zmusic.config.Conf;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class LogBC implements Log {

    private final CommandSender sender;

    public LogBC(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendNormalMessage(String message) {
        sender.sendMessage(Conf.prefix + ChatColor.GREEN + message);
    }

    @Override
    public void sendDebugMessage(String message) {
        if (Conf.debug) {
            sender.sendMessage(Conf.prefix + ChatColor.YELLOW + "[Debug] " + message);
        }
    }

    @Override
    public void sendErrorMessage(String message) {
        sender.sendMessage(Conf.prefix + ChatColor.RED + message);
    }
}
