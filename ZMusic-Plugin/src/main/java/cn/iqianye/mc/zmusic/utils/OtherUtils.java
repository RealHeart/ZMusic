package cn.iqianye.mc.zmusic.utils;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.api.MultiMap;
import cn.iqianye.mc.zmusic.api.bossbar.BossBar;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.data.PlayerData;
import cn.iqianye.mc.zmusic.music.searchSource.NeteaseCloudMusic;
import cn.iqianye.mc.zmusic.proto.Toast;
import com.google.gson.*;

import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtherUtils {

    /**
     * 参数合一
     *
     * @param args 参数
     * @return 合并的值
     */
    public static String argsXin1(String[] args) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i != 0 & i != 1) {
                s.append(args[i]).append(" ");
            }
        }
        return s.toString().trim();
    }

    /**
     * 参数合一
     *
     * @param args 参数
     * @return 合并的值
     */
    public static String argsXin1(String[] args, String isComm) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i != 0) {
                s.append(args[i]).append(" ");
            }
        }
        return s.toString().trim();
    }

    public static void resetPlayerStatus(Object player) {
        ZMusic.music.stop(player);
        if (Config.supportBossBar) {
            BossBar bossBar = PlayerData.getPlayerBoosBar(player);
            if (bossBar != null) {
                bossBar.removePlayer(player);
            }
        }
        if (Config.supportTitle) {
            ZMusic.message.sendTitleMessage("", "", player);
        }
        if (Config.supportHud) {
            ZMusic.send.sendAM(player, "[Lyric]");
            ZMusic.send.sendAM(player, "[Info]");
        }
        PlayerData.setPlayerPlayStatus(player, false);
        PlayerData.setPlayerMusicName(player, null);
        PlayerData.setPlayerMusicSinger(player, null);
        PlayerData.setPlayerCurrentTime(player, null);
        PlayerData.setPlayerMaxTime(player, null);
        PlayerData.setPlayerLyric(player, null);
        PlayerData.setPlayerPlatform(player, null);
        PlayerData.setPlayerPlaySource(player, null);
    }

    public static void checkUpdate(Object sender, boolean aSync) {
        Runnable r = () -> {
            ZMusic.message.sendNormalMessage("正在检查更新...", sender);
            String jsonText = NetUtils.getNetString("https://api.zhenxin.xyz/minecraft/plugins/ZMusic/version.json", null);
            if (jsonText != null) {
                Gson gson = new Gson();
                JsonObject json = gson.fromJson(jsonText, JsonObject.class);
                String latestVer = json.get("latestVer").getAsString();
                int latestVerCode = json.get("latestVerCode").getAsInt();
                String updateLog = json.get("updateLog").getAsString();
                String downloadUrl = json.get("downloadUrl").getAsString();
                String updateUrl = json.get("updateUrl").getAsString();
                String updateMD5 = json.get("updateMD5").getAsString();
                if (ZMusic.thisVerCode < latestVerCode) {
                    ZMusic.message.sendNormalMessage("发现新版本 V" + latestVer, sender);
                    ZMusic.message.sendNormalMessage("更新日志:", sender);
                    String[] log = updateLog.split("\\n");
                    for (String s : log) {
                        ZMusic.message.sendNormalMessage(s, sender);
                    }
                    if (Config.update) {
                        ZMusic.message.sendNormalMessage("已开启自动更新，正在下载最新版本中....", sender);
                        File file = ZMusic.dataFolder;
                        file = new File(file, "ZMusic-" + latestVer + ".jar");
                        String md5 = "";
                        try {
                            md5 = getMD5Three(file.getAbsolutePath());
                        } catch (Exception ignored) {
                        }
                        if (!md5.equals(updateMD5)) {
                            try {
                                OtherUtils.writeToLocal(file.getAbsolutePath(), NetUtils.getNetInputStream(updateUrl));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        ZMusic.message.sendNormalMessage("下载完成，文件已保存至插件文件夹update目录，请手动更新.", sender);
                    } else {
                        ZMusic.message.sendNormalMessage("下载地址: §e§n" + downloadUrl, sender);
                    }
                } else {
                    ZMusic.message.sendNormalMessage("已是最新版本!", sender);
                }
            } else {
                ZMusic.message.sendErrorMessage("检查更新失败!", sender);
            }
        };
        if (aSync) {
            ZMusic.runTask.runAsync(r);
        } else r.run();
    }

    public static void neteaseHotComments(Object player, String musicName) {
        ZMusic.runTask.runAsync(() -> {
            try {
                Gson gson = new GsonBuilder().create();
                JsonObject json = NeteaseCloudMusic.getMusicUrl(musicName);
                String musicId = json.get("id").getAsString();
                JsonObject jsonObject = gson.fromJson(NetUtils.getNetString("http://netease.api.zhenxin.xyz/comment/hot?limit=3&type=0&id=" + musicId, null), JsonObject.class);
                JsonArray jsonArray = jsonObject.get("hotComments").getAsJsonArray();
                ZMusic.message.sendNormalMessage("====== [" + json.get("name").getAsString() + "] 的热门评论 =====", player);
                for (JsonElement j : jsonArray) {
                    ZMusic.message.sendNormalMessage(j.getAsJsonObject().get("content").getAsString() + "\nBy: "
                            + j.getAsJsonObject().get("user").getAsJsonObject().get("nickname").getAsString(), player);
                }
                ZMusic.message.sendNormalMessage("=================================", player);
            } catch (Exception e) {
                ZMusic.message.sendErrorMessage("获取评论失败。", player);
            }
        });
    }

    /**
     * 从输入流中获取字符串
     *
     * @param inputStream 输入流
     * @return 文本
     * @throws IOException IO异常
     */
    public static String readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return new String(bos.toByteArray(), StandardCharsets.UTF_8);
    }

    public static String readInputStream(InputStreamReader inputStream) throws IOException {
        char[] cbuffer = new char[1024];
        int len = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ((len = inputStream.read(cbuffer)) != -1) {
            stringBuilder.append(cbuffer);
        }
        return stringBuilder.toString();
    }


    /**
     * 格式化歌词信息
     *
     * @param lyric   歌词
     * @param lyricTr 歌词翻译
     * @return 格式化后的Json
     */
    public static JsonObject formatLyric(String lyric, String lyricTr) {
        Map<Long, String> lrcMap = formatLyric(lyric);
        Map<Long, String> lrcTrMap = formatLyric(lyricTr);
        JsonObject json = new JsonObject();
        for (Map.Entry<Long, String> entry : lrcMap.entrySet()) {
            JsonObject j = new JsonObject();
            j.addProperty("lrc", entry.getValue());
            if (lrcTrMap.get(entry.getKey()) != null) {
                j.addProperty("lrcTr", lrcTrMap.get(entry.getKey()));
            } else {
                j.addProperty("lrcTr", "");
            }
            json.add(String.valueOf(entry.getKey()), j);
        }
        return json;
    }

    private static Map<Long, String> formatLyric(String lyric) {
        Map<Long, String> map = new HashMap<>();
        MultiMap<Long, String> multiMap = new MultiMap<>();
        if (lyric.isEmpty()) {
            return map;
        }
        String[] lyrics = lyric.split("\n");
        String regex = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})\\]";
        Pattern pattern = Pattern.compile(regex);
        for (String lrc : lyrics) {
            Matcher matcher = pattern.matcher(lrc);
            while (matcher.find()) {
                String min = matcher.group(1);
                String sec = matcher.group(2);
                String mill = matcher.group(3);
                if (mill.length() > 2) {
                    switch (mill.length()) {
                        case 2:
                            mill = String.valueOf(Integer.parseInt(mill));
                        case 3:
                            mill = mill.substring(0, mill.length() - 1);
                            mill = String.valueOf(Integer.parseInt(mill));
                    }
                }
                long time = timeToSec(min, sec, mill);
                String text = lrc.substring(matcher.end());
                text = text.replaceAll("\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})\\]", "");
                multiMap.put(time, text);
            }
        }
        for (int i = 0; i < multiMap.getSize(); i++) {
            Map<Long, List<String>> lrcs = multiMap.get(i);
            for (Map.Entry<Long, List<String>> e : lrcs.entrySet()) {
                List<String> eValue = e.getValue();
                StringBuilder sb = new StringBuilder();
                for (String s : eValue) {
                    sb.append(s).append("\n");
                }
                String s = sb.substring(0, sb.length() - 1);
                map.put(e.getKey(), s);
            }
        }
        return map;
    }

    /**
     * 将分,秒,毫秒转为毫秒
     *
     * @param min  分
     * @param sec  秒
     * @param mill 毫秒
     * @return 毫秒
     */
    private static long timeToSec(String min, String sec, String mill) {
        int m = Integer.parseInt(min);
        int s = Integer.parseInt(sec);
        int ms = Integer.parseInt(mill);
        if (s >= 60) {
            ZMusic.log.sendErrorMessage("警告: 出现了一个时间不正确的项 --> [" + min + ":" + sec + "." + mill.substring(0, 2) + "]");
        }
        return (m * 60 * 1000 + s * 1000 + ms) / 1000;
    }

    public static String getMD5String(String str) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(str.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ArrayList<String> queryFileNames(String filePath) {
        ArrayList<String> es = new ArrayList<>();
        File f = new File(filePath);
        if (!f.exists()) {
            return null;
        }
        File[] fs = f.listFiles();
        for (File file : fs) {
            if (file.isFile()) {
                es.add(file.getName());
            }
        }
        return es;
    }

    public static String readFileToString(File file) {
        String s = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            s = readFileToString(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String readFileToString(InputStreamReader isr) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n"); // 补上换行符
            }
            isr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 将文本写入本地文件
     *
     * @param file 文件路径
     * @param text 文本
     * @throws IOException IOException
     */
    public static void saveStringToLocal(File file, String text) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        osw.write(text);
        osw.flush();
        osw.close();
    }

    /**
     * 将InputStream写入本地文件
     *
     * @param destination 写入本地目录
     * @param input       输入流
     * @throws IOException IOException
     */
    public static void writeToLocal(String destination, InputStream input) throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        input.close();
        downloadFile.close();
    }

    public static String getMD5Three(String path) throws IOException, NoSuchAlgorithmException {
        BigInteger bi;
        byte[] buffer = new byte[8192];
        int len = 0;
        MessageDigest md = MessageDigest.getInstance("MD5");
        File f = new File(path);
        FileInputStream fis = new FileInputStream(f);
        while ((len = fis.read(buffer)) != -1) {
            md.update(buffer, 0, len);
        }
        fis.close();
        byte[] b = md.digest();
        bi = new BigInteger(1, b);
        return bi.toString(16);
    }

    public static String formatTime(Long time) {
        if (time != null) {
            if (time < 60) {
                return "00" + ":" + String.format("%02d", time);
            } else if (time < 3600) {
                long m = time / 60;
                long s = time % 60;
                return String.format("%02d", m) + ":" + String.format("%02d", s);
            } else {
                long h = time / 3600;
                long m = (time % 3600) / 60;
                long s = (time % 3600) % 60;
                return String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s);
            }
        } else {
            return "--:--";
        }
    }

    public static void sendAdv(Object player, String title) {
        if (Config.realSupportAdvancement) {
            if (ZMusic.isBC) {
                JsonObject json = new JsonObject();
                json.addProperty("isAdv", true);
                json.addProperty("title", title);
                ZMusic.send.sendToZMusicAddon(player, json.toString());
            } else {
                ZMusic.runTask.run(() -> {
                    Toast.sendToast(player, title);
                });
            }
        }
    }
}
