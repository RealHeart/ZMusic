package me.zhenxin.zmusic.bridge.proto;

import me.zhenxin.zmusic.bridge.ZMusicBridge;
import me.zhenxin.zmusic.bridge.proto.packet.AdvancementPacket;
import me.zhenxin.zmusic.bridge.proto.packet.impl.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * 吐司工具
 *
 * @author 真心
 * @since 2022/6/8 14:15
 */
public class Toast {

    public static void sendToast(Object player, String title) {
        String packageName = ZMusicBridge.plugin.getServer().getClass().getPackage().getName();
        String nms = packageName.substring(packageName.lastIndexOf('.') + 1);
        Player bukkitPlayer = (Player) player;
        AdvancementPacket packet;
        switch (nms) {
            case "v1_19_R3":
                packet = new AdvancementPacket_1_19_R3(bukkitPlayer, title);
                break;
            case "v1_19_R2":
                packet = new AdvancementPacket_1_19_R2(bukkitPlayer, title);
                break;
            case "v1_19_R1":
                packet = new AdvancementPacket_1_19_R1(bukkitPlayer, title);
                break;
            case "v1_18_R2":
                packet = new AdvancementPacket_1_18_R2(bukkitPlayer, title);
                break;
            case "v1_18_R1":
                packet = new AdvancementPacket_1_18_R1(bukkitPlayer, title);
                break;
            case "v1_17_R1":
                packet = new AdvancementPacket_1_17_R1(bukkitPlayer, title);
                break;
            case "v1_16_R3":
                packet = new AdvancementPacket_1_16_R3(bukkitPlayer, title);
                break;
            case "v1_16_R2":
                packet = new AdvancementPacket_1_16_R2(bukkitPlayer, title);
                break;
            case "v1_16_R1":
                packet = new AdvancementPacket_1_16_R1(bukkitPlayer, title);
                break;
            case "v1_15_R1":
                packet = new AdvancementPacket_1_15_R1(bukkitPlayer, title);
                break;
            case "v1_14_R1":
                packet = new AdvancementPacket_1_14_R1(bukkitPlayer, title);
                break;
            case "v1_13_R2":
                packet = new AdvancementPacket_1_13_R2(bukkitPlayer, title);
                break;
            case "v1_12_R1":
                packet = new AdvancementPacket_1_12_R1(bukkitPlayer, title);
                break;
            default:
                ZMusicBridge.plugin.getLogger().warning(ChatColor.RED + "不支持的NMS版本: " + nms);
                return;
        }

        packet.grant();
        packet.revoke();
    }
}
