package cn.iqianye.mc.zmusic.utils.server;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.ZMusicBC;
import cn.iqianye.mc.zmusic.config.load.LoadBC;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ServerBC implements Server {
    @Override
    public void reload(Object player) {
        try {
            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(ZMusicBC.plugin.getDataFolder(), "config.yml"));
            LoadBC.load(configuration);
            OtherUtils.loginNetease();
            ZMusic.message.sendNormalMessage("配置文件重载完毕!", player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
