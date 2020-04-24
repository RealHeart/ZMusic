package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import cn.iqianye.MinecraftPlugins.ZMusic.Config.Config;
import cn.iqianye.MinecraftPlugins.ZMusic.Main;
import cn.iqianye.MinecraftPlugins.ZMusic.Other.Val;
import cn.iqianye.MinecraftPlugins.ZMusic.Player.PlayerStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.bossbar.BossBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        if (Config.supportBossBar) {
            BossBar bossBar = PlayerStatus.getPlayerBoosBar(player);
            if (bossBar != null) {
                bossBar.removePlayer(player);
            }
        }
        try {
            player.sendTitle("", "", 0, 0, 0);
        } catch (NoSuchMethodError e) {
            player.sendTitle("", "");
            LogUtils.sendErrorMessage(e.getMessage());
            LogUtils.sendErrorMessage("当前服务端不支持新版本Title发送方式，已使用旧的方式发送。");
        }
        PlayerStatus.setPlayerMusicName(player, null);
        PlayerStatus.setPlayerCurrentTime(player, null);
        PlayerStatus.setPlayerMaxTime(player, null);
        PlayerStatus.setPlayerLyric(player, null);
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
            String ver = JavaPlugin.getPlugin(Main.class).getServer().getVersion();
            if (Config.supportBossBar) {
                BossBar bossBar = PlayerStatus.getPlayerBoosBar(player);
                if (bossBar != null) {
                    bossBar.removePlayer(player);
                }
            }
            try {
                player.sendTitle("", "", 0, 0, 0);
            } catch (Exception e) {
                player.sendTitle("", "");
                LogUtils.sendErrorMessage(e.getMessage());
                LogUtils.sendErrorMessage("当前服务端不支持新版本Title发送方式，已使用旧的方式发送。");
            }
            PlayerStatus.setPlayerMusicName(player, null);
            PlayerStatus.setPlayerCurrentTime(player, null);
            PlayerStatus.setPlayerMaxTime(player, null);
            PlayerStatus.setPlayerLyric(player, null);
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


    /**
     * 从网易云获取歌词并格式化
     *
     * @param id 音乐id
     * @return 格式化后的List
     */
    public static List<Map<Integer, String>> getLyricFor163(String id) {
        Gson gson = new GsonBuilder().create();
        String lyricJsonText = NetUtils.getNetString("https://music.163.com/api/song/media?id=" + id, null);
        JsonObject lyricJson = gson.fromJson(lyricJsonText, JsonObject.class);
        String lyric = "";
        try {
            lyric = lyricJson.get("lyric").getAsString();
            lyric = lyric.replaceAll("\r", "");
        } catch (Exception e) {

        }
        return formatLyric(lyric);
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

}
