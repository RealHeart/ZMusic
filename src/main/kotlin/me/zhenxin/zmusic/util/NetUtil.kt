package me.zhenxin.zmusic.util

import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.util.ext.InputStreamExt.readText
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object NetUtil {

    fun getAsString(
        url: String,
        header: Map<String, String> =
            mapOf(
                "Charset" to "UTF-8",
                "User-Agent" to "Mozilla/5.0 ZMusic/" + ZMusic.thisVer
            )
    ): String {
        val getUrl = URL(url)
        val con: HttpURLConnection = getUrl.openConnection() as HttpURLConnection
        con.readTimeout = 20000
        con.connectTimeout = 5000
        con.requestMethod = "GET"
        header.forEach {
            con.addRequestProperty(it.key, it.value)
        }

        return when (con.responseCode) {
            200, 201, 202 -> {
                val i: InputStream = con.inputStream
                val s = i.readText()
                i.close()
                s
            }
            else -> {
                val i: InputStream = con.errorStream
                val s = i.readText()
                i.close()
                s
            }
        }
    }

    fun postAsString() {

    }


}
