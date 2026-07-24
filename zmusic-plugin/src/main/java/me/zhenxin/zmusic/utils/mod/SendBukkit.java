package me.zhenxin.zmusic.utils.mod;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.ZMusicBukkit;
import me.zhenxin.zmusic.api.Version;
import me.zhenxin.zmusic.proto.NmsCustomPayload;
import me.zhenxin.zmusic.utils.runtask.BukkitTaskScheduler;
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
            byte[] array = buf.array();
            // 同一玩家队列内发送两个频道，保证 [Stop] 和 [Play] 的顺序。
            BukkitTaskScheduler.run(player, () -> {
                // 优先走 NMS 直发绕过服务端的通道注册检查，失败再回退到标准方式。
                if (!NmsCustomPayload.trySend(player, "allmusic:channel", array)) {
                    player.sendPluginMessage(ZMusicBukkit.plugin, "allmusic:channel", array);
                }
                if (!NmsCustomPayload.trySend(player, "zmusic:channel", array)) {
                    player.sendPluginMessage(ZMusicBukkit.plugin, "zmusic:channel", array);
                }
            });
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

    @Override
    public void sendToZMusicAddon(Object playerObj, String data) {
        // Bukkit 平台不需要实现 ZMusic Addon 通信
        // ZMusic Addon 是 BungeeCord 专用功能
        // Bukkit 可以通过其他方式传递数据，这里留空实现
    }
}
