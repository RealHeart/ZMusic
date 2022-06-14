package cn.iqianye.mc.zmusic.language;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LoadLang {
    JsonObject json;

    public LoadLang() {
        json = new JsonObject();
        ZMusic.log.sendNormalMessage("初始化语言系统...");
        File dir = new File(ZMusic.dataFolder + "/language");
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(ZMusic.dataFolder + "/language", "zh_CN.json");
        if (file.exists()) {
            json = new Gson().fromJson(OtherUtils.readFileToString(file).replaceAll("&", "§"), JsonObject.class);
        } else {
            InputStream lang = LoadLang.class.getResourceAsStream("/language/zh_CN.json");
            try {
                OtherUtils.saveStringToLocal(file, OtherUtils.readInputStream(lang));
            } catch (IOException e) {
                e.printStackTrace();
            }
            json = new Gson().fromJson(OtherUtils.readFileToString(file).replaceAll("&", "§"), JsonObject.class);
        }
    }

    public void load() {
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
        Lang.playError.clear();
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
        Lang.playListPlayError.clear();
        for (JsonElement j : playListPlayError) {
            Lang.playListPlayError.add(j.getAsString());
        }
        // 全部帮助信息
        Lang.helpHelp = lang.get("help").getAsJsonObject().get("help").getAsString();
        JsonArray helpMain = lang.get("help").getAsJsonObject().get("main").getAsJsonArray();
        Lang.mainHelp.clear();
        for (JsonElement j : helpMain) {
            Lang.mainHelp.add(j.getAsString());
        }
        ZMusic.log.sendNormalMessage("语言文件" + info.get("local").getAsString() + "[" + info.get("name").getAsString() + "]加载完成");
        ZMusic.log.sendNormalMessage("语言文件作者: " + info.get("author").getAsString());
    }
}
