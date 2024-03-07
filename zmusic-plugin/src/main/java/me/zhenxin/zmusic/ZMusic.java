package me.zhenxin.zmusic;

import me.zhenxin.zmusic.config.LoadConfig;
import me.zhenxin.zmusic.data.PlayerData;
import me.zhenxin.zmusic.language.LoadLang;
import me.zhenxin.zmusic.login.NeteaseLogin;
import me.zhenxin.zmusic.music.PlayListPlayer;
import me.zhenxin.zmusic.utils.OtherUtils;
import me.zhenxin.zmusic.utils.log.Log;
import me.zhenxin.zmusic.utils.message.Message;
import me.zhenxin.zmusic.utils.mod.Send;
import me.zhenxin.zmusic.utils.music.Music;
import me.zhenxin.zmusic.utils.player.Player;
import me.zhenxin.zmusic.utils.runtask.RunTask;

import java.io.File;
import java.util.List;

public final class ZMusic {
    public static boolean isBC;

    public static Log log;
    public static Message message;
    public static RunTask runTask;
    public static Music music;
    public static Send send;
    public static Player player;

    public static File dataFolder;
    public static String thisVer;
    public static int thisVerCode = 202403070;
    public static boolean isVip = false;
    public static boolean isViaVer = true;
    public static boolean isEnable = true;
    public static boolean isEnableEd = false;

    public static void disable() {
        ZMusic.log.sendNormalMessage("正在卸载中....");
        List<Object> players = ZMusic.player.getOnlinePlayerList();
        if (!players.isEmpty()) {
            for (Object player : players) {
                OtherUtils.resetPlayerStatus(player);
                PlayListPlayer plp = PlayerData.getPlayerPlayListPlayer(player);
                if (plp != null) {
                    plp.isStop = true;
                    PlayerData.setPlayerPlayListPlayer(player, null);
                    OtherUtils.resetPlayerStatus(player);
                }
            }
        }
        ZMusic.log.sendNormalMessage("插件作者: 真心");
        ZMusic.log.sendNormalMessage("主页：zhenxin.me");
        ZMusic.log.sendNormalMessage("QQ：1307993674");
        ZMusic.log.sendNormalMessage("插件交流群：1032722724");
        ZMusic.log.sendNormalMessage("插件已卸载完成!");
    }

    public static void loadEnd(Object sender) {
        new LoadConfig().load();
        ZMusic.log.sendNormalMessage("成功加载配置文件!");
        ZMusic.runTask.runAsync(() -> {
            OtherUtils.checkUpdate(sender, false);
            new LoadLang().load();
            if (NeteaseLogin.isLogin()) {
                NeteaseLogin.refresh();
            } else {
                NeteaseLogin.anonymous();
            }
            ZMusic.log.sendNormalMessage("插件作者: 真心");
            ZMusic.log.sendNormalMessage("主页：zhenxin.me");
            ZMusic.log.sendNormalMessage("QQ：1307993674");
            ZMusic.log.sendNormalMessage("插件交流群：1032722724");
            ZMusic.log.sendNormalMessage("插件已加载完成!");
            isEnableEd = true;

            ZMusic.log.sendDebugMessage(System.getProperty("file.encoding"));
        });
    }
}
