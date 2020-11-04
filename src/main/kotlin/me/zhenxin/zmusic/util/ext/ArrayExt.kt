package me.zhenxin.zmusic.util.ext

object ArrayExt {

    fun Array<String>.xin1(start: Int = 0): String {
        var r = ""
        this.forEachIndexed { i, s ->
            if (i > start) {
                r += "$s "
            }
        }
        return r.trim()
    }
}
