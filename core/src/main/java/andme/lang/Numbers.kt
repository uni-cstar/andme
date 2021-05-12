@file:JvmName("Numbers")

package andme.lang

import andme.lang.internal.createDecimalPattern
import andme.lang.internal.decimalFormat1
import andme.lang.internal.decimalFormat2
import java.text.DecimalFormat
import java.util.*

/**
 * Created by Lucio on 2021/3/24.
 */



/**
 * 保留一位小数
 */
fun Double?.toDecimal1(): String {
    if (this == null)
        return "0.0"
    return decimalFormat1.format(this)
}

/**
 * 保留为两位小数
 */
fun Double?.toDecimal2(): String {
    if (this == null)
        return "0.00"
    return decimalFormat2.format(this)
}

/**
 * 保留小数位
 * @param cnt 保留小数点后多少位
 */
fun Double?.toDecimal(cnt: Int = 1): String {
    if (this == null) {
        return createDecimalPattern(cnt)
    }
    return when {
        cnt <= 0 -> toInt().toString()
        cnt == 1 -> toDecimal1()
        cnt == 2 -> toDecimal2()
        else -> DecimalFormat(createDecimalPattern(cnt)).format(this)
    }
}

/**
 * 保留一位小数
 */
fun Float?.toDecimal1(): String {
    if (this == null)
        return "0.0"
    return decimalFormat1.format(this)
}

/**
 * 保留为两位小数
 */
fun Float?.toDecimal2(): String {
    if (this == null)
        return "0.00"
    return decimalFormat2.format(this)
}

/**
 * 保留小数位
 * @param cnt 保留小数点后多少位
 */
fun Float?.toDecimal(cnt: Int = 1): String {
    if (this == null) {
        return createDecimalPattern(cnt)
    }
    return when {
        cnt <= 0 -> toInt().toString()
        cnt == 1 -> toDecimal1()
        cnt == 2 -> toDecimal2()
        else ->  DecimalFormat(createDecimalPattern(cnt)).format(this)
    }
}


fun Long.secondsToHMS(): String {
    return (this * 1000L).toHMS()
}

/**
 * 转换成 小时、分钟、秒
 * this
 *  xx秒
 *  xxx:ss
 *  HH:mm:ss
 */
fun Long.toHMS(): String {
    if (this < ONE_MINUTE_TIME) {
        return "${this / 1000}秒"
    } else if (this < ONE_HOUR_TIME) {
        val minute = this / ONE_MINUTE_TIME
        val seconds = this % ONE_MINUTE_TIME / 1000
        return String.format("%02d:%02d", minute, seconds)
    } else if (this < ONE_DAY_TIME) {
        val hour = this / ONE_HOUR_TIME
        val minute = this % ONE_HOUR_TIME / ONE_MINUTE_TIME
        val seconds = this % ONE_HOUR_TIME % ONE_MINUTE_TIME / 1000
        return String.format("%02d:%02d:%02d", hour, minute, seconds)
    } else {
        return Date(this).format("yyyy-MM-dd HH:mm:ss")
    }
}

