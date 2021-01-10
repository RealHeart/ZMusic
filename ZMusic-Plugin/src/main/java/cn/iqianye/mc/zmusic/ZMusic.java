package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.config.LoadConfig;
import cn.iqianye.mc.zmusic.data.PlayerData;
import cn.iqianye.mc.zmusic.language.LoadLang;
import cn.iqianye.mc.zmusic.music.PlayListPlayer;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import cn.iqianye.mc.zmusic.utils.log.Log;
import cn.iqianye.mc.zmusic.utils.message.Message;
import cn.iqianye.mc.zmusic.utils.mod.Send;
import cn.iqianye.mc.zmusic.utils.music.Music;
import cn.iqianye.mc.zmusic.utils.player.Player;
import cn.iqianye.mc.zmusic.utils.runtask.RunTask;

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
    public static int thisVerCode = 202101100;
    public static boolean bilibiliIsVIP = false;
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
        ZMusic.log.sendNormalMessage("博客：www.zhenxin.xyz");
        ZMusic.log.sendNormalMessage("QQ：1307993674");
        ZMusic.log.sendNormalMessage("插件交流群：1032722724");
        ZMusic.log.sendNormalMessage("插件已卸载完成!");
    }

    public static void loadEnd(Object sender) {
        new LoadConfig().load();
        ZMusic.log.sendNormalMessage("成功加载配置文件!");
        ZMusic.runTask.runAsync(() -> {
            OtherUtils.loginNetease(sender, false);
            OtherUtils.checkUpdate(sender, false);
            new LoadLang().load();
            ZMusic.log.sendNormalMessage("插件作者: 真心");
            ZMusic.log.sendNormalMessage("博客：www.zhenxin.xyz");
            ZMusic.log.sendNormalMessage("QQ：1307993674");
            ZMusic.log.sendNormalMessage("插件交流群：1032722724");
            ZMusic.log.sendNormalMessage("插件已加载完成!");
            isEnableEd = true;
        });
    }
}
