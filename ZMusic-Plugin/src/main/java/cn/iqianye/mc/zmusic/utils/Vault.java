package cn.iqianye.mc.zmusic.utils;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.config.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Vault {

    public static boolean take(Object sender) {
        Economy econ;
        RegisteredServiceProvider<Economy> economyProvider = org.bukkit.Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        econ = economyProvider.getProvider();
        double money = econ.getBalance(((Player) sender).getPlayer());
        if ((money - Config.money) >= 0) {
            econ.withdrawPlayer(((Player) sender).getPlayer(), Config.money);
            money = econ.getBalance(((Player) sender).getPlayer());
            ZMusic.message.sendNormalMessage("点歌花费§e" + econ.format(Config.money) + "§a,已扣除,扣除后余额: §e" + econ.format(money) + "§a.", sender);
            return true;
        } else {
            ZMusic.message.sendErrorMessage("金币不足,需要§e" + econ.format(Config.money) + "§c,你有§e" + econ.format(money) + "§c.", sender);
            return false;
        }
    }
}
