package cn.iqianye.mc.zmusic.utils.music;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.api.NettyVersion;
import cn.iqianye.mc.zmusic.api.Version;
import org.bukkit.entity.Player;

public class MusicBukkit implements Music {

    static Version version = new Version();
    static NettyVersion nettyVersion = new NettyVersion();

    @Override
    public void play(String url, Object playerObj) {
        Player player = (Player) playerObj;
        if (version.isHigherThan("1.11")) {
            ZMusic.log.sendDebugMessage("[播放] 服务端版本高于1.11，使用AllMusic发送播放数据.");
            ZMusic.send.sendAM(playerObj, "[Play]" + url);
        } else {
            if (ZMusic.isViaVer) {
                ZMusic.log.sendDebugMessage("[播放] 服务端存在ViaVer，进入客户端Netty版本检测.");
                if (nettyVersion.isHigherThan(340, player)) {
                    ZMusic.log.sendDebugMessage("[播放] 客户端版本高于1.12(340)，使用AllMusic发送播放数据.");
                    ZMusic.send.sendAM(playerObj, "[Play]" + url);
                } else {
                    ZMusic.log.sendDebugMessage("[播放] 客户端版本低于1.12(340)，使用AudioBuffer发送播放数据.");
                    ZMusic.send.sendABF(playerObj, "[Net]" + url);
                }
            } else {
                ZMusic.log.sendDebugMessage("[播放] 服务端版本低于1.11，使用AudioBuffer发送播放数据.");
                ZMusic.send.sendABF(playerObj, "[Net]" + url);
            }
        }
    }

    @Override
    public void stop(Object playerObj) {
        Player player = (Player) playerObj;
        if (version.isHigherThan("1.11")) {
            ZMusic.send.sendAM(playerObj, "[Stop]");
        } else {
            if (ZMusic.isViaVer) {
                if (nettyVersion.isHigherThan(340, player)) {
                    ZMusic.send.sendAM(playerObj, "[Stop]");
                } else {
                    ZMusic.send.sendABF(playerObj, "[Stop]");
                }
            } else {
                ZMusic.send.sendABF(playerObj, "[Stop]");
            }
        }
    }
}
