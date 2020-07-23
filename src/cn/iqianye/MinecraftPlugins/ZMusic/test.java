package cn.iqianye.MinecraftPlugins.ZMusic;

import cn.iqianye.MinecraftPlugins.ZMusic.Utils.JSSecret;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;

import java.util.HashMap;
import java.util.Map;


public class test {

    public static void main(String[] args) {
        String text = "{'id': 813333839, 'offset': 0, 'total': True, 'limit': 1000, 'n': 1000, 'csrf_token': csrf}";
        Map data = new HashMap();
        try {
            //data = NewMusicEncrypt.getData(text);
            data = JSSecret.getData(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(data);
        System.out.println(data.get("encSecKey").toString());
        System.out.println(data.get("params").toString());
        String s = "encSecKey=" + data.get("encSecKey").toString() + "&params=" + data.get("params").toString();
        System.out.println(s);
        System.out.println(NetUtils.getNetStringPOST("http://music.163.com/weapi/v3/playlist/detail?csrf_token=", "http://music.163.com/", s));
    }
}
