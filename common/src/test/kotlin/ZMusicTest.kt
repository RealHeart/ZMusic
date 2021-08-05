import com.alibaba.fastjson.JSON
import me.zhenxin.zmusic.common.config.Config
import me.zhenxin.zmusic.common.modules.api.impl.NeteaseApi

/**
 * 测试类
 *
 * @author 真心
 * @since 2021/8/5 20:41
 * @email qgzhenxin@qq.com
 */
fun main() {
    Config.ApiLinkNetease = "https://netease.api.zhenxin.xyz"
    val api = NeteaseApi()
    val json = JSON.toJSONString(api.searchPage("勾指起誓", 1, 1), true)
    println(json)
}