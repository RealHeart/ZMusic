package cn.iqianye.mc.zmusic.config.load;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.ZMusicBukkit;
import cn.iqianye.mc.zmusic.config.Conf;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import cn.iqianye.mc.zmusic.utils.other.OtherUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class LoadBukkit {
    public static void load(FileConfiguration configuration) {
        // Version
        Conf.version = configuration.getInt("version");
        if (Conf.version != Conf.latestVersion) {
            ZMusic.log.sendNormalMessage("-- 正在更新配置文件...");
            File config = new File(ZMusicBukkit.plugin.getDataFolder() + "config.yml");
            File configBak = new File(ZMusicBukkit.plugin.getDataFolder() + "config.yml.v" + Conf.version + ".bak");
            ZMusic.log.sendNormalMessage("-- 正在备份原配置文件...");
            config.renameTo(configBak);
            ZMusic.log.sendNormalMessage("-- 正在释放新配置文件...");
            ZMusicBukkit.plugin.saveDefaultConfig();
            ZMusic.log.sendNormalMessage("-- 更新完毕.");
            ZMusicBukkit.plugin.reloadConfig();
            load(ZMusicBukkit.plugin.getConfig());
        }
        // Debug
        Conf.debug = configuration.getBoolean("debug");
        Conf.update = configuration.getBoolean("update");
        // Prefix
        Conf.prefix = configuration.getString("prefix").replaceAll("&", "§");
        // Api
        Conf.neteaseApiRoot = configuration.getString("api.netease");
        Conf.qqMusicApiRoot = configuration.getString("api.qq");
        // Account
        // Netease
        Conf.neteaseloginType = configuration.getString("account.netease.loginType");
        Conf.neteaseAccount = configuration.getString("account.netease.account");
        Conf.neteasePasswordType = configuration.getString("account.netease.passwordType");
        if (Conf.neteasePasswordType.equalsIgnoreCase("normal")) {
            Conf.neteasePassword = OtherUtils.getMD5String(configuration.getString("account.netease.password"));
        } else if (Conf.neteasePasswordType.equalsIgnoreCase("md5")) {
            Conf.neteasePassword = configuration.getString("account.netease.password");
        }
        Conf.neteaseFollow = configuration.getBoolean("account.netease.follow");
        // Bilibili
        Conf.bilibiliQQ = configuration.getString("account.bilibili.qq");
        Conf.bilibiliKey = configuration.getString("account.bilibili.key");
        if (!Conf.bilibiliKey.equalsIgnoreCase("none")) {
            ZMusic.runTask.start(() -> {
                Gson gson = new GsonBuilder().create();
                String jsonText = NetUtils.getNetString("https://api.zhenxin.xyz/minecraft/plugins/ZMusic/bilibili/checkVIP.php", null, "qq=" + Conf.bilibiliQQ + "&key=" + Conf.bilibiliKey);
                JsonObject json = gson.fromJson(jsonText, JsonObject.class);
                Val.bilibiliIsVIP = json.get("isVIP").getAsBoolean();
            });
        }
        // Music
        Conf.money = configuration.getInt("music.money");
        Conf.cooldown = configuration.getInt("music.cooldown");
        // Lyric
        Conf.lyricEnable = configuration.getBoolean("lyric.enable");
        Conf.showLyricTr = configuration.getBoolean("lyric.showLyricTr");
        Conf.lyricColor = configuration.getString("lyric.color").replaceAll("&", "§");
        if (Conf.realSupportBossBar) {
            Conf.supportBossBar = configuration.getBoolean("lyric.bossBar");
        }
        if (Conf.realSupportActionBar) {
            Conf.supportActionBar = configuration.getBoolean("lyric.actionBar");
        }
        if (Conf.realSupportTitle) {
            Conf.supportTitle = configuration.getBoolean("lyric.subTitle");
        }
        if (Conf.realSupportHud) {
            Conf.supportHud = configuration.getBoolean("lyric.hud.enable");
            Conf.hudInfoX = configuration.getInt("lyric.hud.infoX");
            Conf.hudInfoY = configuration.getInt("lyric.hud.infoY");
            Conf.hudLyricX = configuration.getInt("lyric.hud.lyricX");
            Conf.hudLyricY = configuration.getInt("lyric.hud.lyricY");
        }
        Conf.supportChat = configuration.getBoolean("lyric.chatMessage");
    }
}