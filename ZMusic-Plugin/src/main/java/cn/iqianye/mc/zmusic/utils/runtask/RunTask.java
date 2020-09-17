package cn.iqianye.mc.zmusic.utils.runtask;

public interface RunTask {

    void run(Runnable runnable);

    void runAsync(Runnable runnable);
}
