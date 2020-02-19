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

public class NeteaseCloudMusic {

    public NeteaseCloudMusic(){

    }

    /**
     * 获取音乐链接
     *
     * @param musicName 音乐名称
     * @return 音乐链接
     */
    public static String getMusicUrl(String musicName) {
        try {
            String getUrl =
                    "http://music.163.com/api/search/get/web?csrf_token=Referer="
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
            String jsonText = NetUtils.getNetString(getUrl,null);
            JSONObject json = JSON.parseObject(jsonText);
            JSONObject result = json.getJSONObject("result");
            if (result != null || result.getInteger("songCount") != 0) {
                JSONObject jsonOut = result.getJSONArray("songs").getJSONObject(0);
                long musicID = jsonOut.getLongValue("id");
                String musicUrl = "http://music.163.com/song/media/outer/url?id=" + musicID + ".mp3";
                return musicUrl;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



