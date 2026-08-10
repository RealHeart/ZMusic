package me.zhenxin.zmusic.utils.message;

import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.component.ZComponent;
import me.zhenxin.zmusic.component.adapter.BungeeComponentAdapter;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.language.Lang;
import me.zhenxin.zmusic.utils.runtask.BukkitTaskScheduler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageBukkit implements Message {

    private final BungeeComponentAdapter adapter = BungeeComponentAdapter.INSTANCE;

    @Override
    public void sendNormalMessage(String message, Object playerObj) {
        CommandSender sender = (CommandSender) playerObj;
        sender.sendMessage(Config.prefix + ChatColor.GREEN + message);
    }

    @Override
    public void sendErrorMessage(String message, Object playerObj) {
        CommandSender sender = (CommandSender) playerObj;
        sender.sendMessage(Config.prefix + ChatColor.RED + message);
    }

    @Override
    public void sendJsonMessage(ZComponent message, Object playerObj) {
        Player player = (Player) playerObj;
        TextComponent bungeeComponent = adapter.adapt(message);
        BukkitTaskScheduler.run(player, () -> player.spigot().sendMessage(bungeeComponent));
    }

    @Override
    public void sendActionBarMessage(ZComponent message, Object playerObj) {
        Player player = (Player) playerObj;
        TextComponent bungeeComponent = adapter.adapt(message);
        BukkitTaskScheduler.run(player,
                () -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, bungeeComponent));
    }

    @Override
    public void sendTitleMessage(String title, String subTitle, Object playerObj) {
        Player player = (Player) playerObj;
        try {
            player.sendTitle(title, subTitle, 0, 200, 20);
            ZMusic.log.sendDebugMessage(title + " " + subTitle);
        } catch (NoSuchMethodError e) {
            player.sendTitle(title, subTitle);
        }
    }

    @Override
    public void sendNull(Object playerObj) {
        CommandSender sender = (CommandSender) playerObj;
        sender.sendMessage(Config.prefix + ChatColor.GREEN + Lang.helpHelp);
    }
}
