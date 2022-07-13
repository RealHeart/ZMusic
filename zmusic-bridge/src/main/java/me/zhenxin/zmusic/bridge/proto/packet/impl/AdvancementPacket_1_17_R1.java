package me.zhenxin.zmusic.bridge.proto.packet.impl;

import com.google.gson.JsonObject;
import me.zhenxin.zmusic.bridge.proto.packet.AdvancementPacket;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.LootSerializationContext;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutAdvancements;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author 真心
 * @since 2022/6/8 16:15
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class AdvancementPacket_1_17_R1 extends AdvancementPacket {
    MinecraftKey minecraftKey = new MinecraftKey(namespaced, key);

    public AdvancementPacket_1_17_R1(Player player, String message) {
        super(player, message);
    }

    @Override
    protected void sent(boolean add) {
        List<Advancement> advancements = new ArrayList<>();
        Set<MinecraftKey> removedAdvancements = new HashSet<>();
        Map<MinecraftKey, AdvancementProgress> progress = new HashMap<>();

        if (add) {
            Map<String, Criterion> criteria = new HashMap<>();
            criteria.put("1", new Criterion(new CriterionInstance() {
                @Override
                public MinecraftKey a() {
                    return new MinecraftKey("", "");
                }

                @Override
                public JsonObject a(LootSerializationContext lootSerializationContext) {
                    return null;
                }
            }));

            ArrayList<String[]> fixedRequirements = new ArrayList<>();
            for (String name : criteria.keySet()) {
                fixedRequirements.add(new String[]{name});
            }
            String[][] requirements = Arrays.stream(fixedRequirements.toArray()).toArray(String[][]::new);

            AdvancementDisplay advancementDisplay = new AdvancementDisplay(
                    CraftItemStack.asNMSCopy(new ItemStack(icon)),
                    new ChatMessage(message),
                    new ChatMessage(desc),
                    null,
                    AdvancementFrameType.a,
                    true,
                    false,
                    true);
            Advancement advancement = new Advancement(
                    minecraftKey,
                    null,
                    advancementDisplay,
                    new AdvancementRewards(0, new MinecraftKey[0], new MinecraftKey[0], null),
                    criteria,
                    requirements);
            AdvancementProgress advancementProgress = new AdvancementProgress();
            advancementProgress.a(criteria, requirements);
            CriterionProgress criterionProgress = null;
            try {
                criterionProgress = (CriterionProgress) advancementProgress
                        .getClass()
                        .getDeclaredMethod("getCriterionProgress", String.class)
                        .invoke(advancementProgress, "1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            criterionProgress.b();
            progress.put(minecraftKey, advancementProgress);
            advancements.add(advancement);
        } else {
            removedAdvancements.add(minecraftKey);
        }

        PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(false, advancements, removedAdvancements, progress);
        CraftPlayer player = (CraftPlayer) this.player;
        try {

            PlayerConnection connection = player.getHandle().b;
            connection.getClass()
                    .getDeclaredMethod("sendPacket", Packet.class)
                    .invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
