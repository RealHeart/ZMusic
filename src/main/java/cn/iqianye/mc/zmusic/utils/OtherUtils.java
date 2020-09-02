package cn.iqianye.mc.zmusic.utils;

import cn.iqianye.mc.zmusic.api.BossBar;
import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.music.PlayListPlayer;
import cn.iqianye.mc.zmusic.music.searchSource.NeteaseCloudMusic;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import com.google.gson.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;
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

    /**
     * 重置玩家状态
     *
     * @param player 玩家列表
     */
    public static void resetPlayerStatus(Player player) {
        PlayerStatus.setPlayerPlayStatus(player, false);
        Timer timer = PlayerStatus.getPlayerTimer(player);
        if (timer != null) {
            timer.cancel();
        }
        PlayListPlayer playListPlayer = PlayerStatus.getPlayerPlayListPlayer(player);
        if (playListPlayer != null) {
            if (playListPlayer.isPlayEd) {
                playListPlayer.isStop = true;
            }
        }
        if (Config.supportBossBar) {
            BossBar bossBar = PlayerStatus.getPlayerBoosBar(player);
            if (bossBar != null) {
                bossBar.removePlayer(player);
            }
        }
        if (Config.supportTitle) {
            try {
                player.sendTitle("", "", 0, 0, 0);
            } catch (NoSuchMethodError e) {
                player.sendTitle("", "");
            }
        }
        PlayerStatus.setPlayerMusicName(player, null);
        PlayerStatus.setPlayerCurrentTime(player, null);
        PlayerStatus.setPlayerMaxTime(player, null);
        PlayerStatus.setPlayerLyric(player, null);
        PlayerStatus.setPlayerPlatform(player, null);
        PlayerStatus.setPlayerPlaySource(player, null);
    }

    /**
     * 重置全部玩家状态
     *
     * @param players 玩家列表
     */
    public static void resetPlayerStatusAll(List<Player> players) {
        for (Player player : players) {
            PlayerStatus.setPlayerPlayStatus(player, false);
            Timer timer = PlayerStatus.getPlayerTimer(player);
            if (timer != null) {
                timer.cancel();
            }
            PlayListPlayer playListPlayer = PlayerStatus.getPlayerPlayListPlayer(player);
            if (playListPlayer != null) {
                if (playListPlayer.isPlayEd) {
                    playListPlayer.isStop = true;
                }
            }
            if (Config.supportBossBar) {
                BossBar bossBar = PlayerStatus.getPlayerBoosBar(player);
                if (bossBar != null) {
                    bossBar.removePlayer(player);
                }
            }
            if (Config.supportTitle) {
                try {
                    player.sendTitle("", "", 0, 0, 0);
                } catch (NoSuchMethodError e) {
                    player.sendTitle("", "");
                }
            }
            PlayerStatus.setPlayerMusicName(player, null);
            PlayerStatus.setPlayerCurrentTime(player, null);
            PlayerStatus.setPlayerMaxTime(player, null);
            PlayerStatus.setPlayerLyric(player, null);
            PlayerStatus.setPlayerPlatform(player, null);
            PlayerStatus.setPlayerPlaySource(player, null);
        }
    }

    /**
     * 检查更新
     *
     * @param ver    当前版本
     * @param sender CommandSender
     */
    public static void checkUpdate(String ver, CommandSender sender) {
        new Thread(() -> {
            String jsonText = NetUtils.getNetString("https://api.zhenxin.xyz/minecraft/plugins/ZMusic/version.json", null);
            if (jsonText != null) {
                Gson gson = new Gson();
                JsonObject json = gson.fromJson(jsonText, JsonObject.class);
                String latestVer = json.get("latestVer").getAsString();
                int latestVerCode = json.get("latestVerCode").getAsInt();
                String updateLog = json.get("updateLog").getAsString();
                String downloadUrl = json.get("downloadUrl").getAsString();
                if (Val.thisVerCode < latestVerCode) {
                    Val.isLatest = false;
                    Val.latestVer = latestVer;
                    Val.updateLog = updateLog;
                    Val.downloadUrl = downloadUrl;
                    LogUtils.sendNormalMessage("发现新版本 V" + Val.latestVer);
                    LogUtils.sendNormalMessage("更新日志:");
                    String[] log = Val.updateLog.split("\\n");
                    for (String s : log) {
                        LogUtils.sendNormalMessage(s);
                    }
                    LogUtils.sendNormalMessage("下载地址: " + ChatColor.YELLOW + ChatColor.UNDERLINE + Val.downloadUrl);
                } else {
                    LogUtils.sendNormalMessage("已是最新版本!");
                    Val.isLatest = true;
                }
            } else {
                LogUtils.sendErrorMessage("检查更新失败!");
                Val.isLatest = true;
                if (sender != null) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (player.hasPermission("zmusic.admin") || player.isOp()) {
                            MessageUtils.sendErrorMessage("检查更新失败!", player);
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 登录网易云音乐
     */
    public static void loginNetease(CommandSender sender) {
        new Thread(() -> {
            try {
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (!Config.neteaseAccount.equalsIgnoreCase("18888888888")) {
                    if (player != null) {
                        MessageUtils.sendNormalMessage("正在尝试登录网易云音乐...", player);
                    }
                    LogUtils.sendNormalMessage("正在尝试登录网易云音乐...");
                    String s = null;
                    String c = null;
                    if (Config.neteaseloginType.equalsIgnoreCase("phone")) {
                        s = Val.neteaseApiRoot + "login/cellphone?phone=" + Config.neteaseAccount + "&md5_password=" + URLEncoder.encode(Config.neteasePassword, "UTF-8");
                    } else if (Config.neteaseloginType.equalsIgnoreCase("email")) {
                        s = Val.neteaseApiRoot + "login?email=" + Config.neteaseAccount + "&md5_password=" + URLEncoder.encode(Config.neteasePassword, "UTF-8");
                    }
                    String jsonText = NetUtils.getNetString(s, null);
                    Gson gson = new GsonBuilder().create();
                    JsonObject json = gson.fromJson(jsonText, JsonObject.class);
                    if (jsonText != null) {
                        Val.neteaseCookie = URLEncoder.encode(json.get("cookie").getAsString(), "UTF-8");
                        if (player != null) {
                            MessageUtils.sendNormalMessage("登录成功,欢迎你: " + json.get("profile").getAsJsonObject().get("nickname").getAsString(), player);
                        }
                        LogUtils.sendNormalMessage("登录成功,欢迎你: " + json.get("profile").getAsJsonObject().get("nickname").getAsString());
                        if (Config.neteaseFollow) {
                            // 关注“QG真心”的网易云账号
                            NetUtils.getNetString(Val.neteaseApiRoot + "follow?id=265857414&t=1&cookie=" + Val.neteaseCookie, null);
                        }
                    } else {
                        if (player != null) {
                            MessageUtils.sendNormalMessage("登录失败: 请检查账号密码是否正确。", player);
                        }
                        LogUtils.sendErrorMessage("登录失败: 请检查账号密码是否正确。");
                    }
                } else {
                    if (player != null) {
                        MessageUtils.sendNormalMessage("登录失败：请在配置文件设置账号密码。", player);
                    }
                    LogUtils.sendErrorMessage("登录失败：请在配置文件设置账号密码。");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void neteaseHotComments(Player player, String musicName) {
        new Thread(() -> {
            try {
                Gson gson = new GsonBuilder().create();
                JsonObject json = NeteaseCloudMusic.getMusicUrl(musicName);
                String musicId = json.get("id").getAsString();
                JsonObject jsonObject = gson.fromJson(NetUtils.getNetString("http://netease.api.zhenxin.xyz/comment/hot?limit=3&type=0&id=" + musicId, null), JsonObject.class);
                JsonArray jsonArray = jsonObject.get("hotComments").getAsJsonArray();
                MessageUtils.sendNormalMessage("====== [" + json.get("name").getAsString() + "] 的热门评论 =====", player);
                for (JsonElement j : jsonArray) {
                    MessageUtils.sendNormalMessage(j.getAsJsonObject().get("content").getAsString() + "\nBy: "
                            + j.getAsJsonObject().get("user").getAsJsonObject().get("nickname").getAsString(), player);
                }
                MessageUtils.sendNormalMessage("=================================", player);
            } catch (Exception e) {
                MessageUtils.sendErrorMessage("获取评论失败。", player);
            }
        }).start();
    }

    /**
     * 从输入流中获取字符串
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return new String(bos.toByteArray(), "utf-8");
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
     * @param lyric 歌词
     * @return 格式化后的List
     */
    public static List<Map<Integer, String>> formatLyric(String lyric) {
        if (lyric != null) {
            String[] lyrics = lyric.split("\\n");
            List<Map<Integer, String>> list = new ArrayList<>();
            String regex = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})\\]"; // 正则表达式
            Pattern pattern = Pattern.compile(regex); // 创建 Pattern 对象
            for (String i : lyrics) {
                Matcher matcher = pattern.matcher(i);
                while (matcher.find()) {
                    // 用于存储当前时间和文字信息的容器
                    Map<Integer, String> map = new HashMap<>();
                    String min = matcher.group(1); // 分钟
                    String sec = matcher.group(2); // 秒
                    String mill = matcher.group(3); // 毫秒，注意这里其实还要乘以10
                    if (mill.length() > 2) {
                        switch (mill.length()) {
                            case 2:
                                mill = String.valueOf(Integer.parseInt(mill) * 10);
                            case 3:
                                mill = mill.substring(0, mill.length() - 1);
                                mill = String.valueOf(Integer.parseInt(mill) * 10);
                        }
                    }
                    int time = timeToSec(min, sec, mill);
                    // 获取当前时间的歌词信息
                    String text = i.substring(matcher.end());
                    map.put(time, text); // 添加到容器中
                    list.add(map);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * 将分,秒,毫秒转为秒
     *
     * @param min  分
     * @param sec  秒
     * @param mill 毫秒
     * @return 秒
     */
    private static int timeToSec(String min, String sec, String mill) {
        int m = Integer.parseInt(min);
        int s = Integer.parseInt(sec);
        int ms = Integer.parseInt(mill);
        if (s >= 60) {
            LogUtils.sendErrorMessage("警告: 出现了一个时间不正确的项 --> [" + min + ":" + sec + "." + mill.substring(0, 2) + "]");
        }
        int time = (m * 60 * 1000 + s * 1000 + ms) / 1000;
        return time;
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
        ArrayList<String> es = new ArrayList<String>();
        File f = new File(filePath);
        File[] fs = f.listFiles();
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isFile()) {
                es.add(fs[i].getName());
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
}
