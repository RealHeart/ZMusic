package cn.iqianye.mc.zmusic.api;

import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;


public class NettyVersion {

    // NettyVersion List
    // https://wiki.vg/Protocol_version_numbers
    public int getVersion(Player player) {
        ViaAPI viaAPI = Via.getAPI();
        return viaAPI.getPlayerVersion(player);
    }

    //比目标版本低
    public boolean isLowerThan(int ver, Player player) {
        int version = getVersion(player);
        if (ver >= version) {
            return true;
        } else return false;
    }

    //比目标版本高
    public boolean isHigherThan(int ver, Player player) {
        int version = getVersion(player);
        if (ver <= version) {
            return true;
        } else return false;
    }

    //与目标版本相等
    public boolean isEquals(int ver, Player player) {
        int version = getVersion(player);
        if (ver == version) {
            return true;
        } else return false;
    }
}
