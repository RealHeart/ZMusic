package me.zhenxin.zmusic.common.utils

import java.io.FileOutputStream
import java.io.InputStream
import java.security.MessageDigest


/**
 * 扩展函数
 * @author 真心
 * @since 2021/1/23 16:44
 * @email qgzhenxin@qq.com
 */

/**
 * 生成md5
 */
fun String.md5(): String {
    try {
        val m = MessageDigest.getInstance("MD5")
        m.update(this.toByteArray(charset("UTF8")))
        val s = m.digest()
        var result = ""
        for (i in s.indices) {
            result += Integer.toHexString(0x000000FF and s[i].toInt() or -0x100).substring(6)
        }
        return result
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return this
}

/**
 * 保存InputStream到本地
 * @param path 路径
 */
fun InputStream.saveData(path: String) {
    var index: Int
    val bytes = ByteArray(1024)
    val outputStream = FileOutputStream(path)
    while (this.read(bytes).also { index = it } != -1) {
        outputStream.write(bytes, 0, index)
        outputStream.flush()
    }
    this.close()
    outputStream.close()
}