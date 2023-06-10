package me.zhenxin.zmusic.utils.mod;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.ZMusicBukkit;
import me.zhenxin.zmusic.api.Version;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

public class SendBukkit implements Send {
    private final Version version = new Version();

    @Override
    public void sendAM(Object playerObj, String data) {
        Player player = (Player) playerObj;
        if (player == null)
            return;
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            ZMusic.runTask.runAsync(() -> player.sendPluginMessage(ZMusicBukkit.plugin, "allmusic:channel", buf.array()));
            ZMusic.runTask.runAsync(() -> player.sendPluginMessage(ZMusicBukkit.plugin, "zmusic:channel", buf.array()));
        } catch (Exception e) {
            ZMusic.log.sendDebugMessage("[Mod通信] 数据发送发生错误");
        }
    }


    @Override
    public void sendABF(Object playerObj, String data) {
        if (!version.isHigherThan("1.12")) {
            Player player = (Player) playerObj;
            if (player == null)
                return;
            try {
                ZMusic.runTask.runAsync(() -> player.sendPluginMessage(ZMusicBukkit.plugin, "AudioBuffer", data.getBytes()));
            } catch (Exception e) {
                ZMusic.log.sendDebugMessage("[Mod通信] 数据发送发生错误");
            }
        }
    }
}
