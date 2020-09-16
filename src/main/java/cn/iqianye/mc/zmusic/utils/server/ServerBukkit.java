package cn.iqianye.mc.zmusic.utils.server;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.ZMusicBukkit;
import cn.iqianye.mc.zmusic.config.load.LoadBukkit;
import cn.iqianye.mc.zmusic.papi.PApiHook;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerBukkit implements Server {
    @Override
    public void reload(Object player) {
        JavaPlugin plugin = ZMusicBukkit.plugin;
        plugin.reloadConfig();
        LoadBukkit.load(plugin.getConfig());
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            boolean success = new PApiHook().register();
            if (success) {
                ZMusic.log.sendNormalMessage("§r[§ePlaceholderAPI§r] §a注册成功!");
                ZMusic.message.sendNormalMessage("§r[§ePlaceholderAPI§r] §a注册成功!", player);
            } else {
                ZMusic.log.sendErrorMessage("§r[§ePlaceholderAPI§r] §c注册失败!");
                ZMusic.message.sendErrorMessage("§r[§ePlaceholderAPI§r] §c注册失败!", player);
            }
        }
        OtherUtils.loginNetease();
        ZMusic.message.sendNormalMessage("配置文件重载完毕!", player);
    }
}
