package cn.iqianye.mc.zmusic.papi.api;

import org.bukkit.Bukkit;

public class Version {

    public String getRawVersion() {
        return Bukkit.getBukkitVersion();
    }

    public String getVersion() {
        return getRawVersion().split("-")[0];
    }

    //比目标版本低
    public boolean isLowerThan(String ver) {
        String[] versions = getVersion().split("\\.");
        String[] vers = ver.split("\\.");
        for (int i = 0; i <= (vers.length - 1); i++) {
            if (Integer.parseInt(vers[i]) > Integer.parseInt(versions[i])) {
                return true;
            }
        }
        return false;
    }

    //比目标版本高
    public boolean isHigherThan(String ver) {
        String[] versions = getVersion().split("\\.");
        String[] vers = ver.split("\\.");
        for (int i = 0; i <= (vers.length - 1); i++) {
            if (Integer.parseInt(vers[i]) < Integer.parseInt(versions[i])) {
                return true;
            }
        }
        return false;
    }

    //与目标版本相等
    public boolean isEquals(String ver) {
        return getVersion().equals(ver);
    }
}
