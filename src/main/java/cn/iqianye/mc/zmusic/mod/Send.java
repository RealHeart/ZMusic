package cn.iqianye.mc.zmusic.mod;


public interface Send {

    void sendAM(Object playerObj, String data);

    void sendAM(Object playerObj, int infoX, int infoY, int lyricX, int lyricY);

    void sendABF(Object playerObj, String data);
}
