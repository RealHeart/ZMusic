package cn.iqianye.mc.zmusic.utils.music;

public interface Music {
    void play(String url, Object playerObj);

    void stop(Object playerObj);
}