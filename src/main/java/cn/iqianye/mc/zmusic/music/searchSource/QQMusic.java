package cn.iqianye.mc.zmusic.music.searchSource;

import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import com.google.gson.*;
import org.json.simple.JSONArray;

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
            String songmid;
            Gson gson = new GsonBuilder().create();
            if (musicName.contains("-id:")) {
                songmid = musicName.split("-id:")[1];
            } else {
                String getUrl = Config.qqMusicApiRoot + "search?pageSize=1&key=" + URLEncoder.encode(musicName, "utf-8");
                JsonObject json = gson.fromJson(NetUtils.getNetString(getUrl, null), JsonObject.class);
                JsonObject data = json.get("data").getAsJsonObject();
                JsonObject list = data.getAsJsonArray("list").get(0).getAsJsonObject();
                songmid = list.get("songmid").getAsString();
            }
            String getSongInfo = Config.qqMusicApiRoot + "song?songmid=" + songmid;
            String songInfoText = NetUtils.getNetString(getSongInfo, null);
            JsonObject songInfo = gson.fromJson(songInfoText, JsonObject.class);
            songInfo = songInfo.get("data").getAsJsonObject().get("track_info").getAsJsonObject();
            songmid = songInfo.get("mid").getAsString();
            String mediaId = songInfo.get("file").getAsJsonObject().get("media_mid").getAsString();
            int songTime = songInfo.get("interval").getAsInt();
            String songName = songInfo.get("title").getAsString();
            JsonArray singer = songInfo.getAsJsonArray("singer");
            String singerName = "";
            for (JsonElement j : singer) {
                singerName += j.getAsJsonObject().get("name").getAsString() + "/";
            }
            singerName = singerName.substring(0, singerName.length() - 1);
            String getLyricUrl = Config.qqMusicApiRoot + "lyric?songmid=" + songmid;
            String lyricJsonText = NetUtils.getNetString(getLyricUrl, null);
            JsonObject lyricJson = gson.fromJson(lyricJsonText, JsonObject.class);
            String lyric = lyricJson.get("data").getAsJsonObject().get("lyric").getAsString();
            lyric = lyric.replaceAll("&apos;", "'");
            lyric = lyric.replaceAll("\r", "");
            String lyricTr = lyricJson.get("data").getAsJsonObject().get("trans").getAsString();
            lyricTr = lyricTr.replaceAll("&apos;", "'");
            lyricTr = lyricTr.replaceAll("\r", "");
            StringBuilder sb = new StringBuilder();
            if (lyric.isEmpty()) {
                sb.append("未找到歌词信息\n");
            }
            if (lyricTr.isEmpty()) {
                sb.append("未找到歌词翻译\n");
            }
            String getMp3Url = Config.qqMusicApiRoot + "song/url?id=" + songmid + "&mediaId=" + mediaId;
            String getMp3JsonText = NetUtils.getNetString(getMp3Url, null);
            JsonObject getMp3Json = gson.fromJson(getMp3JsonText, JsonObject.class);
            String mp3Url = getMp3Json.get("data").getAsString();
            JsonObject returnJSON = new JsonObject();
            returnJSON.addProperty("url", mp3Url);
            returnJSON.addProperty("time", songTime);
            returnJSON.addProperty("name", songName);
            returnJSON.addProperty("singer", singerName);
            returnJSON.addProperty("lyric", lyric);
            returnJSON.addProperty("lyricTr", lyricTr);
            returnJSON.addProperty("error", sb.toString());
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
            String getUrl = Config.qqMusicApiRoot + "search?pageSize=10&key=" + URLEncoder.encode(musicName, "utf-8");
            Gson gson = new GsonBuilder().create();
            JsonObject json = gson.fromJson(NetUtils.getNetString(getUrl, null), JsonObject.class);
            JsonObject data = json.get("data").getAsJsonObject();
            JsonArray list = data.getAsJsonArray("list");
            JsonArray returnJson = new JsonArray();
            for (JsonElement j : list) {
                String songName = j.getAsJsonObject().get("songname").getAsString();
                String songmid = j.getAsJsonObject().get("songmid").getAsString();
                JsonArray singer = j.getAsJsonObject().getAsJsonArray("singer");
                String singerName = "";
                for (JsonElement js : singer) {
                    singerName += js.getAsJsonObject().get("name").getAsString() + "/";
                }
                singerName = singerName.substring(0, singerName.length() - 1);
                JsonObject returnJsonObj = new JsonObject();
                returnJsonObj.addProperty("id", songmid);
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

    /**
     * 获取音乐歌单
     *
     * @param playListId 歌单ID
     * @return 音乐歌单信息
     */
    public static JsonObject getMusicSongList(String playListId) {
        try {
            String getUrl = Config.qqMusicApiRoot + "songlist?id=" + playListId;
            Gson gson = new GsonBuilder().create();
            JsonObject json = gson.fromJson(NetUtils.getNetString(getUrl, null), JsonObject.class);
            JsonObject data = json.get("data").getAsJsonObject();
            JsonArray songList = data.getAsJsonArray("songlist");
            String songListName = data.get("dissname").getAsString();
            int songListSongNum = data.get("songnum").getAsInt();
            JsonObject returnJson = new JsonObject();
            JSONArray returnJsonArr = new JSONArray();
            for (JsonElement j : songList) {
                String songMid = j.getAsJsonObject().get("songmid").getAsString();
                String strMediaMid = j.getAsJsonObject().get("strMediaMid").getAsString();
                String songName = j.getAsJsonObject().get("songname").getAsString();
                JsonArray singer = j.getAsJsonObject().getAsJsonArray("singer");
                String singerName = "";
                for (JsonElement js : singer) {
                    singerName += js.getAsJsonObject().get("name").getAsString() + "/";
                }
                singerName = singerName.substring(0, singerName.length() - 1);
                int songTime = j.getAsJsonObject().get("interval").getAsInt();
                JsonObject returnJsonObj = new JsonObject();
                returnJsonObj.addProperty("id", songMid);
                returnJsonObj.addProperty("mid", strMediaMid);
                returnJsonObj.addProperty("name", songName);
                returnJsonObj.addProperty("singer", singerName);
                returnJsonObj.addProperty("time", songTime);
                returnJsonArr.add(returnJsonObj);
            }
            returnJson.addProperty("name", songListName);
            returnJson.addProperty("songs", songListSongNum);
            returnJson.add("list", gson.fromJson(returnJsonArr.toJSONString(), JsonElement.class));
            return returnJson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
