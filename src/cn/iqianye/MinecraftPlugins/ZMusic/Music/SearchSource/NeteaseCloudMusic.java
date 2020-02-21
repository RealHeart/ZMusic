package cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource;

import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NeteaseCloudMusic {

    public NeteaseCloudMusic() {

    }

    /**
     * 获取音乐链接
     *
     * @param musicName 音乐名称
     * @return 音乐链接
     */
    public static JSONObject getMusicUrl(String musicName) {
        try {
            String getUrl =
                    "https://music.163.com/api/search/get/web?csrf_token=Referer="
                            +
                            "http://music.163.com/search/&hlposttag="
                            +
                            URLEncoder.encode("</span>&hlpretag=< span class=\"s-fc7\">", "utf-8")
                            +
                            "&limit=1&s="
                            +
                            URLEncoder.encode(musicName, "utf-8")
                            +
                            "&type=1";
            String jsonText = NetUtils.getNetString(getUrl, null);
            JSONObject json = JSON.parseObject(jsonText);
            JSONObject result = json.getJSONObject("result");
            if (result != null || result.getInteger("songCount") != 0) {
                JSONObject jsonOut = result.getJSONArray("songs").getJSONObject(0);
                Date time = new Date(jsonOut.getIntValue("duration"));
                SimpleDateFormat formats = new SimpleDateFormat("hh:mm:ss");
                int musicID = jsonOut.getIntValue("id");
                String musicUrl = "http://music.163.com/song/media/outer/url?id=" + musicID + ".mp3";
                String lyricJsonText = NetUtils.getNetString("https://music.163.com/api/song/media?id=" + musicID, null);
                JSONObject lyricJson = JSON.parseObject(lyricJsonText);
                String name = jsonOut.getString("name");
                String artist = jsonOut.getJSONArray("artists").getJSONObject(0).getString("name");
                String lyric = lyricJson.getString("lyric");
                int duration = jsonOut.getIntValue("duration");
                JSONObject returnJson = new JSONObject();
                returnJson.put("url", musicUrl);
                returnJson.put("name", name);
                returnJson.put("artist", artist);
                returnJson.put("lyric", lyric);
                returnJson.put("duration",duration);
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
    public static String[] getMusicList(String musicName) {
        try {
            String getUrl =
                    "https://music.163.com/api/search/get/web?csrf_token=Referer="
                            +
                            "http://music.163.com/search/&hlposttag="
                            +
                            URLEncoder.encode("</span>&hlpretag=< span class=\"s-fc7\">", "utf-8")
                            +
                            "&limit=10&s="
                            +
                            URLEncoder.encode(musicName, "utf-8")
                            +
                            "&type=1";
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



