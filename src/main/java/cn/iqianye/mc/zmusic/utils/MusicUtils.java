package cn.iqianye.mc.zmusic.utils;

import cn.iqianye.mc.zmusic.api.NettyVersion;
import cn.iqianye.mc.zmusic.api.Version;
import cn.iqianye.mc.zmusic.mod.Send;
import cn.iqianye.mc.zmusic.other.Val;
import org.bukkit.entity.Player;

public class MusicUtils {

    static Version version = new Version();
    static NettyVersion nettyVersion = new NettyVersion();

    /**
     * 播放音乐(个人)
     *
     * @param url    音乐地址
     * @param player 玩家
     */
    public static void play(String url, Player player) {
        if (version.isHigherThan("1.11")) {
            LogUtils.sendDebugMessage("[播放] 服务端版本高于1.11，使用AllMusic发送播放数据.");
            Send.sendAM(player, "[Play]" + url);
        } else {
            if (Val.isViaVer) {
                LogUtils.sendDebugMessage("[播放] 服务端存在ViaVer，进入客户端Netty版本检测.");
                if (nettyVersion.isHigherThan(340, player)) {
                    LogUtils.sendDebugMessage("[播放] 客户端版本高于1.12(340)，使用AllMusic发送播放数据.");
                    Send.sendAM(player, "[Play]" + url);
                } else {
                    LogUtils.sendDebugMessage("[播放] 客户端版本低于1.12(340)，使用AudioBuffer发送播放数据.");
                    Send.sendABF(player, "[Net]" + url);
                }
            } else {
                LogUtils.sendDebugMessage("[播放] 服务端版本低于1.11，使用AudioBuffer发送播放数据.");
                Send.sendABF(player, "[Net]" + url);
            }
        }
    }

    /**
     * 停止播放音乐(个人)
     *
     * @param player 玩家
     */
    public static void stop(Player player) {
        if (version.isHigherThan("1.11")) {
            Send.sendAM(player, "[Stop]");
        } else {
            if (Val.isViaVer) {
                if (nettyVersion.isHigherThan(340, player)) {
                    Send.sendAM(player, "[Stop]");
                } else {
                    Send.sendABF(player, "[Stop]");
                }
            } else {
                Send.sendABF(player, "[Stop]");
            }
        }
    }
}
