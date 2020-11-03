package me.zhenxin.zmusic.util.ext

import java.io.InputStream
import java.nio.charset.StandardCharsets

object InputStreamExt {
    fun InputStream.readText(): String {
        val bytes = this.readBytes()
        this.close()
        return String(bytes, StandardCharsets.UTF_8)
    }
}
