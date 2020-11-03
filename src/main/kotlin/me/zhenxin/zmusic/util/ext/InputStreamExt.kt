package me.zhenxin.zmusic.util.ext

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

object InputStreamExt {
    fun InputStream.readText(): String {
        val buffer = ByteArray(1024)
        var len: Int
        val bos = ByteArrayOutputStream()
        while (this.read(buffer).also { len = it } != -1) {
            bos.write(buffer, 0, len)
        }
        bos.close()
        return String(bos.toByteArray(), StandardCharsets.UTF_8)
    }
}
