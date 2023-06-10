package me.zhenxin.zmusic.utils.runtask;

public interface RunTask {

    void run(Runnable runnable);

    void runAsync(Runnable runnable);
}
