package me.zhenxin.zmusic.bridge.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.zhenxin.zmusic.bridge.ZMusicBridge;
import me.zhenxin.zmusic.bridge.data.PlayerData;
import me.zhenxin.zmusic.bridge.entity.Message;
import me.zhenxin.zmusic.bridge.entity.MusicInfo;
import me.zhenxin.zmusic.bridge.proto.Toast;
import me.zhenxin.zmusic.bridge.utils.StringUtil;
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
        if (ZMusicBridge.DEBUG) {
            ZMusicBridge.logger.warning("[PluginMessage] " + message);
        }
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
                    if (!StringUtil.isNullOrEmpty(info.getName())) {
                        PlayerData.setName(player, info.getName());
                    }
                    if (!StringUtil.isNullOrEmpty(info.getSinger())) {
                        PlayerData.setSinger(player, info.getSinger());
                    }
                    if (!StringUtil.isNullOrEmpty(info.getLyric())) {
                        PlayerData.setLyric(player, info.getLyric());
                    }
                    if (info.getCurrentTime() != null) {
                        PlayerData.setCurrentTime(player, info.getCurrentTime());
                    }
                    if (info.getMaxTime() != null) {
                        PlayerData.setMaxTime(player, info.getMaxTime());
                    }
                    break;
                case RESET:
                    PlayerData.resetData(player);
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
