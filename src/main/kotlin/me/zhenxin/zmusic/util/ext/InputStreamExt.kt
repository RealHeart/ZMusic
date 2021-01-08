package me.zhenxin.zmusic.util.ext

import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream


object InputStreamExt {
    fun InputStream.readText(gzip: Boolean = false): String {
        return if (gzip) {
            val gis = GZIPInputStream(this)
            val isr = InputStreamReader(gis, StandardCharsets.UTF_8)
            val cBuffer = CharArray(1024)
            var len = 0
            val stringBuilder = StringBuilder()
            while (isr.read(cBuffer).also { len = it } != -1) {
                stringBuilder.append(cBuffer)
            }
            stringBuilder.toString()
        } else {
            val bytes = this.readBytes()
            this.close()
            String(bytes, StandardCharsets.UTF_8)
        }
    }
}
