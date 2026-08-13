package me.zhenxin.zmusic.proto;

import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.ZMusicBukkit;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.proto.packet.AdvancementPacket;
import me.zhenxin.zmusic.proto.packet.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

/**
 * 吐司工具
 *
 * @author 真心
 * @since 2022/6/8 14:15
 */
public class Toast {

    public static void sendToast(Object player, String title) {
        try {
            String packageName = ZMusicBukkit.plugin.getServer().getClass().getPackage().getName();
            String nms = packageName.substring(packageName.lastIndexOf('.') + 1);
            Player bukkitPlayer = (Player) player;
            AdvancementPacket packet;
            ZMusic.log.sendDebugMessage("当前NMS版本: " + nms);
            switch (nms) {
                case "v1_20_R1":
                    packet = new AdvancementPacket_1_20_R1(bukkitPlayer, title);
                    break;
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
                case "craftbukkit": // Mojang mappings server for paper 1.20.5+
                    String version;
                    if (Bukkit.getUnsafe().getDataVersion() >= 4786) {
                        version = "26_2";
                    } else if (Bukkit.getUnsafe().getDataVersion() >= 4440) {
                        version = "1_21_11";
                    } else if (Bukkit.getUnsafe().getDataVersion() >= 4435) {
                        version = "1_21_8";
                    } else {
                        version = "1_21_4";
                    }
                    Class<?> clazz = Class.forName("me.zhenxin.zmusic.proto.packet.impl.paper.AdvancementPacket_" + version);
                    Constructor<?> constructor = clazz.getConstructor(Player.class, String.class);
                    packet = (AdvancementPacket) constructor.newInstance(bukkitPlayer, title);
                    break;
                default:
                    ZMusic.log.sendErrorMessage("不支持的NMS版本: " + nms);
                    return;
            }

            packet.grant();
            packet.revoke();
        } catch (Throwable t) {
            if (Config.debug) {
                t.printStackTrace();
            }
            ZMusic.log.sendDebugMessage("当前服务端不支持Toast功能.");
        }
    }
}
