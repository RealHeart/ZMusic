package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import cn.iqianye.MinecraftPlugins.ZMusic.Other.Var;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtils {
    /**
     * 获取网络文件返回文本
     *
     * @param url 网络地址
     * @return 获取的文本
     */
    public static String getNetString(String url, String Referer) {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) getUrl.openConnection();
            con.setReadTimeout(20000);
            con.setConnectTimeout(5000);
            con.addRequestProperty("Charset", "UTF-8");
            con.addRequestProperty("Referer", Referer);
            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 10; Redmi K20 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.99 Mobile Safari/537.36 ZMusic/" + Var.thisVer);
            con.setRequestMethod("GET");
            if (con.getResponseCode() == 200) {
                InputStream is = con.getInputStream();
                String s = OtherUtils.readInputStream(is);
                is.close();
                //LogUtils.sendNormalMessage(s);
                return s;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

