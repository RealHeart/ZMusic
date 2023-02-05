package me.zhenxin.zmusic.utils

import com.alibaba.fastjson2.parseObject
import me.zhenxin.zmusic.config.Config
import java.net.URLEncoder

/**
 * ZMusic VIP
 *
 * @author 真心
 * @since 2022/1/19 11:40
 */

/**
 * m4s转mp3
 *
 * @param id 唯一ID
 * @param url 音频地址
 */
fun m4s2mp3(id: String, url: String): String {
    val result = post(
        "https://api.zplu.cc/zmusic/vip/m4s2mp3",
        mapOf(
            "id" to id,
            "url" to URLEncoder.encode(url, "UTF-8"),
            "qq" to Config.VIP_QQ,
            "key" to Config.VIP_KEY
        )
    )
    val json = result.parseObject()
    if (json.getIntValue("code") != 200) {
        return ""
    }
    return json.getString("data")
}

/**
 * 是否VIP
 */
fun isVip(): Boolean {
    val result = post(
        "https://api.zplu.cc/zmusic/vip/check",
        mapOf("qq" to Config.VIP_QQ, "key" to Config.VIP_KEY)
    )
    val data = result.parseObject()
    return data.getBoolean("data")
}