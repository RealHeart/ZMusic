package cn.iqianye.mc.zmusic.utils.music;

import cn.iqianye.mc.zmusic.ZMusic;
import cn.iqianye.mc.zmusic.api.Version;

public class Music {

    private final Version version = new Version();

    public void play(String url, Object playerObj) {
        ZMusic.send.sendAM(playerObj, "[Play]" + url);
        if (!version.isHigherThan("1.12")) {
            ZMusic.send.sendABF(playerObj, "[Net]" + url);
        }
    }

    public void stop(Object playerObj) {
        ZMusic.send.sendAM(playerObj, "[Stop]");
        if (!version.isHigherThan("1.12")) {
            ZMusic.send.sendABF(playerObj, "[Stop]");
        }

    }
}