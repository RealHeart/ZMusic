package cn.iqianye.MinecraftPlugins.ZMusic.Music;

import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.KuGouMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.NeteaseCloudMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.QQMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Other.Var;
import cn.iqianye.MinecraftPlugins.ZMusic.Player.PlayerStatus;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.HelpUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.MessageUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.MusicUtils;
import cn.iqianye.MinecraftPlugins.ZMusic.Utils.OtherUtils;
import com.alibaba.fastjson.JSONObject;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayMusicAsync extends Thread {

    public CommandSender sender; // 发送者
    public Command cmd; // 命令
    public String[] args; // 参数
    public Server server; // 服务器
    public String type; // 类型

    @Override
    public void run() {
        if (sender instanceof Player) {
            if (args.length >= 2) {
                switch (args[1]) {
                    case "qq":
                        if (args.length >= 3) {
                            String qqMusic = OtherUtils.argsXin1(args);
                            String qqMusicUrl = QQMusic.getMusicUrl(qqMusic);
                            if (qqMusicUrl != null) {
                                switch (type) {
                                    case "all":
                                        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                        MusicUtils.stopAll(players); // 停止播放
                                        for (Player player : players) {
                                            PlayerStatus.setPlayerStatus(player, false);
                                        }
                                        MusicUtils.playAll(qqMusicUrl, players); // 播放搜索到的歌曲
                                        Bukkit.broadcastMessage(Var.prefix + "§a强制全部玩家播放§r[§e" + qqMusic + "§r]§a成功!");
                                        break;
                                    case "self":
                                        PlayerStatus.setPlayerStatus((Player) sender, false);
                                        MusicUtils.stopSelf((Player) sender); // 停止播放
                                        MusicUtils.playSelf(qqMusicUrl, (Player) sender); // 播放搜索到的歌曲
                                        MessageUtils.sendNormalMessage("播放§r[§e" + qqMusic + "§r]§a成功!", sender);
                                        ActionBarAPI.sendActionBar((Player) sender, "§b§l" + qqMusic);
                                        PlayerStatus.setPlayerStatus((Player) sender, true);
                                        break;
                                    case "music":
                                        TextComponent message = new TextComponent(Var.prefix + "§a玩家§d" + sender.getName() + "§a在QQ音乐点了一首§r[");
                                        TextComponent music = new TextComponent(qqMusic);
                                        music.setColor(ChatColor.YELLOW);
                                        music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm url " + qqMusicUrl));
                                        music.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放").create()));
                                        message.addExtra(music);
                                        message.addExtra("§r]§a点击歌名播放!");
                                        server.spigot().broadcast(message);
                                        break;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("搜索§r[§e" + qqMusic + "§r]§c失败，可能为以下问题.", sender);
                                MessageUtils.sendErrorMessage("1.搜索的音乐不存在或已下架", sender);
                                MessageUtils.sendErrorMessage("2.搜索的音乐为付费音乐", sender);
                                MessageUtils.sendErrorMessage("3.搜索的音乐为试听音乐", sender);
                                MessageUtils.sendErrorMessage("4.搜索的音乐无mp3音源", sender);
                                MessageUtils.sendErrorMessage("5.服务器网络异常", sender);
                            }

                        } else {
                            MessageUtils.sendNull(cmd.getName(), sender);
                        }

                        break;
                    case "163":
                        if (args.length >= 3) {
                            String netease = OtherUtils.argsXin1(args);
                            JSONObject neteaseJson = NeteaseCloudMusic.getMusicUrl(netease);
                            if (neteaseJson != null) {
                                String neteaseUrl = neteaseJson.getString("url");
                                String neteaseName = neteaseJson.getString("name") + "(" + neteaseJson.getString("artist") + ")";
                                switch (type) {
                                    case "all":
                                        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                        MusicUtils.stopAll(players); // 停止播放
                                        for (Player player : players) {
                                            PlayerStatus.setPlayerStatus(player, false);
                                        }
                                        MusicUtils.playAll(neteaseUrl, players); // 播放搜索到的歌曲
                                        Bukkit.broadcastMessage(Var.prefix + "§a强制全部玩家播放§r[§e" + neteaseName + "§r]§a成功!");
                                        for (Player player : players) {
                                            PlayerStatus.setPlayerStatus(player, true);
                                            sendLyric(neteaseJson.getString("lyric"), neteaseJson.getIntValue("duration"), player);
                                        }
                                        break;
                                    case "self":
                                        PlayerStatus.setPlayerStatus((Player) sender, false);
                                        MusicUtils.stopSelf((Player) sender); // 停止播放
                                        MusicUtils.playSelf(neteaseUrl, (Player) sender); // 播放搜索到的歌曲
                                        MessageUtils.sendNormalMessage("播放§r[§e" + neteaseName + "§r]§a成功!", sender);
                                        ActionBarAPI.sendActionBar((Player) sender, "§b§l" + neteaseName);
                                        PlayerStatus.setPlayerStatus((Player) sender, true);
                                        sendLyric(neteaseJson.getString("lyric"), neteaseJson.getIntValue("duration"), sender);
                                        break;
                                    case "music":
                                        TextComponent message = new TextComponent(Var.prefix + "§a玩家§d" + sender.getName() + "§a在网易云音乐点了一首§r[");
                                        TextComponent music = new TextComponent(neteaseName);
                                        music.setColor(ChatColor.YELLOW);
                                        music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm play 163 " + netease));
                                        music.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放").create()));
                                        message.addExtra(music);
                                        message.addExtra("§r]§a点击歌名播放!");
                                        server.spigot().broadcast(message);
                                        break;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("搜索§r[§e" + netease + "§r]§c失败，可能为以下问题.", sender);
                                MessageUtils.sendErrorMessage("1.搜索的音乐不存在或已下架", sender);
                                MessageUtils.sendErrorMessage("2.搜索的音乐为付费音乐", sender);
                                MessageUtils.sendErrorMessage("3.搜索的音乐为试听音乐", sender);
                                MessageUtils.sendErrorMessage("4.服务器网络异常", sender);
                            }
                        } else {
                            MessageUtils.sendNull(cmd.getName(), sender);
                        }
                        break;
                    case "kugou":
                        if (args.length >= 3) {
                            String kugouMusic = OtherUtils.argsXin1(args);
                            JSONObject kugouMusicJson = KuGouMusic.getMusicUrl(kugouMusic);
                            if (kugouMusicJson != null) {
                                String kugouMusicName = kugouMusicJson.getString("name") + "(" + kugouMusicJson.getString("singer") + ")";
                                String kugouMusicUrl = kugouMusicJson.getString("url");
                                switch (type) {
                                    case "all":
                                        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                        MusicUtils.stopAll(players); // 停止播放
                                        for (Player player : players) {
                                            PlayerStatus.setPlayerStatus(player, false);
                                        }
                                        MusicUtils.playAll(kugouMusicUrl, players); // 播放搜索到的歌曲
                                        Bukkit.broadcastMessage(Var.prefix + "§a强制全部玩家播放§r[§e" + kugouMusicName + "§r]§a成功!");
                                        break;
                                    case "self":
                                        MusicUtils.stopSelf((Player) sender); // 停止播放
                                        PlayerStatus.setPlayerStatus((Player) sender, false);
                                        MusicUtils.playSelf(kugouMusicUrl, (Player) sender); // 播放搜索到的歌曲
                                        MessageUtils.sendNormalMessage("播放§r[§e" + kugouMusicName + "§r]§a成功!", sender);
                                        ActionBarAPI.sendActionBar((Player) sender, "§b§l" + kugouMusicName);
                                        PlayerStatus.setPlayerStatus((Player) sender, true);
                                        break;
                                    case "music":
                                        TextComponent message = new TextComponent(Var.prefix + "§a玩家§d" + sender.getName() + "§a在酷狗音乐点了一首§r[");
                                        TextComponent music = new TextComponent(kugouMusicName);
                                        music.setColor(ChatColor.YELLOW);
                                        music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm url " + kugouMusicUrl));
                                        music.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放").create()));
                                        message.addExtra(music);
                                        message.addExtra("§r]§a点击歌名播放!");
                                        server.spigot().broadcast(message);
                                        break;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("搜索§r[§e" + kugouMusic + "§r]§c失败，可能为以下问题.", sender);
                                MessageUtils.sendErrorMessage("1.搜索的音乐不存在或已下架", sender);
                                MessageUtils.sendErrorMessage("2.搜索的音乐为付费音乐", sender);
                                MessageUtils.sendErrorMessage("3.搜索的音乐为试听音乐", sender);
                                MessageUtils.sendErrorMessage("4.服务器网络异常", sender);
                            }
                        } else {
                            MessageUtils.sendNull(cmd.getName(), sender);
                        }
                        break;
                    default:
                        sender.sendMessage(Var.prefix + "§c错误，未知的搜索源.");
                }
            } else {
                HelpUtils.sendHelp(cmd.getName(), "play", sender);
            }
        } else {
            MessageUtils.sendErrorMessage("命令只能由玩家使用!", sender);
        }
    }

    public static void sendLyric(String lyric, int maxTime, CommandSender sender) {
        String[] lyrics = lyric.split("\\n");
        List<Map<Long, String>> list = new ArrayList<Map<Long, String>>();
        String regex = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})\\]"; // 正则表达式
        Pattern pattern = Pattern.compile(regex); // 创建 Pattern 对象
        for (String i : lyrics) {
            Matcher matcher = pattern.matcher(i);
            while (matcher.find()) {
                // 用于存储当前时间和文字信息的容器
                Map<Long, String> map = new HashMap<Long, String>();
                // System.out.println(m.group(0)); // 例：[02:34.94]
                // [02:34.94] ----对应---> [分钟:秒.毫秒]
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
                long time = getLongTime(min, sec, mill);
                // 获取当前时间的歌词信息
                String text = i.substring(matcher.end());
                map.put(time, text); // 添加到容器中
                list.add(map);
            }
        }
        if (list != null || !list.isEmpty()) {
            LyircSendTimer lyircSendTimer = new LyircSendTimer();
            lyircSendTimer.list = list;
            lyircSendTimer.maxTime = maxTime / 1000;
            lyircSendTimer.player = (Player) sender;
            Timer timer = new Timer();
            timer.schedule(lyircSendTimer, 1000L, 1000L);
        } else {
            ActionBarAPI.sendActionBar((Player) sender, "§c§l没有任何歌词信息");
        }
    }

    private static long getLongTime(String min, String sec, String mill) {
        // 转成整型
        int m = Integer.parseInt(min);
        int s = Integer.parseInt(sec);
        int ms = Integer.parseInt(mill);

        if (s >= 60) {
            System.out.println("警告: 出现了一个时间不正确的项 --> [" + min + ":" + sec + "."
                    + mill.substring(0, 2) + "]");
        }
        // 组合成一个长整型表示的以毫秒为单位的时间
        long time = (m * 60 * 1000 + s * 1000 + ms) / 1000;
        return time;
    }
}
