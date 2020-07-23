package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import cn.iqianye.MinecraftPlugins.ZMusic.Other.Val;

import java.io.DataOutputStream;
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
            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 11; Mi 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.99 Mobile Safari/537.36 ZMusic/" + Val.thisVer);
            con.addRequestProperty("Cookie", "appver=2.0.2");
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

    /**
     * 获取网络文件返回文本
     *
     * @param url 网络地址
     * @return 获取的文本
     */
    public static String getNetStringPOST(String url, String Referer, String content) {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) getUrl.openConnection();
            con.setReadTimeout(20000);
            con.setConnectTimeout(5000);
            con.addRequestProperty("Charset", "UTF-8");
            con.addRequestProperty("Referer", Referer);
            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 11; Mi 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.99 Mobile Safari/537.36 ZMusic/" + Val.thisVer);
            con.addRequestProperty("Cookie", "appver=2.0.2");
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();
            //DataOutputStream流
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            //将要上传的内容写入流中
            out.writeBytes(content);
            //刷新、关闭
            out.flush();
            out.close();
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

