package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import org.bukkit.command.CommandSender;

public class HelpUtils {

    /**
     * 发送帮助
     *
     * @param cmdName 主命令名称
     * @param type    帮助类型
     * @param player  玩家
     */
    public static void sendHelp(String cmdName, String type, CommandSender player) {
        switch (type) {
            case "main":
                MessageUtils.sendNormalMessage("§6========= §r[§bZMusic§r] §d帮助 By 真心 §6=========", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " help - 查看主帮助(当前).", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " help play - 查看播放帮助.", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " help music - 查看点歌帮助.", player);
                if (player.hasPermission("zmusic.admin") || player.isOp()) {
                    MessageUtils.sendNormalMessage("/" + cmdName + " help admin - 查看管理员帮助.", player);
                }
                MessageUtils.sendNormalMessage("====================================================", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " stop - 停止播放", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " url [MP3地址] - 播放网络音乐", player);
                break;
            case "admin":
                MessageUtils.sendNormalMessage("§6========= §r[§bZMusic§r] §d管理员帮助 By 真心 §6=========", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " playAll [搜索源] [歌名] - 强制为所有玩家播放音乐.", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " stopAll - 强制为所有玩家停止播放音乐.", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " reload - 重载配置文件.", player);
                break;
            case "play":
                MessageUtils.sendNormalMessage("§6========= §r[§bZMusic§r] §d播放帮助 By 真心 §6=========", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " play qq <歌名> - QQ音乐播放§a.", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " play 163 <歌名> - 网易云音乐播放§a.", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " play kugou <歌名> - 酷狗音乐播放§a.", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " play kuwo <歌名> - 酷我音乐播放§a.", player);
                break;
            case "music":
                MessageUtils.sendNormalMessage("§6========= §r[§bZMusic§r] §d点歌帮助 By 真心 §6=========", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " music qq <歌名> - QQ音乐点歌§a.", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " music 163 <歌名> - 网易云音乐点歌§a.", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " music kugou <歌名> - 酷狗音乐点歌§a.", player);
                MessageUtils.sendNormalMessage("/" + cmdName + " play kuwo <歌名> - 酷我音乐播放§a.", player);
                break;
            case "url":
                MessageUtils.sendNormalMessage("/" + cmdName + " url <音乐直链> - 播放链接的音乐§a.", player);
                break;
            default:
                MessageUtils.sendNull(cmdName, player);
        }
    }
}