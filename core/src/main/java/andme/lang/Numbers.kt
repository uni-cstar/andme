package andme.lang

import java.util.*

/**
 * Created by Lucio on 2021/3/24.
 */


fun Long.secondsToHMS(): String {
    return (this * 1000L).toHMS()
}

/**
 * 转换成 小时、分钟、秒
 *  xx秒
 *  xxx:ss
 *  HH:mm:ss
 */
fun Long.toHMS(): String {
    if (this < ONE_MINUTE_TIME) {
        return "${this}秒"
    } else if (this < ONE_HOUR_TIME) {
        val minute = this / ONE_MINUTE_TIME
        val seconds = this % ONE_MINUTE_TIME / 1000
        return String.format("%02d:%02d", minute, seconds)
    } else if (this < ONE_DAY_TIME) {
        val hour = this / ONE_HOUR_TIME
        val minute = this % ONE_HOUR_TIME / ONE_MINUTE_TIME
        val seconds = this % ONE_HOUR_TIME % ONE_MINUTE_TIME / 1000
        return String.format("%02d:%02d:%02d", hour, minute, seconds)
    }else {
        return Date(this).format("yyyy-MM-dd HH:mm:ss")
    }
}