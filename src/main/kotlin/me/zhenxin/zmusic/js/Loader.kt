package me.zhenxin.zmusic.js

import delight.nashornsandbox.NashornSandbox
import delight.nashornsandbox.NashornSandboxes

/**
 * JS模块加载器
 *
 * @author 真心
 * @since 2022/6/15 1:34
 */

val nashornSandbox: NashornSandbox by lazy {
    NashornSandboxes.create()
}

fun String.evalJS(): Any? {
    return nashornSandbox.eval(this)
}