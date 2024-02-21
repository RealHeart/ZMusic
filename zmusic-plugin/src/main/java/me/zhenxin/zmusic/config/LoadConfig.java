package me.zhenxin.zmusic.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.utils.NetUtils;
import me.zhenxin.zmusic.utils.OtherUtils;

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
        Config.checkUpdate = config.get("check-update").getAsBoolean();
        // Prefix
        Config.prefix = config.get("prefix").getAsString().replaceAll("&", "§");
        // Api
        JsonObject api = config.get("api").getAsJsonObject();
        String neteaseApiRoot = api.get("netease").getAsString();
        if (!neteaseApiRoot.endsWith("/")) {
            neteaseApiRoot += "/";
        }
        Config.neteaseApiRoot = neteaseApiRoot;
        // NeteaseFollow
        Config.neteaseFollow = config.get("netease-follow").getAsBoolean();
        // VIP
        JsonObject vip = config.get("vip").getAsJsonObject();
        Config.vipAccount = vip.get("account").getAsString();
        Config.vipSecret = vip.get("secret").getAsString();
        if (!Config.vipSecret.equalsIgnoreCase("none")) {
            ZMusic.runTask.runAsync(() -> {
                Gson gson = new GsonBuilder().create();
                JsonObject data = new JsonObject();
                data.addProperty("account", Config.vipAccount);
                data.addProperty("secret", Config.vipSecret);
                String jsonText = NetUtils.postNetString("https://api.zhenxin.me/zmusic/vip/check", null, data);
                JsonObject json = gson.fromJson(jsonText, JsonObject.class);
                int code = json.get("code").getAsInt();
                if (code == 200) {
                    JsonObject dataJson = json.get("data").getAsJsonObject();
                    ZMusic.isVip = dataJson.get("isVip").getAsBoolean();
                }
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
