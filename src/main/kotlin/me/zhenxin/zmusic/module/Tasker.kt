package me.zhenxin.zmusic.module

interface Tasker {
    fun async(runnable: Runnable)
}
