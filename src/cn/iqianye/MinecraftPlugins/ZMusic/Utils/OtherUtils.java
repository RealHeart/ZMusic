package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.KuGouMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.NeteaseCloudMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource.QQMusic;
import cn.iqianye.MinecraftPlugins.ZMusic.Other.Var;
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

import java.util.ArrayList;
import java.util.List;

public class OtherUtils {

    /**
     * 参数合一
     *
     * @param args 参数
     * @return 合并的值
     */
    public static String argsXin1(String[] args) {
        String s = new String();
        for (int i = 0; i < args.length; i++) {
            if (i != 0 & i != 1) {
                if (i != args.length) {
                    s += args[i] + " ";
                } else {
                    s += args[i];
                }
            }
        }
        return s;
    }

    /**
     * 播放音乐
     *
     * @param sender 发送者
     * @param cmd    命令
     * @param args   参数
     * @param server 服务器
     * @param type   类型(点歌、个人或全体)
     */
    public static void playMusic(CommandSender sender, Command cmd, String[] args, Server server, String type) {
        if (sender instanceof Player) {
            if (args.length >= 2) {
                switch (args[1]) {
                    case "qq":
                        if (args.length >= 3) {
                            MessageUtils.sendErrorMessage("QQ音乐搜索音乐有问题，暂时不开放。",sender);
                            /*
                            String qqMusic = OtherUtils.argsXin1(args);
                            String qqMusicUrl = QQMusic.getMusicUrl(qqMusic);
                            if (qqMusicUrl != null) {
                                switch (type) {
                                    case "all":
                                        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                        MusicUtils.stopAll(players); // 停止播放
                                        MusicUtils.playAll(qqMusicUrl, players); // 播放搜索到的歌曲
                                        Bukkit.broadcastMessage("播放§r[§e" + qqMusic + "§r]§a成功!");
                                        break;
                                    case "self":
                                        MusicUtils.stopSelf((Player) sender); // 停止播放
                                        MusicUtils.playSelf(qqMusicUrl, (Player) sender); // 播放搜索到的歌曲
                                        MessageUtils.sendNormalMessage("播放§r[§e" + qqMusic + "§r]§a成功!", sender);
                                        ActionBarAPI.sendActionBar((Player) sender, "§b§l" + qqMusic);
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
                                MessageUtils.sendErrorMessage("搜索失败，请检查名称是否正确!", sender);
                            }
                            */
                        } else {
                            MessageUtils.sendNull(cmd.getName(), sender);
                        }

                        break;
                    case "netease":
                        if (args.length >= 3) {
                            String netease = OtherUtils.argsXin1(args);
                            String neteaseUrl = NeteaseCloudMusic.getMusicUrl(netease);
                            if (neteaseUrl != null) {
                                switch (type) {
                                    case "all":
                                        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                        MusicUtils.stopAll(players); // 停止播放
                                        MusicUtils.playAll(neteaseUrl, players); // 播放搜索到的歌曲
                                        Bukkit.broadcastMessage("播放§r[§e" + netease + "§r]§a成功!");
                                        break;
                                    case "self":
                                        MusicUtils.stopSelf((Player) sender); // 停止播放
                                        MusicUtils.playSelf(neteaseUrl, (Player) sender); // 播放搜索到的歌曲
                                        MessageUtils.sendNormalMessage("播放§r[§e" + netease + "§r]§a成功!", sender);
                                        ActionBarAPI.sendActionBar((Player) sender, "§b§l" + netease);
                                        break;
                                    case "music":
                                        TextComponent message = new TextComponent(Var.prefix + "§a玩家§d" + sender.getName() + "§a在网易云音乐点了一首§r[");
                                        TextComponent music = new TextComponent(netease);
                                        music.setColor(ChatColor.YELLOW);
                                        music.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zm url " + neteaseUrl));
                                        music.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b点击播放").create()));
                                        message.addExtra(music);
                                        message.addExtra("§r]§a点击歌名播放!");
                                        server.spigot().broadcast(message);
                                        break;
                                }
                            } else {
                                MessageUtils.sendErrorMessage("搜索失败，请检查名称是否正确!", sender);
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
                                String kugouMusicName = kugouMusicJson.getString("singer") + " - " + kugouMusicJson.getString("name");
                                String kugouMusicUrl = kugouMusicJson.getString("url");
                                switch (type) {
                                    case "all":
                                        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                                        MusicUtils.stopAll(players); // 停止播放
                                        MusicUtils.playAll(kugouMusicUrl, players); // 播放搜索到的歌曲
                                        Bukkit.broadcastMessage("播放§r[§e" + kugouMusicName + "§r]§a成功!");
                                        break;
                                    case "self":
                                        MusicUtils.stopSelf((Player) sender); // 停止播放
                                        MusicUtils.playSelf(kugouMusicUrl, (Player) sender); // 播放搜索到的歌曲
                                        MessageUtils.sendNormalMessage("播放§r[§e" + kugouMusicName + "§r]§a成功!", sender);
                                        ActionBarAPI.sendActionBar((Player) sender, "§b§l" + kugouMusicName);
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
                                MessageUtils.sendErrorMessage("搜索失败，请检查名称是否正确!", sender);
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
}
