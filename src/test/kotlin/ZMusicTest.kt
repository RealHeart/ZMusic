import me.zhenxin.zmusic.module.Config
import me.zhenxin.zmusic.module.api.impl.NeteaseApi

/**
 * 测试
 *
 * @author 真心
 * @since 2021/8/18 17:44
 * @email qgzhenxin@qq.com
 */
suspend fun main() {
    Config.API_NETEASE_LINK = "https://netease.api.zhenxin.xyz"
    val api = NeteaseApi()
    println(api.searchSingle("勾指起誓"))
}