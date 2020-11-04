package me.zhenxin.zmusic.module

internal interface Tasker {
    fun async(runnable: Runnable)
}
