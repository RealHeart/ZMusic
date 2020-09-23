package cn.iqianye.mc.zmusic.language;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;

public class LoadLang {
    InputStream is;
    String lang;

    public LoadLang() {
        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage() + "_" + locale.getCountry();
        //lang = "en_US";
        ZMusic.log.sendDebugMessage(lang);
        is = getClass().getResourceAsStream("/language/" + lang + ".json");
        if (is == null) {
            ZMusic.log.sendErrorMessage("错误: 语言文件不存在,使用默认语言文件[zh_CN]");
            lang = "zh_CN";
            is = getClass().getResourceAsStream("/language/" + lang + ".json");
            ZMusic.log.sendDebugMessage(lang);
        }
        this.lang = lang;
    }

    public void load() {
        Gson gson = new GsonBuilder().create();
        File path = new File(ZMusic.dataFolder.getPath(), "language");
        if (!path.exists()) {
            path.mkdir();
        }
        File langFile = new File(ZMusic.dataFolder.getPath() + "/language/", lang + ".json");
        String data;
        if (langFile.exists()) {
            InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("/language/versions.json"), StandardCharsets.UTF_8);
            String latestData = OtherUtils.readFileToString(isr);
            data = OtherUtils.readFileToString(langFile);
            long thisVer = gson.fromJson(data, JsonObject.class).get("info").getAsJsonObject().get("version").getAsLong();
            long latestVer = gson.fromJson(latestData, JsonObject.class).get(lang).getAsLong();
            if (thisVer != latestVer) {
                ZMusic.log.sendNormalMessage("正在更新语言文件...");
                langFile.delete();
                try {
                    Files.copy(is, langFile.toPath());
                    ZMusic.log.sendNormalMessage("更新完毕");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                Files.copy(is, langFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JsonObject json;
        try {
            data = OtherUtils.readFileToString(langFile);
            data = data.replaceAll("&", "§");
            json = gson.fromJson(data, JsonObject.class);
        } catch (Exception e) {
            lang = "zh_CN";
            ZMusic.log.sendErrorMessage("错误: 语言文件加载出错,使用默认语言文件[zh_CN]");
            load();
            return;
        }
        JsonObject info = json.get("info").getAsJsonObject();
        JsonObject lang = json.get("language").getAsJsonObject();
        JsonObject play = lang.get("play").getAsJsonObject();
        // 全部播放提示
        Lang.searching = play.get("searching").getAsString();
        Lang.playing = play.get("playing").getAsString();
        Lang.musicMessage = play.get("musicMessage").getAsString();
        Lang.playSuccess = play.get("playSuccess").getAsString();
        Lang.playAllSource = play.get("playAllSource").getAsString();
        JsonArray playError = play.get("playError").getAsJsonArray();
        for (JsonElement j : playError) {
            Lang.playError.add(j.getAsString());
        }
        // 全部点击事件
        JsonObject click = lang.get("click").getAsJsonObject();
        Lang.clickPlay = click.get("play").getAsString();
        Lang.clickPlayText = click.get("playText").getAsString();
        Lang.clickMusic = click.get("music").getAsString();
        Lang.clickMusicText = click.get("musicText").getAsString();
        Lang.clickPlayAll = click.get("playAll").getAsString();
        Lang.clickPlayAllText = click.get("playAllText").getAsString();
        Lang.clickPrev = click.get("prev").getAsString();
        Lang.clickPrevText = click.get("prevText").getAsString();
        Lang.clickNext = click.get("next").getAsString();
        Lang.clickNextText = click.get("nextText").getAsString();
        Lang.clickJump = click.get("jump").getAsString();
        Lang.clickJumpText = click.get("jumpText").getAsString();
        Lang.clickView = click.get("view").getAsString();
        Lang.clickViewText = click.get("viewText").getAsString();
        Lang.clickUpdatePlaylist = click.get("updatePlaylist").getAsString();
        Lang.clickUpdatePlaylistText = click.get("updatePlaylistText").getAsString();

        // 全部歌单提示
        JsonObject playList = lang.get("playList").getAsJsonObject();
        JsonArray playListPlayError = playList.get("playListPlayError").getAsJsonArray();
        for (JsonElement j : playListPlayError) {
            Lang.playListPlayError.add(j.getAsString());
        }
        // 全部帮助信息
        Lang.helpHelp = lang.get("help").getAsJsonObject().get("help").getAsString();
        JsonArray helpMain = lang.get("help").getAsJsonObject().get("main").getAsJsonArray();
        for (JsonElement j : helpMain) {
            Lang.mainHelp.add(j.getAsString());
        }
        ZMusic.log.sendNormalMessage("语言文件" + this.lang + "[" + info.get("name").getAsString() + "]加载完成");
        ZMusic.log.sendNormalMessage("语言文件作者: " + info.get("author").getAsString());
    }
}
