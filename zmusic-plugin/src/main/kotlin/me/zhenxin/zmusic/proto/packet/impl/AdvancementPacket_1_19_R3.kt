package me.zhenxin.zmusic.proto.packet.impl

import com.google.gson.JsonObject
import me.zhenxin.zmusic.proto.packet.AdvancementPacket
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.advancements.*
import net.minecraft.advancements.critereon.LootSerializationContext
import net.minecraft.network.chat.IChatBaseComponent
import net.minecraft.network.protocol.game.PacketPlayOutAdvancements
import net.minecraft.resources.MinecraftKey
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


/**
 *
 *
 * @author 真心
 * @since 2022/6/9 12:01
 */
@Suppress("DuplicatedCode", "ClassName")
class AdvancementPacket_1_19_R3(
    private val player: Player,
    private val message: String
) : AdvancementPacket(player, message) {

    private val minecraftKey = MinecraftKey(namespaced, key)

    override fun sent(add: Boolean) {
        val advancements = mutableListOf<Advancement>()
        val removedAdvancements = mutableSetOf<MinecraftKey>()
        val progress = mutableMapOf<MinecraftKey, AdvancementProgress>()
        val criteria = mutableMapOf<String, Criterion>()
        if (add) {
            criteria["1"] = Criterion(object : CriterionInstance {

                override fun a(): MinecraftKey {
                    return MinecraftKey("", "")
                }

                override fun a(p0: LootSerializationContext?): JsonObject? {
                    return null
                }
            })
            val requirements = arrayOf(arrayOf("1"))

            val advancementDisplay = AdvancementDisplay(
                CraftItemStack.asNMSCopy(ItemStack(icon)),
                IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(TextComponent(message))),
                IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(TextComponent(desc))),
                null,
                AdvancementFrameType.a,
                true,
                false,
                true
            )

            val advancement = Advancement(
                minecraftKey,
                null,
                advancementDisplay,
                AdvancementRewards(0, arrayOfNulls(0), arrayOfNulls(0), null),
                criteria,
                requirements
            )
            val advancementProgress = AdvancementProgress()
            advancementProgress.a(criteria, requirements)
            val criterionProgress: CriterionProgress = advancementProgress.c("1")!!
            criterionProgress.b()
            progress[minecraftKey] = advancementProgress
            advancements.add(advancement)
        } else {
            removedAdvancements.add(minecraftKey)
        }

        val packet = PacketPlayOutAdvancements(false, advancements, removedAdvancements, progress)
        val craftPlayer = player as CraftPlayer
        craftPlayer.handle.b.a(packet)
    }
}