package cn.iqianye.mc.zmusic.music.SearchSource;

import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import com.google.gson.*;

import java.net.URLEncoder;

public class NeteaseCloudMusic {

    public NeteaseCloudMusic() {

    }

    /**
     * 获取音乐链接
     *
     * @param musicName 音乐名称
     * @return 音乐链接
     */
    public static JsonObject getMusicUrl(String musicName) {
        try {
            String getUrl = Val.neteaseApiRoot + "search?keywords=" + URLEncoder.encode(musicName, "UTF-8") + "&limit=1&type=1&cookie=" + Val.neteaseCookie;
            Gson gson = new Gson();
            String jsonText = NetUtils.getNetString(getUrl, null);
            JsonObject json = gson.fromJson(jsonText, JsonObject.class);
            JsonObject result = json.getAsJsonObject("result");
            if (result != null || result.get("songCount").getAsInt() != 0) {
                JsonObject jsonOut = result.getAsJsonArray("songs").get(0).getAsJsonObject();
                int musicID = jsonOut.get("id").getAsInt();
                JsonObject getUrlJson = gson.fromJson(NetUtils.getNetString(Val.neteaseApiRoot + "song/url?id=" + musicID + "&br=320000&" +
                        "cookie=" + Val.neteaseCookie, null), JsonObject.class);
                String musicUrl = null;
                try {
                    musicUrl = getUrlJson.get("data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String lyricJsonText = NetUtils.getNetString(Val.neteaseApiRoot + "lyric?id=" + musicID, null);
                JsonObject lyricJson = gson.fromJson(lyricJsonText, JsonObject.class);
                String name = jsonOut.get("name").getAsString();
                int inttime = jsonOut.get("duration").getAsInt();
                inttime = inttime / 1000;
                String time = String.valueOf(inttime);
                JsonArray singer = jsonOut.get("artists").getAsJsonArray();
                String singerName = "";
                for (JsonElement j : singer) {
                    singerName += j.getAsJsonObject().get("name").getAsString() + "/";
                }
                singerName = singerName.substring(0, singerName.length() - 1);
                String lyric = "";
                //String lyricTr = "";
                try {
                    lyric = lyricJson.get("lrc").getAsJsonObject().get("lyric").getAsString();
                    //lyricTr = lyricJson.get("tlyric").getAsJsonObject().get("lyric").getAsString();
                    lyric = lyric.replaceAll("\r", "");
                    //lyricTr = lyricTr.replaceAll("\r", "");
                } catch (Exception e) {
                }
                JsonObject returnJson = new JsonObject();
                returnJson.addProperty("id", musicID);
                returnJson.addProperty("url", musicUrl);
                returnJson.addProperty("time", time);
                returnJson.addProperty("name", name);
                returnJson.addProperty("singer", singerName);
                returnJson.addProperty("lyric", lyric);
                returnJson.addProperty("lyricTr", "");
                return returnJson;
            } else {
                return null;
            }

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
            String getUrl = Val.neteaseApiRoot + "search?keywords=" + URLEncoder.encode(musicName, "UTF-8") + "&limit=10&type=1&cookie=" + Val.neteaseCookie;
            Gson gson = new GsonBuilder().create();
            String jsonText = NetUtils.getNetString(getUrl, null);
            JsonObject json = gson.fromJson(jsonText, JsonObject.class);
            JsonObject result = json.getAsJsonObject("result");
            JsonArray returnJson = new JsonArray();
            if (result != null || result.get("songCount").getAsInt() != 0) {
                JsonArray jsonOut = result.getAsJsonArray("songs");
                for (JsonElement j : jsonOut) {
                    String name = j.getAsJsonObject().get("name").getAsString();
                    int musicID = j.getAsJsonObject().get("id").getAsInt();
                    JsonArray singer = j.getAsJsonObject().get("artists").getAsJsonArray();
                    String singerName = "";
                    for (JsonElement js : singer) {
                        singerName += js.getAsJsonObject().get("name").getAsString() + "/";
                    }
                    singerName = singerName.substring(0, singerName.length() - 1);
                    JsonObject returnJsonObj = new JsonObject();
                    returnJsonObj.addProperty("id", musicID);
                    returnJsonObj.addProperty("name", name);
                    returnJsonObj.addProperty("singer", singerName);
                    returnJson.add(returnJsonObj);
                }
                return returnJson;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



