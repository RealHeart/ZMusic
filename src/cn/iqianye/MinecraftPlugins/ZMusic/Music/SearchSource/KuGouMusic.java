package cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource;

import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;

public class KuGouMusic {

    /**
     * 获取音乐链接
     *
     * @param musicName 音乐名称
     */
    public static JSONObject getMusicUrl(String musicName) {
        try {


            String getUrl =
                    "https://songsearch.kugou.com/song_search_v2?keyword="
                            +
                            URLEncoder.encode(musicName, "utf-8")
                            +
                            "&platform=WebFilter&format=json&page=1&pagesize=1";
            String jsonText = NetUtils.getNetString(getUrl, null);
            JSONObject json = JSON.parseObject(jsonText);
            if (json.getIntValue("status") == 1) {
                JSONObject data = json.getJSONObject("data");
                JSONObject list = data.getJSONArray("lists").getJSONObject(0);
                String songName = list.getString("SongName");
                String songSinger = list.getString("SingerName");
                String hash = list.getString("FileHash");
                String id = list.getString("ID");
                String getInfoUrl = "http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=" + hash;
                String infoJsonText = NetUtils.getNetString(getInfoUrl, "http://m.kugou.com/play/info/" + id);
                JSONObject infoJosn = JSON.parseObject(infoJsonText);
                String song_url = infoJosn.getString("url");
                JSONObject returnObject = new JSONObject();
                returnObject.put("name", songName);
                returnObject.put("singer", songSinger);
                returnObject.put("url", song_url);
                return returnObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }
}
