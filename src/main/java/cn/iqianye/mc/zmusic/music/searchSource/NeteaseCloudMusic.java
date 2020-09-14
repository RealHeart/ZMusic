package cn.iqianye.mc.zmusic.music.searchSource;

import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import com.google.gson.*;
import org.json.simple.JSONArray;

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
            String getUrl = Config.neteaseApiRoot + "search?keywords=" + URLEncoder.encode(musicName, "UTF-8") + "&limit=1&type=1&cookie=" + Val.neteaseCookie;
            Gson gson = new Gson();
            String jsonText = NetUtils.getNetString(getUrl, null);
            JsonObject json = gson.fromJson(jsonText, JsonObject.class);
            JsonObject result = json.getAsJsonObject("result");
            if (result != null || result.get("songCount").getAsInt() != 0) {
                JsonObject jsonOut = result.getAsJsonArray("songs").get(0).getAsJsonObject();
                int musicID = jsonOut.get("id").getAsInt();
                JsonObject getUrlJson = gson.fromJson(NetUtils.getNetString(Config.neteaseApiRoot + "song/url?id=" + musicID + "&br=320000&" +
                        "cookie=" + Val.neteaseCookie, null), JsonObject.class);
                String musicUrl = null;
                try {
                    musicUrl = getUrlJson.get("data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String lyricJsonText = NetUtils.getNetString(Config.neteaseApiRoot + "lyric?id=" + musicID, null);
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
                String lyricTr = "";
                try {
                    lyric = lyricJson.get("lrc").getAsJsonObject().get("lyric").getAsString();
                    lyric = lyric.replaceAll("\r", "");
                    lyricTr = lyricJson.get("tlyric").getAsJsonObject().get("lyric").getAsString();
                    lyricTr = lyricTr.replaceAll("\r", "");
                } catch (Exception ignored) {
                }
                StringBuilder sb = new StringBuilder();
                if (lyric.isEmpty()) {
                    sb.append("未找到歌词信息\n");
                }
                if (lyricTr.isEmpty()) {
                    sb.append("未找到歌词翻译\n");
                }
                JsonObject returnJson = new JsonObject();
                returnJson.addProperty("id", musicID);
                returnJson.addProperty("url", musicUrl);
                returnJson.addProperty("time", time);
                returnJson.addProperty("name", name);
                returnJson.addProperty("singer", singerName);
                returnJson.addProperty("lyric", lyric);
                returnJson.addProperty("lyricTr", lyricTr);
                returnJson.addProperty("error", sb.toString());
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
            String getUrl = Config.neteaseApiRoot + "search?keywords=" + URLEncoder.encode(musicName, "UTF-8") + "&limit=10&type=1&cookie=" + Val.neteaseCookie;
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

    /**
     * 获取音乐歌单
     *
     * @param playListId 歌单ID
     * @return 音乐歌单信息
     */
    public static JsonObject getMusicSongList(String playListId) {
        try {
            String getUrl = Config.neteaseApiRoot + "playlist/detail?id=" + playListId;
            Gson gson = new GsonBuilder().create();
            JsonObject json = gson.fromJson(NetUtils.getNetString(getUrl, null), JsonObject.class);
            JsonArray trackIds = json.get("playlist").getAsJsonObject().get("trackIds").getAsJsonArray();
            String playListName = json.getAsJsonObject("playlist").get("name").getAsString();
            int playListSongs = json.getAsJsonObject("playlist").get("trackCount").getAsInt();
            if (playListSongs > 1000) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            String s;
            for (JsonElement jsonElement : trackIds) {
                sb.append(jsonElement.getAsJsonObject().get("id").getAsString()).append(",");
            }
            s = sb.toString();
            s = s.substring(0, s.length() - 1);

            JsonObject playListInfo = gson.fromJson(NetUtils.getNetString(Config.neteaseApiRoot + "song/detail", null, "ids=" + s), JsonObject.class);
            JsonArray songList = playListInfo.get("songs").getAsJsonArray();

            JsonObject returnJson = new JsonObject();
            JSONArray returnJsonArr = new JSONArray();

            for (JsonElement jsonElement : songList) {
                String songName = jsonElement.getAsJsonObject().get("name").getAsString();
                int songTime = jsonElement.getAsJsonObject().get("dt").getAsInt();
                songTime = songTime / 1000;
                JsonArray ar = jsonElement.getAsJsonObject().get("ar").getAsJsonArray();
                String singer = "";
                for (JsonElement j : ar) {
                    singer += j.getAsJsonObject().get("name").getAsString() + "/";
                }
                singer = singer.substring(0, singer.length() - 1);
                String songId = jsonElement.getAsJsonObject().get("id").getAsString();
                JsonObject returnJsonObj = new JsonObject();
                returnJsonObj.addProperty("id", songId);
                returnJsonObj.addProperty("name", songName);
                returnJsonObj.addProperty("singer", singer);
                returnJsonObj.addProperty("time", songTime);
                returnJsonArr.add(returnJsonObj);
            }
            returnJson.addProperty("name", playListName);
            returnJson.addProperty("songs", playListSongs);
            returnJson.add("list", gson.fromJson(returnJsonArr.toJSONString(), JsonElement.class));
            return returnJson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



