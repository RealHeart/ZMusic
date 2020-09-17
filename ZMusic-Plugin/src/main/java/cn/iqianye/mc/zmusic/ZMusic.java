package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.utils.log.Log;
import cn.iqianye.mc.zmusic.utils.message.Message;
import cn.iqianye.mc.zmusic.utils.mod.Send;
import cn.iqianye.mc.zmusic.utils.music.Music;
import cn.iqianye.mc.zmusic.utils.player.Player;
import cn.iqianye.mc.zmusic.utils.runtask.RunTask;

import java.io.File;

public class ZMusic {
    public static boolean isBC;

    public static Log log;
    public static Message message;
    public static RunTask runTask;
    public static Music music;
    public static Send send;
    public static Player player;

    public static File dataFolder;
    public static String thisVer;
    public static int thisVerCode = 202009170;
    public static boolean isLatest = true;
    public static String updateLog;
    public static String latestVer;
    public static String downloadUrl;
    public static String updateUrl;
    public static boolean bilibiliIsVIP = false;
    public static boolean isViaVer = true;
    public static boolean isEnable = true;
}
