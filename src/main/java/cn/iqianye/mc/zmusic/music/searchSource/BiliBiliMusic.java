package cn.iqianye.mc.zmusic.music.searchSource;

import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import com.google.gson.*;
import com.locydragon.abf.api.AudioBufferAPI;

import java.net.URLEncoder;

public class BiliBiliMusic {

    public static JsonObject getMusic(String keyword) {
        try {
            Gson gson = new GsonBuilder().create();
            keyword = URLEncoder.encode(keyword, "UTF-8");
            String searchUrl = "https://api.bilibili.com/audio/music-service-c/s?keyword=" + keyword + "&pagesize=1";
            String searchJsonText = NetUtils.getNetStringBiliBili(searchUrl, null);
            JsonObject searchJson = gson.fromJson(searchJsonText, JsonObject.class);
            JsonObject searchResult = searchJson.get("data").getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject();
            String musicId = searchResult.get("id").getAsString();
            String musicName = searchResult.get("title").getAsString();
            String musicSinger = searchResult.get("author").getAsString();
            String getUrl = "https://www.bilibili.com/audio/music-service-c/web/url?sid=" + musicId;
            String urlJsonText = NetUtils.getNetStringBiliBiliGZip(getUrl, null);
            urlJsonText = urlJsonText.trim();
            JsonObject urlJson = gson.fromJson(urlJsonText, JsonObject.class);
            String musicUrl = urlJson.get("data").getAsJsonObject().get("cdns").getAsJsonArray().get(0).getAsString();
            musicUrl = NetUtils.getNetString("https://api.zhenxin.xyz/minecraft/plugins/ZMusic/bilibili/getMp3.php?qq=" + Config.bilibiliQQ + "&key=" + Config.bilibiliKey + "&id=" + musicId + "&url=" + URLEncoder.encode(musicUrl, "UTF-8"), null);
            int musicTime = AudioBufferAPI.getAudioLengthByParamQuickly("[Net]" + musicUrl);
            JsonObject returnJSON = new JsonObject();
            returnJSON.addProperty("url", musicUrl);
            returnJSON.addProperty("time", musicTime);
            returnJSON.addProperty("name", musicName);
            returnJSON.addProperty("singer", musicSinger);
            returnJSON.addProperty("lyric", "");
            returnJSON.addProperty("lyricTr", "");
            return returnJSON;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonArray getMusicList(String keyword) {
        try {
            Gson gson = new GsonBuilder().create();
            keyword = URLEncoder.encode(keyword, "UTF-8");
            String searchUrl = "https://api.bilibili.com/audio/music-service-c/s?keyword=" + keyword + "&pagesize=10";
            String searchJsonText = NetUtils.getNetStringBiliBili(searchUrl, null);
            JsonObject searchJson = gson.fromJson(searchJsonText, JsonObject.class);
            JsonArray searchResultList = searchJson.get("data").getAsJsonObject().get("result").getAsJsonArray();
            JsonArray returnJSON = new JsonArray();
            for (JsonElement json : searchResultList) {
                String musicName = json.getAsJsonObject().get("title").getAsString();
                String musicSinger = json.getAsJsonObject().get("author").getAsString();
                JsonObject returnJSONObj = new JsonObject();
                returnJSONObj.addProperty("name", musicName);
                returnJSONObj.addProperty("singer", musicSinger);
                returnJSON.add(returnJSONObj);
            }
            return returnJSON;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}