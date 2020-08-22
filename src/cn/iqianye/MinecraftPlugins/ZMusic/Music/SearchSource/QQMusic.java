package cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource;

import cn.iqianye.MinecraftPlugins.ZMusic.Other.Val;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;
import com.google.gson.*;
import com.locydragon.abf.api.AudioBufferAPI;

import java.net.URLEncoder;

public class QQMusic {

    /**
     * 获取音乐链接
     *
     * @param musicName 音乐名称
     * @return 音乐链接
     */
    public static JsonObject getMusicUrl(String musicName) {
        try {
            String getUrl = Val.qqMusicApiRoot + "search?pageSize=1&key=" + URLEncoder.encode(musicName, "utf-8");
            Gson gson = new GsonBuilder().create();
            JsonObject json = gson.fromJson(NetUtils.getNetString(getUrl, null), JsonObject.class);
            JsonObject data = json.get("data").getAsJsonObject();
            JsonObject list = data.getAsJsonArray("list").get(0).getAsJsonObject();
            String songmid = list.get("songmid").getAsString();
            String songName = list.get("songname").getAsString();
            JsonArray singer = list.getAsJsonArray("singer");
            String singerName = "";
            for (JsonElement j : singer) {
                singerName += j.getAsJsonObject().get("name").getAsString() + "/";
            }
            singerName = singerName.substring(0, singerName.length() - 1);
            String getLyricUrl = Val.qqMusicApiRoot + "lyric?songmid=" + songmid;
            String lyricJsonText = NetUtils.getNetString(getLyricUrl, null);
            JsonObject lyricJson = gson.fromJson(lyricJsonText, JsonObject.class);
            String lyric = lyricJson.get("data").getAsJsonObject().get("lyric").getAsString();
            String lyricTr = lyricJson.get("data").getAsJsonObject().get("trans").getAsString();
            String getMp3Url = Val.qqMusicApiRoot + "song/url?id=" + songmid;
            String getMp3JsonText = NetUtils.getNetString(getMp3Url, null);
            JsonObject getMp3Json = gson.fromJson(getMp3JsonText, JsonObject.class);
            String mp3Url = getMp3Json.get("data").getAsString();
            int songTime = AudioBufferAPI.getAudioLengthByParamQuickly("[Net]" + mp3Url);

            JsonObject returnJSON = new JsonObject();
            returnJSON.addProperty("url", mp3Url);
            returnJSON.addProperty("time", songTime);
            returnJSON.addProperty("name", songName);
            returnJSON.addProperty("singer", singerName);
            returnJSON.addProperty("lyric", lyric);
            returnJSON.addProperty("lyricTr", lyricTr);
            return returnJSON;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取音乐列表
     *
     * @param musicName 音乐名称
     * @return 音乐列表数组
     */
    public static JsonArray getMusicList(String musicName) {
        try {
            String getUrl = Val.qqMusicApiRoot + "search?pageSize=10&key=" + URLEncoder.encode(musicName, "utf-8");
            Gson gson = new GsonBuilder().create();
            JsonObject json = gson.fromJson(NetUtils.getNetString(getUrl, null), JsonObject.class);
            JsonObject data = json.get("data").getAsJsonObject();
            JsonArray list = data.getAsJsonArray("list");
            JsonArray returnJson = new JsonArray();
            for (JsonElement j : list) {
                String songName = j.getAsJsonObject().get("songname").getAsString();
                JsonArray singer = j.getAsJsonObject().getAsJsonArray("singer");
                String singerName = "";
                for (JsonElement js : singer) {
                    singerName += js.getAsJsonObject().get("name").getAsString() + "/";
                }
                singerName = singerName.substring(0, singerName.length() - 1);
                JsonObject returnJsonObj = new JsonObject();
                returnJsonObj.addProperty("name", songName);
                returnJsonObj.addProperty("singer", singerName);
                returnJson.add(returnJsonObj);
            }
            return returnJson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
