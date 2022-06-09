package me.zhenxin.zmusic.proto.packet

import me.zhenxin.zmusic.platform.bukkitPlugin
import org.bukkit.Material
import org.bukkit.entity.Player
import kotlin.random.Random

/**
 *
 *
 * @author 真心
 * @since 2022/6/9 3:50
 */
abstract class AdvancementPacket(
    private val player: Player,
    private val message: String
) {
    protected val icon = randomIcon()

    protected val namespaced = "zmusic"
    protected val key = "music"

    protected val desc = "ZMusic Music Playing Toast"

    private fun randomIcon(): Material {
        val icons = mutableListOf<Material>()
        val pkgName = bukkitPlugin.server.javaClass.`package`.name
        val nms = pkgName.substring(pkgName.lastIndexOf('.') + 1)
        if (nms != "v1_12_R1") {
            icons.addAll(
                listOf(
                    Material.MUSIC_DISC_13,
                    Material.MUSIC_DISC_CAT,
                    Material.MUSIC_DISC_BLOCKS,
                    Material.MUSIC_DISC_CHIRP,
                    Material.MUSIC_DISC_FAR,
                    Material.MUSIC_DISC_MALL,
                    Material.MUSIC_DISC_MELLOHI,
                    Material.MUSIC_DISC_STAL,
                    Material.MUSIC_DISC_STRAD,
                    Material.MUSIC_DISC_WARD,
                    Material.MUSIC_DISC_WAIT
                )
            )
        } else {
            icons.addAll(
                listOf(
                    Material.matchMaterial("GOLD_RECORD")!!,
                    Material.matchMaterial("GREEN_RECORD")!!,
                    Material.matchMaterial("RECORD_3")!!,
                    Material.matchMaterial("RECORD_4")!!,
                    Material.matchMaterial("RECORD_5")!!,
                    Material.matchMaterial("RECORD_6")!!,
                    Material.matchMaterial("RECORD_7")!!,
                    Material.matchMaterial("RECORD_8")!!,
                    Material.matchMaterial("RECORD_9")!!,
                    Material.matchMaterial("RECORD_10")!!,
                    Material.matchMaterial("RECORD_12")!!
                )
            )
        }

        val i = Random.nextInt(icons.size)
        return icons[i]
    }

    fun grant() {
        sent(true)
    }

    fun revoke() {
        sent(false)
    }

    abstract protected fun sent(add: Boolean)
}