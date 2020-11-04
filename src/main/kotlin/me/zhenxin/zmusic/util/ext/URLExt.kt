package me.zhenxin.zmusic.util.ext

import me.zhenxin.zmusic.ZMusic
import me.zhenxin.zmusic.util.ext.InputStreamExt.readText
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

internal object URLExt {
    fun URL.readText(
        header: MutableMap<String, String> = mutableMapOf()
    ): String {
        val con = conInit(this.openConnection() as HttpURLConnection, header)
        con.requestMethod = "GET"
        return getString(con)
    }

    fun URL.readText(
        data: String,
        header: MutableMap<String, String> = mutableMapOf(),
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
        return getString(con)
    }

    private fun conInit(con: HttpURLConnection, header: MutableMap<String, String>): HttpURLConnection {
        con.readTimeout = 20000
        con.connectTimeout = 5000
        header.putAll(
            mapOf(
                "Charset" to "UTF-8",
                "User-Agent" to "Mozilla/5.0 ZMusic/" + ZMusic.thisVer
            )
        )
        header.forEach { con.setRequestProperty(it.key, it.value) }
        return con
    }

    private fun getString(con: HttpURLConnection) = when (con.responseCode) {
        200, 201, 202 -> con.inputStream.readText()
        else -> con.errorStream.readText()
    }
}
