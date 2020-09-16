package cn.iqianye.mc.zmusic.mod;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.ZMusicBukkit;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

public class SendBukkit implements Send {

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
            ZMusic.runTask.start(() -> player.sendPluginMessage(ZMusicBukkit.plugin, "allmusic:channel", buf.array()));
        } catch (Exception e) {
            ZMusic.log.sendDebugMessage("[Mod通信] 数据发送发生错误");
        }
    }

    @Override
    public void sendAM(Object playerObj, int infoX, int infoY, int lyricX, int lyricY) {
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
        sendAM(playerObj, data.toString());
    }

    @Override
    public void sendABF(Object playerObj, String data) {
        Player player = (Player) playerObj;
        if (player == null)
            return;
        try {
            ZMusic.runTask.start(() -> player.sendPluginMessage(ZMusicBukkit.plugin, "AudioBuffer", data.getBytes()));
        } catch (Exception e) {
            ZMusic.log.sendDebugMessage("[Mod通信] 数据发送发生错误");
        }
    }
}
