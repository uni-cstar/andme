@file:JvmName("Numbers")

package andme.lang.internal

import java.text.DecimalFormat

/**
 * 保留小数点后一位
 */
internal val decimalFormat1 by lazy {
    DecimalFormat("0.0")
}
/**
 * 保留小数点后两位
 */
internal val decimalFormat2 by lazy {
    DecimalFormat("0.00")
}

/**
 * 创建Decimal格式化表达式
 * @param decimalPlace 小数点后多少位
 * @param fill 填充字符；默认用0填充
 */
internal fun createDecimalPattern(decimalPlace: Int, fill: String = "0"): String {
    var de = decimalPlace
    val pattern: StringBuilder = StringBuilder(fill).apply {
        if (de > 0) {
            append(".")
        }
    }

    while (de > 0) {
        de--
        pattern.append(fill)
    }
    return pattern.toString()
}