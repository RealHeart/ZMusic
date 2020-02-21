package cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource;

import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;
import java.util.Random;

public class QQMusic {

    /**
     * 获取音乐链接
     *
     * @param musicName 音乐名称
     * @return 音乐链接
     */
    public static String getMusicUrl(String musicName) {
        try {
            String getUrl =
                    "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?aggr=1&cr=1&flag_qc=0&p=1&n=1&format=json&w="
                            +
                            URLEncoder.encode(musicName, "utf-8");
            JSONObject json = JSON.parseObject(NetUtils.getNetString(getUrl, null));
            JSONObject data = json.getJSONObject("data");
            JSONObject song = data.getJSONObject("song");
            JSONObject list = song.getJSONArray("list").getJSONObject(0);
            String songid = list.getString("songmid");
            String getHTMLUrl = "https://i.y.qq.com/v8/playsong.html?songmid=" + songid;
            String HTML = NetUtils.getNetString(getHTMLUrl, null);
            String m4aurl = HTML.split("m4aUrl\":\"")[1].split("\",")[0];
            String songUrlRoot = m4aurl.split("C400")[0];
            String songmid = m4aurl.split("C400")[1].split("\\.m4a")[0];
            Random r = new Random();
            int guid = new Random().nextInt(999999999) + 100000000;
            /*
            String getMp3Url = "https://api.zhenxin.xyz/minecraft/plugins/ZMusic/m4a2mp3.php?url=" + URLEncoder.encode(m4aurl,"utf-8");
            String mp3Url = NetUtils.getNetString(getMp3Url,null);
            return mp3Url;
             */
            String getVkey = "https://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg?&format=json&loginUin=0&platform=yqq&cid=205361747&uin=0&guid=" +
                    guid + "&songmid=" +
                    songmid + "&filename=M500" +
                    songmid + ".mp3";
            JSONObject vkeyJosn = JSON.parseObject(NetUtils.getNetString(getVkey, null));
            String vkey = vkeyJosn.getJSONObject("data").getJSONArray("items").getJSONObject(0).getString("vkey");
            int subcode = vkeyJosn.getJSONObject("data").getJSONArray("items").getJSONObject(0).getIntValue("subcode");
            if (subcode == 0) {
                String mp3Url = songUrlRoot + "M500" + songmid + ".mp3?guid=" + guid + "&vkey=" + vkey + "&uin=0&fromtag=64";
                return mp3Url;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
