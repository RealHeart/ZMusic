package cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource;

import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;

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
                    "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?g_tk=5381&p=1&n=1&w="
                            +
                            URLEncoder.encode(musicName, "utf-8")
                            +
                            "&format=jsonp&jsonpCallback=callback&loginUin=0&hostUin=0&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&remoteplace=txt.yqq.song&t=0&aggr=1&cr=1&catZhida=1&flag_qc=0";
            String ret = NetUtils.getNetString(getUrl,null);
            String songid = ret.split("songmid\":\"")[1].split("\",")[0];
            String url = "https://i.y.qq.com/v8/playsong.html?songmid=" + songid + "&ADTAG=myqq&from=myqq&channel=10007100";
            String ret2 = NetUtils.getNetString(url,null);
            String songurl = ret2.split("m4aUrl\":\"")[1].split("\",")[0];
            return songurl;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
