package cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource;

import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;
import com.google.gson.*;

import java.net.URLEncoder;
import java.util.Base64;
import java.util.Random;

public class QQMusic {

    /**
     * 获取音乐链接
     *
     * @param musicName 音乐名称
     * @return 音乐链接
     */
    public static JsonObject getMusicUrl(String musicName) {
        try {
            String getUrl =
                    "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?aggr=1&cr=1&flag_qc=0&p=1&n=1&format=json&w="
                            +
                            URLEncoder.encode(musicName, "utf-8");
            Gson gson = new GsonBuilder()//建造者模式设置不同的配置
                    .serializeNulls()//序列化为null对象
                    .create();
            JsonObject json = gson.fromJson(NetUtils.getNetString(getUrl, null), JsonObject.class);
            JsonObject data = json.get("data").getAsJsonObject();
            JsonObject song = data.get("song").getAsJsonObject();
            JsonObject list = song.getAsJsonArray("list").get(0).getAsJsonObject();
            String songid = list.get("songmid").getAsString();
            String songName = list.get("songname").getAsString();
            JsonArray singer = list.getAsJsonArray("singer");
            String singerName = "";
            for (JsonElement j : singer) {
                singerName += j.getAsJsonObject().get("name").getAsString() + "/";
            }
            singerName = singerName.substring(0, singerName.length() - 1);
            String getHTMLUrl = "https://i.y.qq.com/v8/playsong.html?songmid=" + songid;
            String HTML = NetUtils.getNetString(getHTMLUrl, null);
            String m4aurl = HTML.split("m4aUrl\":\"")[1].split("\",")[0];
            String songUrlRoot = m4aurl.split("C400")[0];
            String songmid = m4aurl.split("C400")[1].split("\\.m4a")[0];
            Random r = new Random();
            int guid = new Random().nextInt(999999999) + 100000000;
            String getVkey = "https://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg?format=json&loginUin=0&platform=yqq&cid=205361747&uin=0&guid=" +
                    guid + "&songmid=" +
                    songmid + "&filename=M500" +
                    songmid + ".mp3";
            JsonObject vkeyJosn = gson.fromJson(NetUtils.getNetString(getVkey, null), JsonObject.class);
            String vkey = vkeyJosn.get("data").getAsJsonObject().getAsJsonArray("items").get(0).getAsJsonObject().get("vkey").getAsString();
            String getLyricUrl = "https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric.fcg?g_tk=5381&uin=0&format=json&outCharset=utf-8&songmid=" + songid;
            String lyricJsonStr = NetUtils.getNetString(getLyricUrl, getHTMLUrl);
            lyricJsonStr = lyricJsonStr.replaceAll("\\(", "");
            lyricJsonStr = lyricJsonStr.replaceAll("\\)", "");
            lyricJsonStr = lyricJsonStr.replaceAll("MusicJsonCallback", "");
            JsonObject lyricJSON = gson.fromJson(lyricJsonStr, JsonObject.class);
            String lyricBase64 = lyricJSON.get("lyric").getAsString();
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(lyricBase64);
            String lyric = new String(bytes, "utf-8");
            String mp3Url = songUrlRoot + "M500" + songmid + ".mp3?guid=" + guid + "&vkey=" + vkey + "&uin=0&fromtag=64";

            JsonObject returnJSON = new JsonObject();
            returnJSON.addProperty("url", mp3Url);
            returnJSON.addProperty("name", songName);
            returnJSON.addProperty("singer", singerName);
            returnJSON.addProperty("lyric", lyric);
            return returnJSON;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
