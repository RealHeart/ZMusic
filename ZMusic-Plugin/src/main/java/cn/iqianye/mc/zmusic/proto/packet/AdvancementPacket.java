package cn.iqianye.mc.zmusic.proto.packet;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.ZMusicBukkit;
import cn.iqianye.mc.zmusic.config.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * @author 真心
 * @since 2022/6/8 16:14
 */
public abstract class AdvancementPacket {
    protected Player player;
    protected Material icon;
    protected String message;

    protected String namespaced = "zmusic";
    protected String key = "music";

    protected String desc = "ZMusic Music Playing Toast";

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public AdvancementPacket(Player player, String message) {
        this.player = player;
        this.icon = getIcon();
        if (Config.debug) {
            ZMusic.log.sendDebugMessage("[进度] 随机图标: " + icon);
        }
        this.message = message;
    }

    public void grant() {
        sent(true);
    }

    public void revoke() {
        sent(false);
    }

    abstract protected void sent(boolean add);

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    private Material getIcon() {
        Material[] icons;
        String packageName = ZMusicBukkit.plugin.getServer().getClass().getPackage().getName();
        String nms = packageName.substring(packageName.lastIndexOf('.') + 1);
        if (!"v1_12_R1".equals(nms)) {
            icons = new Material[]{
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
            };
        } else {
            icons = new Material[]{
                    Material.matchMaterial("GOLD_RECORD"),
                    Material.matchMaterial("GREEN_RECORD"),
                    Material.matchMaterial("RECORD_3"),
                    Material.matchMaterial("RECORD_4"),
                    Material.matchMaterial("RECORD_5"),
                    Material.matchMaterial("RECORD_6"),
                    Material.matchMaterial("RECORD_7"),
                    Material.matchMaterial("RECORD_8"),
                    Material.matchMaterial("RECORD_9"),
                    Material.matchMaterial("RECORD_10"),
                    Material.matchMaterial("RECORD_12")
            };
        }

        Random random = new Random();
        int i = random.nextInt(icons.length);
        return icons[i];
    }
}
