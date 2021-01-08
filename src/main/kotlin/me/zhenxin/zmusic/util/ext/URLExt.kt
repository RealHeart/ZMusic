package me.zhenxin.zmusic.util.ext

import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.util.ext.InputStreamExt.readText
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

object URLExt {
    fun URL.readText(
        header: MutableMap<String, String> = mutableMapOf(),
        gzip: Boolean = false
    ): String {
        val con = conInit(this.openConnection() as HttpURLConnection, header)
        con.requestMethod = "GET"
        return getString(con, gzip)
    }

    fun URL.readText(
        data: String,
        header: MutableMap<String, String> = mutableMapOf(),
        gzip: Boolean = false
    ): String {
        val con = conInit(this.openConnection() as HttpURLConnection, header)
        con.requestMethod = "POST"
        con.doOutput = true
        con.doInput = true
        val out = DataOutputStream(con.outputStream)
        con.connect()
        out.writeBytes(data)
        out.flush()
        out.close()
        return getString(con, gzip)
    }

    private fun conInit(con: HttpURLConnection, header: MutableMap<String, String>): HttpURLConnection {
        con.readTimeout = 20000
        con.connectTimeout = 5000
        if (header.isNullOrEmpty()) {
            header["User-Agent"] = "Mozilla/5.0 ZMusic/" + ZMusic.thisVer
        }
        header["Charset"] = "UTF-8"
        header.forEach { con.setRequestProperty(it.key, it.value) }
        return con
    }

    private fun getString(con: HttpURLConnection, gzip: Boolean) = when (con.responseCode) {
        200, 201, 202 -> con.inputStream.readText(gzip)
        else -> con.errorStream.readText(gzip)
    }
}
