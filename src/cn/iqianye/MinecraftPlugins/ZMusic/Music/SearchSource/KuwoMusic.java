package cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource;

import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.net.URLEncoder;

public class KuwoMusic {
    /**
     * 获取音乐链接
     *
     * @param musicName 音乐名称
     */
    public static JsonObject getMusicUrl(String musicName) {
        try {
            String getUrl = "http://search.kuwo.cn/r.s?all=" + URLEncoder.encode(musicName, "utf-8") + "&ft=music&itemset=web_2013&client=kt&pn=0&rn=1&rformat=json&encoding=utf8";
            String jsonStr = NetUtils.getNetString(getUrl, null);
            Gson gson = new GsonBuilder().create();
            JsonObject json = gson.fromJson(jsonStr, JsonObject.class);
            JsonObject abslist = json.getAsJsonArray("abslist").get(0).getAsJsonObject();
            String musicID = abslist.get("MUSICRID").getAsString().replaceAll("MUSIC_", "");
            String name = abslist.get("NAME").getAsString();
            String singer = abslist.get("ARTIST").getAsString();
            String url = NetUtils.getNetString("http://antiserver.kuwo.cn/anti.s?type=convert_url&format=mp3&rid=" + musicID, null);
            JsonObject returnJson = new JsonObject();
            returnJson.addProperty("url", url);
            returnJson.addProperty("name", name);
            returnJson.addProperty("singer", singer);
            returnJson.addProperty("lyric", "");
            return returnJson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
