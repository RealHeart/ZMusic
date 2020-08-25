package cn.iqianye.mc.zmusic.music.searchSource;

import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import com.google.gson.*;
import com.locydragon.abf.api.AudioBufferAPI;

import java.net.URLEncoder;

public class MiguMusic {

    public static JsonObject getMusic(String keyword) {
        try {
            Gson gson = new GsonBuilder().create();
            keyword = URLEncoder.encode(keyword, "UTF-8");
            String searchUrl = Val.miguMusicApiRoot + "search?keyword=" + keyword;
            String searchJsonText = NetUtils.getNetString(searchUrl, null);
            JsonObject searchJson = gson.fromJson(searchJsonText, JsonObject.class);
            JsonObject searchResult = searchJson.get("data").getAsJsonObject().get("list").getAsJsonArray().get(0).getAsJsonObject();
            String musicId = searchResult.get("id").getAsString();
            String musicCid = searchResult.get("cid").getAsString();
            String musicName = searchResult.get("name").getAsString();
            JsonArray musicSingerJson = searchResult.get("artists").getAsJsonArray();
            String musicSinger = "";
            for (JsonElement j : musicSingerJson) {
                musicSinger += j.getAsJsonObject().get("name").getAsString() + "/";
            }
            musicSinger = musicSinger.substring(0, musicSinger.length() - 1);
            String getUrl = Val.miguMusicApiRoot + "song/url?id=" + musicId;
            String urlJsonText = NetUtils.getNetString(getUrl, null);
            JsonObject urlJson = gson.fromJson(urlJsonText, JsonObject.class);
            String musicUrl = urlJson.get("data").getAsString();
            String getLyric = Val.miguMusicApiRoot + "lyric?cid=" + musicCid;
            String lyricJsonText = NetUtils.getNetString(getLyric, null);
            JsonObject lyricJson = gson.fromJson(lyricJsonText, JsonObject.class);
            String musicLyric = lyricJson.get("data").getAsString();
            musicLyric = musicLyric.replaceAll("\r", "");
            int musicTime = AudioBufferAPI.getAudioLengthByParamQuickly("[Net]" + musicUrl);
            JsonObject returnJSON = new JsonObject();
            returnJSON.addProperty("url", musicUrl);
            returnJSON.addProperty("time", musicTime);
            returnJSON.addProperty("name", musicName);
            returnJSON.addProperty("singer", musicSinger);
            returnJSON.addProperty("lyric", musicLyric);
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
            String searchUrl = Val.miguMusicApiRoot + "search?keyword=" + keyword;
            String searchJsonText = NetUtils.getNetStringBiliBili(searchUrl, null);
            JsonObject searchJson = gson.fromJson(searchJsonText, JsonObject.class);
            JsonArray searchResultList = searchJson.get("data").getAsJsonObject().get("list").getAsJsonArray();
            JsonArray returnJSON = new JsonArray();
            for (JsonElement json : searchResultList) {
                String musicName = json.getAsJsonObject().get("name").getAsString();
                JsonArray musicSingerJson = json.getAsJsonObject().get("artists").getAsJsonArray();
                String musicSinger = "";
                for (JsonElement j : musicSingerJson) {
                    musicSinger += j.getAsJsonObject().get("name").getAsString() + "/";
                }
                musicSinger = musicSinger.substring(0, musicSinger.length() - 1);
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
