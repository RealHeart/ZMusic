package cn.iqianye.mc.zmusic.utils.message;

import cn.iqianye.mc.zmusic.config.Config;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageBukkit implements Message {

    @Override
    public void sendNormalMessage(String message, Object playerObj) {
        Player player = (Player) playerObj;
        player.sendMessage(Config.prefix + ChatColor.GREEN + message);
    }

    @Override
    public void sendErrorMessage(String message, Object playerObj) {
        Player player = (Player) playerObj;
        player.sendMessage(Config.prefix + ChatColor.RED + message);
    }

    @Override
    public void sendJsonMessage(TextComponent message, Object playerObj) {
        Player player = (Player) playerObj;
        player.spigot().sendMessage(message);
    }

    @Override
    public void sendActionBarMessage(TextComponent message, Object playerObj) {
        Player player = (Player) playerObj;
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
    }

    @Override
    public void sendTitleMessage(String title, String subTitle, Object playerObj) {
        Player player = (Player) playerObj;
        try {
            player.sendTitle(title, subTitle, 0, 200, 20);
        } catch (NoSuchMethodError e) {
            player.sendTitle(title, subTitle);
        }
    }

    @Override
    public void sendNull(Object playerObj) {
        Player player = (Player) playerObj;
        player.sendMessage(Config.prefix + ChatColor.GREEN + "输入 /zm help 查看帮助.");
    }
}
