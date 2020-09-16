package cn.iqianye.mc.zmusic.utils.music;

import cn.iqianye.mc.zmusic.ZMusic;

public class MusicBC implements Music {

    @Override
    public void play(String url, Object playerObj) {
        ZMusic.send.sendAM(playerObj, "[Play]" + url);
    }

    @Override
    public void stop(Object playerObj) {
        ZMusic.send.sendAM(playerObj, "[Stop]");
    }
}
