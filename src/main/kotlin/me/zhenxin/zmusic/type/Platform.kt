package me.zhenxin.zmusic.type

import me.zhenxin.zmusic.module.Api
import me.zhenxin.zmusic.module.api.*

enum class Platform {
    QQ,
    NETEASE,
    KUGOU,
    KUWO,
    BILIBILI,
    XIMA;

    fun getApi(): Api = when (this) {
        QQ -> QQApi()
        NETEASE -> NeteaseApi()
        KUGOU -> KugouApi()
        KUWO -> KuwoApi()
        BILIBILI -> BiliBiliApi()
        XIMA -> XimaApi()
    }
}
