package cn.iqianye.mc.zmusic.addon.proto.packet.impl;

import cn.iqianye.mc.zmusic.addon.proto.packet.AdvancementPacket;
import com.google.gson.JsonObject;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.LootSerializationContext;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutAdvancements;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author 真心
 * @since 2022/6/8 16:15
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class AdvancementPacket_1_18_R2 extends AdvancementPacket {
    MinecraftKey minecraftKey = new MinecraftKey(namespaced, key);

    public AdvancementPacket_1_18_R2(Player player, String message) {
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
            CriterionProgress criterionProgress = advancementProgress.c("1");
            criterionProgress.b();
            progress.put(minecraftKey, advancementProgress);
            advancements.add(advancement);
        } else {
            removedAdvancements.add(minecraftKey);
        }

        PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(false, advancements, removedAdvancements, progress);
        CraftPlayer player = (CraftPlayer) this.player;
        player.getHandle().b.a(packet);
    }
}
