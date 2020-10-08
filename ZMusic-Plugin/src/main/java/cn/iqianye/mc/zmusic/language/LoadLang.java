package cn.iqianye.mc.zmusic.language;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class LoadLang {
    JsonObject json;
    String resRoot = "https://cdn.jsdelivr.net/gh/RealHeart/ZMusic@master/language/";

    public LoadLang() {
        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage() + "_" + locale.getCountry();
        ZMusic.log.sendNormalMessage("初始化语言系统...");
        JsonObject versions = new Gson().fromJson(NetUtils.getNetString(resRoot + "versions.json", null), JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : versions.entrySet()) {
            if (entry.getKey().equals(lang)) {
                File file = new File(ZMusic.dataFolder.getPath() + "/language/", lang + ".json");
                int ver = entry.getValue().getAsInt();
                if (!file.exists()) {
                    ZMusic.log.sendNormalMessage("正在下载语言文件[" + entry.getKey() + "]");
                    downLang(entry.getKey(), file.getPath());
                } else {
                    JsonObject json;
                    try {
                        json = new Gson().fromJson(OtherUtils.readFileToString(file).replaceAll("&", "§"), JsonObject.class);
                    } catch (Exception e) {
                        ZMusic.log.sendErrorMessage("错误: 语言文件读取出错,正在重置...");
                        file.delete();
                        ZMusic.log.sendNormalMessage("正在下载语言文件[" + entry.getKey() + "]");
                        downLang(entry.getKey(), file.getPath());
                        json = new Gson().fromJson(OtherUtils.readFileToString(file).replaceAll("&", "§"), JsonObject.class);
                        return;
                    }
                    if (json.get("info").getAsJsonObject().get("version").getAsInt() != ver) {
                        ZMusic.log.sendNormalMessage("正在更新语言文件[" + entry.getKey() + "]");
                        file.delete();
                        downLang(entry.getKey(), file.getPath());
                        json = new Gson().fromJson(OtherUtils.readFileToString(file).replaceAll("&", "§"), JsonObject.class);
                    }
                    this.json = json;
                }
            }
        }
        ZMusic.log.sendDebugMessage(lang);
    }

    public void load() {
        Gson gson = new GsonBuilder().create();
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
        ZMusic.log.sendNormalMessage("语言文件" + info.get("local").getAsString() + "[" + info.get("name").getAsString() + "]加载完成");
        ZMusic.log.sendNormalMessage("语言文件作者: " + info.get("author").getAsString());
    }

    private void downLang(String lang, String path) {
        try {
            OtherUtils.writeToLocal(path, NetUtils.getNetInputStream(resRoot + lang + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}