package cn.iqianye.mc.zmusic.api;

import org.bukkit.Bukkit;


public class Version {

    public String getRawVersion(){
        return Bukkit.getBukkitVersion();
    }

    public String getVersion(){
        String[] rawver = getRawVersion().split("-");
        return rawver[0];
    }

    //比目标版本低
    public boolean isLowerThan(String ver){
        String[] version = getVersion().split("\\.",3);
        String mid = version[1];
        String[] testmid = ver.split("\\.");
        if(Integer.parseInt(testmid[1])>Integer.parseInt(mid)){
            return true;
        }else return false;
    }

    //比目标版本高
    public boolean isHigherThan(String ver){
        String[] version = getVersion().split("\\.");
        String mid = version[1];
        String[] testmid = ver.split("\\.");
        if(Integer.parseInt(testmid[1])<Integer.parseInt(mid)){
            return true;
        }else return false;
    }

    //与目标版本相等
    public boolean isEquals(String ver){
        String[] version = getVersion().split("\\.");
        String mid = version[1];
        String[] testmid = ver.split("\\.");
        if(Integer.parseInt(testmid[1])==Integer.parseInt(mid)){
            return true;
        }else return false;
    }
}
