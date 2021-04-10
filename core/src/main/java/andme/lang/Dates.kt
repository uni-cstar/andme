package andme.lang

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lucio on 2021/1/18.
 */

/**
 * 一天
 */
const val ONE_DAY_TIME = 86400000L

/**
 * 一个小时
 */
const val ONE_HOUR_TIME = 3600000L

/**
 * 一分钟
 */
const val ONE_MINUTE_TIME = 60000L

/**
 * utc 时间格式
 */
const val DATE_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

/**
 * 国内常用的完整时间格式，yyyy-MM-dd HH:mm:ss
 */
const val DATE_CN_FORMAT = "yyyy-MM-dd HH:mm:ss"


/**
 * 星期几 格式
 */
const val DATE_WEEK_FORMAT = "E"

/**
 * utc时间格式化
 */
val UTC_DATE_FORMAT by lazy {
    SimpleDateFormat(DATE_UTC_FORMAT)
}


/**
 * 格式化时间
 */
fun Date?.format(format: String): String {
    if (this == null) return ""
    return SimpleDateFormat(format).format(this)
}

/**
 * 获取星期几
 */
val Date.week: String
    get() = this.format(DATE_WEEK_FORMAT)

/**
 * utc时间格式字符串
 */
val Date.toUtc: String
    get() = UTC_DATE_FORMAT.format(this)

/**
 * 获取UTC时间
 * @return
 */
fun UTCDate(): Date {
    // 1、取得本地时间：
    val cal = Calendar.getInstance()
    // 2、取得时间偏移量：
    val zoneOffset = cal.get(Calendar.ZONE_OFFSET)
    // 3、取得夏令时差：
    val dstOffset = cal.get(Calendar.DST_OFFSET)
    // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
    cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset))
    return cal.time
}

/**
 * 时间转换成本地时间
 */
fun Date.toLocalDate(): Date {
    // 1、取得本地时间：
    val cal = Calendar.getInstance()
    // 2、取得时间偏移量：
    val zoneOffset = cal.get(Calendar.ZONE_OFFSET)
    // 3、取得夏令时差：
    val dstOffset = cal.get(Calendar.DST_OFFSET)
    cal.time = this
    cal.add(Calendar.MILLISECOND, zoneOffset + dstOffset)
    return cal.time
}
