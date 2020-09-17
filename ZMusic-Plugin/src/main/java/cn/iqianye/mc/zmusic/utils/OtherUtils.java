package cn.iqianye.mc.zmusic.utils;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.api.AdvancementAPI;
import cn.iqianye.mc.zmusic.api.MultiMap;
import cn.iqianye.mc.zmusic.api.bossbar.BossBar;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.data.PlayerData;
import cn.iqianye.mc.zmusic.music.searchSource.NeteaseCloudMusic;
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
        PlayerData.setPlayerPlayStatus(player, false);
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
        PlayerData.setPlayerMusicName(player, null);
        PlayerData.setPlayerMusicSinger(player, null);
        PlayerData.setPlayerCurrentTime(player, null);
        PlayerData.setPlayerMaxTime(player, null);
        PlayerData.setPlayerLyric(player, null);
        PlayerData.setPlayerPlatform(player, null);
        PlayerData.setPlayerPlaySource(player, null);
    }

    public static void checkUpdate() {
        ZMusic.runTask.runAsync(() -> {
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
                    ZMusic.isLatest = false;
                    ZMusic.latestVer = latestVer;
                    ZMusic.updateLog = updateLog;
                    ZMusic.downloadUrl = downloadUrl;
                    ZMusic.updateUrl = updateUrl;
                    ZMusic.log.sendNormalMessage("发现新版本 V" + ZMusic.latestVer);
                    ZMusic.log.sendNormalMessage("更新日志:");
                    String[] log = ZMusic.updateLog.split("\\n");
                    for (String s : log) {
                        ZMusic.log.sendNormalMessage(s);
                    }
                    if (Config.update) {
                        ZMusic.log.sendNormalMessage("已开启自动更新，正在下载最新版本中....");
                        File file = ZMusic.dataFolder;
                        file = new File(file, "ZMusic-" + latestVer + ".jar");
                        String md5 = "";
                        try {
                            md5 = getMD5Three(file.getAbsolutePath());
                        } catch (IOException | NoSuchAlgorithmException e) {
                            ZMusic.log.sendDebugMessage(e.getMessage());
                        }
                        if (!md5.equals(updateMD5)) {
                            try {
                                OtherUtils.writeToLocal(file.getAbsolutePath(), NetUtils.getNetInputStream(updateUrl));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        ZMusic.log.sendNormalMessage("下载完成，文件已保存至插件文件夹update目录，请手动更新.");
                    } else {
                        ZMusic.log.sendNormalMessage("下载地址: §e§n" + ZMusic.downloadUrl);
                    }
                } else {
                    ZMusic.log.sendNormalMessage("已是最新版本!");
                    ZMusic.isLatest = true;
                }
            } else {
                ZMusic.log.sendErrorMessage("检查更新失败!");
                ZMusic.isLatest = true;
            }
        });
    }

    /**
     * 登录网易云音乐
     */
    public static void loginNetease() {
        ZMusic.runTask.runAsync(() -> {
            try {
                if (!Config.neteaseAccount.equalsIgnoreCase("18888888888")) {
                    ZMusic.log.sendNormalMessage("正在尝试登录网易云音乐...");
                    String s = null;
                    if (Config.neteaseloginType.equalsIgnoreCase("phone")) {
                        s = Config.neteaseApiRoot + "login/cellphone?phone=" + Config.neteaseAccount + "&md5_password=" + URLEncoder.encode(Config.neteasePassword, "UTF-8");
                    } else if (Config.neteaseloginType.equalsIgnoreCase("email")) {
                        s = Config.neteaseApiRoot + "login?email=" + Config.neteaseAccount + "&md5_password=" + URLEncoder.encode(Config.neteasePassword, "UTF-8");
                    }
                    String jsonText = NetUtils.getNetString(s, null);
                    Gson gson = new GsonBuilder().create();
                    JsonObject json = gson.fromJson(jsonText, JsonObject.class);
                    if (jsonText != null) {
                        ZMusic.log.sendNormalMessage("登录成功,欢迎你: " + json.get("profile").getAsJsonObject().get("nickname").getAsString());
                        if (Config.neteaseFollow) {
                            // 关注“QG真心”的网易云账号
                            ZMusic.log.sendDebugMessage(NetUtils.getNetString(Config.neteaseApiRoot + "follow?id=265857414&t=1", null));
                        }
                    } else {
                        ZMusic.log.sendErrorMessage("登录失败: 请检查账号密码是否正确。");
                    }
                } else {
                    ZMusic.log.sendErrorMessage("登录失败：请在配置文件设置账号密码。");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            String temp = "";
            while ((temp = br.readLine()) != null) {
                // 拼接换行符
                sb.append(temp).append("\n");
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        BigInteger bi = null;
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
                ZMusic.runTask.run(() -> new AdvancementAPI(title).sendAdvancement(player));
            }
        }
    }
}