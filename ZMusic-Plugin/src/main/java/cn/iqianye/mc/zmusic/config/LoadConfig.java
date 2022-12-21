package cn.iqianye.mc.zmusic.config;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LoadConfig {
    public void load() {
        File oldConfig = new File(ZMusic.dataFolder.getPath(), "config.yml");
        if (oldConfig.exists()) {
            File reToOld = new File(ZMusic.dataFolder.getPath(), "config.yml.old");
            if (!oldConfig.renameTo(reToOld)) {
                reToOld.delete();
                oldConfig.renameTo(reToOld);
            }
        }
        File config = new File(ZMusic.dataFolder.getPath(), "config.json");
        if (!config.exists()) {
            ZMusic.log.sendErrorMessage("无法找到配置文件,正在创建!");
            saveDefaultConfig();
        }
        String json = OtherUtils.readFileToString(config);
        JsonObject configJson;
        try {
            configJson = new Gson().fromJson(json, JsonObject.class);
        } catch (Exception e) {
            ZMusic.log.sendErrorMessage("配置文件出错,正在重置!");
            File configErrBak = new File(ZMusic.dataFolder.getPath(), System.currentTimeMillis() + "_error-config.json");
            config.renameTo(configErrBak);
            config.delete();
            saveDefaultConfig();
            load();
            return;
        }
        Config.version = configJson.get("version").getAsInt();
        if (Config.version != Config.latestVersion) {
            ZMusic.log.sendNormalMessage("-- 正在更新配置文件...");
            config = new File(ZMusic.dataFolder.getPath(), "config.json");
            File configBak = new File(ZMusic.dataFolder.getPath(), "config.json.v" + Config.version + ".bak");
            ZMusic.log.sendNormalMessage("-- 正在备份原配置文件...");
            config.renameTo(configBak);
            ZMusic.log.sendNormalMessage("-- 正在释放新配置文件...");
            saveDefaultConfig();
            ZMusic.log.sendNormalMessage("-- 更新完毕.");
            load();
            return;
        }
        init(configJson);
    }

    private void init(JsonObject config) {
        // Version
        Config.version = config.get("version").getAsInt();
        // Debug
        Config.debug = config.get("debug").getAsBoolean();
        Config.update = config.get("update").getAsBoolean();
        // Prefix
        Config.prefix = config.get("prefix").getAsString().replaceAll("&", "§");
        // Api
        JsonObject api = config.get("api").getAsJsonObject();
        String neteaseApiRoot = api.get("netease").getAsString();
        if (!neteaseApiRoot.endsWith("/")) {
            neteaseApiRoot += "/";
        }
        Config.neteaseApiRoot = neteaseApiRoot;
        // Account
        JsonObject account = config.get("account").getAsJsonObject();
        // Netease
        JsonObject netease = account.get("netease").getAsJsonObject();
        Config.neteaseloginType = netease.get("loginType").getAsString();
        Config.neteaseAccount = netease.get("account").getAsString();
        Config.neteasePasswordType = netease.get("passwordType").getAsString();
        if (Config.neteasePasswordType.equalsIgnoreCase("normal")) {
            Config.neteasePassword = OtherUtils.getMD5String(netease.get("password").getAsString());
        } else if (Config.neteasePasswordType.equalsIgnoreCase("md5")) {
            Config.neteasePassword = netease.get("password").getAsString();
        }
        Config.neteaseFollow = netease.get("follow").getAsBoolean();
        // Bilibili
        JsonObject bilibili = account.get("bilibili").getAsJsonObject();
        Config.bilibiliQQ = bilibili.get("qq").getAsString();
        Config.bilibiliKey = bilibili.get("key").getAsString();
        if (!Config.bilibiliKey.equalsIgnoreCase("none")) {
            ZMusic.runTask.runAsync(() -> {
                Gson gson = new GsonBuilder().create();
                String jsonText = NetUtils.getNetString("https://api.zhenxin.xyz/minecraft/plugins/ZMusic/bilibili/checkVIP.php", null, "qq=" + Config.bilibiliQQ + "&key=" + Config.bilibiliKey);
                JsonObject json = gson.fromJson(jsonText, JsonObject.class);
                ZMusic.bilibiliIsVIP = json.get("isVIP").getAsBoolean();
            });
        }
        // Music
        JsonObject music = config.get("music").getAsJsonObject();
        Config.money = music.get("money").getAsInt();
        Config.cooldown = music.get("cooldown").getAsInt();
        // Lyric
        JsonObject lyric = config.get("lyric").getAsJsonObject();
        Config.lyricEnable = lyric.get("enable").getAsBoolean();
        Config.showLyricTr = lyric.get("showLyricTr").getAsBoolean();
        Config.lyricColor = lyric.get("color").getAsString().replaceAll("&", "§");
        if (Config.realSupportBossBar) {
            Config.supportBossBar = lyric.get("bossBar").getAsBoolean();
        }
        if (Config.realSupportActionBar) {
            Config.supportActionBar = lyric.get("actionBar").getAsBoolean();
        }
        if (Config.realSupportTitle) {
            Config.supportTitle = lyric.get("subTitle").getAsBoolean();
        }
        Config.supportChat = lyric.get("chatMessage").getAsBoolean();
        if (Config.realSupportHud) {
            JsonObject hud = lyric.get("hud").getAsJsonObject();
            Config.supportHud = hud.get("enable").getAsBoolean();
            Config.hudInfoX = hud.get("infoX").getAsInt();
            Config.hudInfoY = hud.get("infoY").getAsInt();
            Config.hudLyricX = hud.get("lyricX").getAsInt();
            Config.hudLyricY = hud.get("lyricY").getAsInt();
        }

    }

    private void saveDefaultConfig() {
        File config = new File(ZMusic.dataFolder.getPath(), "config.json");
        try {
            Files.copy(this.getClass().getResourceAsStream("/config.json"), config.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload(Object sender) {
        load();
        ZMusic.message.sendNormalMessage("配置文件重载完毕!", sender);
    }
}
