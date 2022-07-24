package me.zhenxin.zmusic.bridge.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.zhenxin.zmusic.bridge.data.PlayerData;
import me.zhenxin.zmusic.bridge.entity.Message;
import me.zhenxin.zmusic.bridge.entity.MusicInfo;
import me.zhenxin.zmusic.bridge.proto.Toast;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;

/**
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 20202/07/24 12:43
 */
@SuppressWarnings("NullableProblems")
public class PluginMessage implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        bytes[0] = 0;
        String message = new String(bytes, StandardCharsets.UTF_8).substring(1);
        Gson gson = new GsonBuilder().create();
        try {
            Message data = gson.fromJson(message, Message.class);
            switch (data.getType()) {
                case TOAST:
                    String title = data.getTitle();
                    Toast.sendToast(player, title);
                    break;
                case INFO:
                    MusicInfo info = data.getInfo();
                    if (info.getName() != null) {
                        PlayerData.setName(player, info.getName());
                    }
                    if (info.getSinger() != null) {
                        PlayerData.setSinger(player, info.getSinger());
                    }
                    if (info.getLyric() != null) {
                        PlayerData.setName(player, info.getLyric());
                    }
                    if (info.getCurrentTime() != null) {
                        PlayerData.setCurrentTime(player, info.getCurrentTime());
                    }
                    if (info.getMaxTime() != null) {
                        PlayerData.setMaxTime(player, info.getMaxTime());
                    }
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
