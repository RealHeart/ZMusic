package cn.iqianye.mc.zmusic.mod;

import cn.iqianye.mc.zmusic.Main;
import cn.iqianye.mc.zmusic.utils.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.charset.StandardCharsets;

public class Send {

    public static void sendAM(Player player, String data) {
        if (player == null)
            return;
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            new Thread(() -> {
                player.sendPluginMessage(JavaPlugin.getPlugin(Main.class), "allmusic:channel", buf.array());
            }).start();
        } catch (Exception e) {
            LogUtils.sendErrorMessage("数据发送发生错误");
            e.printStackTrace();
        }
    }

    public static void sendABF(Player player, String data) {
        if (player == null)
            return;
        try {
            new Thread(() -> {
                player.sendPluginMessage(JavaPlugin.getPlugin(Main.class), "AudioBuffer", data.getBytes());
            }).start();
        } catch (Exception e) {
            LogUtils.sendErrorMessage("数据发送发生错误");
            e.printStackTrace();
        }
    }
}
