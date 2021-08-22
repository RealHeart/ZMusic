package me.zhenxin.zmusic.utils

import java.lang.StringBuilder
import java.util.Locale

/**
 * 获取从颜色A渐变到颜色B的文本
 * @param colorA 色彩A
 * @param colorB 色彩B
 * @return 经过处理的颜色渐变文本
 */
fun String.asGradient(colorA: String, colorB: String): String {
    //防止开发者手贱，不带井号，多输入空格
    var cA = colorA.trim()
    var cB = colorB.trim()
    if (!cA.startsWith("#")) cA = "#$cA"
    if (!cB.startsWith("#")) cB = "#$cB"

    //再度防止开发者手贱
    if (!(cA.length == 7 && cB.length == 7)) return this

    //对字符串进行处理，获取色值
    val aColors = hex2RGB(cA)
    val bColors = hex2RGB(cB)
    val r = intArrayOf(aColors!![0], bColors!![0])
    val g = intArrayOf(aColors[1], bColors[1])
    val b = intArrayOf(aColors[2], bColors[2])
    val step = this.length

    //使用公式 A+(B-A)*N/step N代表第几个，从1开始

    //存储渐变色值
    val gradientR = IntArray(step)
    val gradientG = IntArray(step)
    val gradientB = IntArray(step)
    for (i in 0 until step) {
        gradientR[i] = r[0] + (r[1] - r[0]) * i / step
        gradientG[i] = g[0] + (g[1] - g[0]) * i / step
        gradientB[i] = b[0] + (b[1] - b[0]) * i / step
    }
    val thisBuilder = StringBuilder()
    for (i in 0 until step) {
        thisBuilder.append(
            getColorString(
                rgb2Hex(
                    gradientR[i], gradientG[i], gradientB[i]
                )
            )
        )
        thisBuilder.append(this[i])
    }
    return thisBuilder.toString()
}

/**
 * rgb转换成16进制
 */
private fun rgb2Hex(r: Int, g: Int, b: Int): String {
    return String.format("#%02X%02X%02X", r, g, b)
}

private fun hex2RGB(hexStr: String?): IntArray? {
    if (hexStr != null && "" != hexStr && hexStr.length == 7) {
        val rgb = IntArray(3)
        rgb[0] = Integer.valueOf(hexStr.substring(1, 3), 16)
        rgb[1] = Integer.valueOf(hexStr.substring(3, 5), 16)
        rgb[2] = Integer.valueOf(hexStr.substring(5, 7), 16)
        return rgb
    }
    return null
}

private fun getColorString(color: String): String {
    var c = color
    c = c.replace("#", "").lowercase(Locale.getDefault())
    val colors = c.toCharArray()
    val stringBuilder = StringBuilder()
    stringBuilder.append("§x")
    for (i in colors) {
        stringBuilder.append("§")
        stringBuilder.append(i)
    }
    return stringBuilder.toString()
}