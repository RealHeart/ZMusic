package me.zhenxin.zmusic.music.searchSource;

import com.google.gson.*;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.utils.NetUtils;

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
            Gson gson = new Gson();
            int musicId;
            if (!musicName.contains("-id:")) {
                String getUrl = Config.neteaseApiRoot + "search?keywords=" + URLEncoder.encode(musicName, "UTF-8") + "&limit=1&type=1";
                String jsonText = NetUtils.getNetString(getUrl, null);
                JsonObject json = gson.fromJson(jsonText, JsonObject.class);
                JsonObject result = json.getAsJsonObject("result");
                if (result != null || result.get("songCount").getAsInt() != 0) {
                    JsonObject jsonOut = result.getAsJsonArray("songs").get(0).getAsJsonObject();
                    musicId = jsonOut.get("id").getAsInt();
                } else {
                    return null;
                }
            } else {
                musicId = Integer.parseInt(musicName.substring(musicName.indexOf("-id:") + 4));
            }
            String getUrl = Config.neteaseApiRoot + "song/detail?ids=" + musicId;
            String jsonText = NetUtils.getNetString(getUrl, null);
            JsonObject json = gson.fromJson(jsonText, JsonObject.class);
            JsonObject result = json.getAsJsonArray("songs").get(0).getAsJsonObject();
            String name = result.get("name").getAsString();
            int inttime = result.get("dt").getAsInt();
            inttime = inttime / 1000;
            String time = String.valueOf(inttime);
            JsonArray singer = result.get("ar").getAsJsonArray();
            String singerName = "";
            for (JsonElement j : singer) {
                singerName += j.getAsJsonObject().get("name").getAsString() + "/";
            }
            singerName = singerName.substring(0, singerName.length() - 1);

            String lyricJsonText = NetUtils.getNetString(Config.neteaseApiRoot + "lyric?id=" + musicId, null);
            JsonObject lyricJson = gson.fromJson(lyricJsonText, JsonObject.class);

            String lyric = "";
            String lyricTr = "";
            try {
                lyric = lyricJson.get("lrc").getAsJsonObject().get("lyric").getAsString();
                lyric = lyric.replaceAll("\r", "");
                lyricTr = lyricJson.get("tlyric").getAsJsonObject().get("lyric").getAsString();
                lyricTr = lyricTr.replaceAll("\r", "");
            } catch (Exception ignored) {
            }

            JsonObject getUrlJson = gson.fromJson(NetUtils.getNetString(Config.neteaseApiRoot + "song/url/v1?id=" + musicId + "&level=exhigh", null), JsonObject.class);
            String musicUrl = null;
            try {
                musicUrl = getUrlJson.get("data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();
            JsonObject returnJson = new JsonObject();
            returnJson.addProperty("id", musicId);
            returnJson.addProperty("url", musicUrl);
            returnJson.addProperty("time", time);
            returnJson.addProperty("name", name);
            returnJson.addProperty("singer", singerName);
            returnJson.addProperty("lyric", lyric);
            returnJson.addProperty("lyricTr", lyricTr);
            returnJson.addProperty("error", sb.toString());
            return returnJson;
        } catch (
                Exception e) {
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
            String getUrl = Config.neteaseApiRoot + "search?keywords=" + URLEncoder.encode(musicName, "UTF-8") + "&limit=10&type=1";
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

            JsonObject playListInfo = gson.fromJson(NetUtils.postNetString(Config.neteaseApiRoot + "song/detail", null, "ids=" + s), JsonObject.class);
            JsonArray songList = playListInfo.get("songs").getAsJsonArray();

            JsonObject returnJson = new JsonObject();
            JsonArray returnJsonArr = new JsonArray();

            for (JsonElement jsonElement : songList) {
                String songName = jsonElement.getAsJsonObject().get("name").getAsString();
                int songTime = jsonElement.getAsJsonObject().get("dt").getAsInt();
                songTime = songTime / 1000;
                JsonArray ar = jsonElement.getAsJsonObject().get("ar").getAsJsonArray();
                StringBuilder singer = new StringBuilder();
                for (JsonElement j : ar) {
                    try {
                        singer.append(j.getAsJsonObject().get("name").getAsString()).append("/");
                    } catch (Exception ignored) {
                    }
                }
                if (!((singer.length() - 1) < 0)) {
                    singer = new StringBuilder(singer.substring(0, singer.length() - 1));
                } else {
                    singer.append("无");
                }
                String songId = jsonElement.getAsJsonObject().get("id").getAsString();
                JsonObject returnJsonObj = new JsonObject();
                returnJsonObj.addProperty("id", songId);
                returnJsonObj.addProperty("name", songName);
                returnJsonObj.addProperty("singer", singer.toString());
                returnJsonObj.addProperty("time", songTime);
                returnJsonArr.add(returnJsonObj);
            }
            returnJson.addProperty("name", playListName);
            returnJson.addProperty("songs", playListSongs);
            returnJson.add("list", returnJsonArr);
            return returnJson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



