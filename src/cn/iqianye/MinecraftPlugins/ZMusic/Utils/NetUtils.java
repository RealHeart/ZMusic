package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
            con.setReadTimeout(5000);
            con.setConnectTimeout(5000);
            con.addRequestProperty("Charset", "UTF-8");
            con.addRequestProperty("Referer", Referer);
            con.setRequestMethod("GET");
            if (con.getResponseCode() == 200) {
                InputStream is = con.getInputStream();
                String jsonText = readInputStream(is);
                is.close();
                return jsonText;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从输入流中获取字符串
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return new String(bos.toByteArray(), "utf-8");
    }
}

