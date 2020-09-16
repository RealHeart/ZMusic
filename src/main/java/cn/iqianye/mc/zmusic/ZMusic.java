package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.mod.Send;
import cn.iqianye.mc.zmusic.utils.log.Log;
import cn.iqianye.mc.zmusic.utils.message.Message;
import cn.iqianye.mc.zmusic.utils.music.Music;
import cn.iqianye.mc.zmusic.utils.player.Player;
import cn.iqianye.mc.zmusic.utils.runnable.RunTask;
import cn.iqianye.mc.zmusic.utils.server.Server;

import java.io.File;

public class ZMusic {
    public static boolean isBC;

    public static Log log;
    public static Message message;
    public static RunTask runTask;
    public static Music music;
    public static Send send;
    public static Player player;
    public static Server server;

    public static File dataFolder;
}
