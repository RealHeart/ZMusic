package me.zhenxin.zmusic.utils

import cn.hutool.json.JSONObject
import me.zhenxin.zmusic.config.config
import java.net.URLEncoder

/**
 * ZMusic VIP
 *
 * @author 真心
 * @since 2022/1/19 11:40
 */

/**
 * m4s转mp3
 */
fun m4s2mp3(id: String, url: String): String {
    val result = get(
        "https://api.zplu.cc/zmusic/vip/m4s2mp3?id=$id&url=${
            URLEncoder.encode(
                url,
                "UTF-8"
            )
        }&qq=${config.VIP_QQ}&key=${config.VIP_KEY}"
    )
    val json = JSONObject(result)
    if (json.getInt("code") != 200) {
        return ""
    }
    return json.getStr("data")
}