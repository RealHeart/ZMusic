package me.zhenxin.zmusic.utils;

import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.language.Lang;

public class HelpUtils {

    /**
     * 发送帮助
     *
     * @param type      帮助类型
     * @param playerObj 玩家
     */
    public static void sendHelp(String type, Object playerObj) {
        boolean isAdmin;
        if (ZMusic.player.isPlayer(playerObj))
            isAdmin = ZMusic.player.hasPermission(playerObj, "zmusic.admin");
        else isAdmin = true;
        switch (type) {
            case "main":
                ZMusic.message.sendNormalMessage("§6========= §r[§bZMusic§r] §d帮助 By 真心 §6=========", playerObj);
                for (String s : Lang.mainHelp) {
                    if (s.contains("[admin]")) {
                        ZMusic.message.sendNormalMessage(s.split("\\[admin]")[1], playerObj);
                    } else {
                        ZMusic.message.sendNormalMessage(s, playerObj);
                    }
                }
                break;
            case "admin":
                ZMusic.message.sendNormalMessage("§6========= §r[§bZMusic§r] §d管理员帮助 By 真心 §6=========", playerObj);
                ZMusic.message.sendNormalMessage("/zm playAll [搜索源] [歌名] - 强制为所有玩家播放音乐.", playerObj);
                ZMusic.message.sendNormalMessage("/zm stopAll - 强制为所有玩家停止播放音乐.", playerObj);
                ZMusic.message.sendNormalMessage("/zm update - 检查更新.", playerObj);
                ZMusic.message.sendNormalMessage("/zm login - 登录网易云音乐..", playerObj);
                ZMusic.message.sendNormalMessage("/zm reload - 重载配置文件.", playerObj);
                ZMusic.message.sendNormalMessage("§6=========================================", playerObj);
                break;
            case "play":
                ZMusic.message.sendNormalMessage("§6========= §r[§bZMusic§r] §d播放帮助 By 真心 §6=========", playerObj);
                ZMusic.message.sendNormalMessage("/zm play 163 <歌名> - 网易云音乐播放§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm play qq <歌名> - QQ音乐播放§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm play kugou <歌名> - 酷狗音乐播放§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm play kuwo <歌名> - 酷我音乐播放§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm play bilibili <:auXXXXX/歌名> - 哔哩哔哩音乐播放§a.", playerObj);
                ZMusic.message.sendNormalMessage("§6=========================================", playerObj);
                break;
            case "playlist":
                ZMusic.message.sendNormalMessage("§6========= §r[§bZMusic§r] §d歌单帮助 By 真心 §6=========", playerObj);
                ZMusic.message.sendNormalMessage("/zm playlist [qq/163/netease] import <歌单链接> - 导入歌单§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm playlist [qq/163/netease] list - 查看已导入的歌单列表§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm playlist [qq/163/netease] play <歌单ID> - 播放已导入的歌单§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm playlist [qq/163/netease] show <歌单ID> - 查看已导入的歌单§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm playlist [qq/163/netease] update - 更新已导入歌单.", playerObj);
                ZMusic.message.sendNormalMessage("/zm playlist type [normal/loop/random] - 设置歌单播放模式§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm playlist prev - 切换到下一首歌曲§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm playlist next - 切换到下一首歌曲§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm playlist jump [ID] - 跳转到指定歌曲§a.", playerObj);
                if (isAdmin) {
                    ZMusic.message.sendNormalMessage("§6=========================================", playerObj);
                    ZMusic.message.sendNormalMessage("/zm playlist global [qq/163/netease] import <歌单链接> - 导入全服歌单§a.", playerObj);
                    ZMusic.message.sendNormalMessage("/zm playlist global [qq/163/netease] list - 查看已导入的全服歌单列表§a.", playerObj);
                    ZMusic.message.sendNormalMessage("/zm playlist global [qq/163/netease] play <歌单ID> - 播放已导入的全服歌单§a.", playerObj);
                    ZMusic.message.sendNormalMessage("/zm playlist global [qq/163/netease] update - 更新已导入歌单.", playerObj);
                }
                ZMusic.message.sendNormalMessage("§6=========================================", playerObj);
                break;
            case "music":
                ZMusic.message.sendNormalMessage("§6========= §r[§bZMusic§r] §d点歌帮助 By 真心 §6=========", playerObj);
                ZMusic.message.sendNormalMessage("/zm music 163 <歌名> - 网易云音乐点歌§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm music qq <歌名> - QQ音乐点歌§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm music kugou <歌名> - 酷狗音乐点歌§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm music kuwo <歌名> - 酷我音乐点歌§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm music bilibili <:auXXXXX歌名> - 哔哩哔哩音乐点歌§a.", playerObj);
                ZMusic.message.sendNormalMessage("§6=========================================", playerObj);
                break;
            case "search":
                ZMusic.message.sendNormalMessage("§6========= §r[§bZMusic§r] §d搜索帮助 By 真心 §6=========", playerObj);
                ZMusic.message.sendNormalMessage("/zm search 163 <歌名> - 网易云音乐搜索§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm search qq <歌名> - QQ音乐搜索§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm search kugou <歌名> - 酷狗音乐搜索§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm search kuwo <歌名> - 酷我音乐搜索§a.", playerObj);
                ZMusic.message.sendNormalMessage("/zm search bilibili <:auXXXXX歌名> - 哔哩哔哩音乐搜索§a.", playerObj);
                ZMusic.message.sendNormalMessage("§6=========================================", playerObj);
                break;
            case "url":
                ZMusic.message.sendNormalMessage("/zm url <音乐直链> - 播放链接的音乐§a.", playerObj);
                break;
            default:
                ZMusic.message.sendNull(playerObj);
        }
    }
}