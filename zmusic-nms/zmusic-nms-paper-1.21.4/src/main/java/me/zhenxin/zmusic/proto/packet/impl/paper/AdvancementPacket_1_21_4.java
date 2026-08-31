package me.zhenxin.zmusic.proto.packet.impl.paper;

import io.papermc.paper.adventure.PaperAdventure;
import me.zhenxin.zmusic.proto.packet.AdvancementPacket;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.advancements.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Optional;

/**
 * @author Lumine1909
 * @since 2025/5/8 10:22
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class AdvancementPacket_1_21_4 extends AdvancementPacket {

    private static final AdvancementRequirements SIMPLE_REQUIREMENT = new AdvancementRequirements(Collections.singletonList(Collections.singletonList("1")));

    private final ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(namespaced, key);
    private final Component messageComponent;
    private final Component descriptionComponent;

    public AdvancementPacket_1_21_4(Player player, String message) {
        super(player, message);
        messageComponent = PaperAdventure.asVanilla(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
        descriptionComponent = PaperAdventure.asVanilla(LegacyComponentSerializer.legacyAmpersand().deserialize(desc));
    }

    @Override
    protected void sent(boolean add) {
        ClientboundUpdateAdvancementsPacket packet;

        if (add) {
            Advancement advancement = new Advancement(
                Optional.empty(),
                Optional.of(new DisplayInfo(
                    CraftItemStack.asNMSCopy(new ItemStack(icon)),
                    messageComponent,
                    descriptionComponent,
                    Optional.empty(),
                    AdvancementType.TASK,
                    true,
                    false,
                    true
                )),
                AdvancementRewards.EMPTY,
                Collections.emptyMap(),
                SIMPLE_REQUIREMENT,
                true
            );

            AdvancementProgress progress = new AdvancementProgress();
            progress.update(SIMPLE_REQUIREMENT);
            progress.grantProgress("1");
            packet = new ClientboundUpdateAdvancementsPacket(
                false,
                Collections.singleton(new AdvancementHolder(resourceLocation, advancement)),
                Collections.emptySet(),
                Collections.singletonMap(resourceLocation, progress)
            );
        } else {
            packet = new ClientboundUpdateAdvancementsPacket(
                false,
                Collections.emptySet(),
                Collections.singleton(resourceLocation),
                Collections.emptyMap()
            );
        }
        CraftPlayer player = (CraftPlayer) this.player;
        player.getHandle().connection.send(packet);
    }
}