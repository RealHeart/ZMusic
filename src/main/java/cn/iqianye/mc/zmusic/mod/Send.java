package cn.iqianye.mc.zmusic.mod;


import cn.iqianye.mc.zmusic.ZMusic;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.nio.charset.StandardCharsets;

public interface Send {

    void sendAM(Object playerObj, String data);

    void sendAM(Object playerObj, int infoX, int infoY, int lyricX, int lyricY);

    void sendABF(Object playerObj, String data);

    default void sendZMPapi(Object playerObj, String data) {
        ProxiedPlayer player = (ProxiedPlayer) playerObj;
        if (player == null)
            return;
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            ZMusic.runTask.start(() -> player.getServer().getInfo().sendData("zmusic:channel", buf.array()));
        } catch (
                Exception e) {
            ZMusic.log.sendDebugMessage("[Mod通信] 数据发送发生错误");
        }
    }
}
