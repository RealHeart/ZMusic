package cn.iqianye.mc.zmusic.mod;

import cn.iqianye.mc.zmusic.ZMusicBukkit;
import cn.iqianye.mc.zmusic.utils.LogUtils;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendPluginMessage(ZMusicBukkit.plugin, "allmusic:channel", buf.array());
                }
            }.runTaskAsynchronously(ZMusicBukkit.plugin);
        } catch (Exception e) {
            LogUtils.sendDebugMessage("[Mod通信] 数据发送发生错误");
        }
    }

    public static void sendAM(Player player, int infoX, int infoY, int lyricX, int lyricY) {
        JsonObject data = new JsonObject();
        data.addProperty("EnableLyric", true);
        data.addProperty("EnableInfo", true);
        JsonObject info = new JsonObject();
        info.addProperty("x", infoX);
        info.addProperty("y", infoY);
        JsonObject lyric = new JsonObject();
        lyric.addProperty("x", lyricX);
        lyric.addProperty("y", lyricY);
        data.add("Info", info);
        data.add("Lyric", lyric);
        sendAM(player, data.toString());
    }

    public static void sendABF(Player player, String data) {
        if (player == null)
            return;
        try {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendPluginMessage(ZMusicBukkit.plugin, "AudioBuffer", data.getBytes());
                }
            }.runTaskAsynchronously(ZMusicBukkit.plugin);
        } catch (Exception e) {
            LogUtils.sendDebugMessage("[Mod通信] 数据发送发生错误");
        }
    }
}
