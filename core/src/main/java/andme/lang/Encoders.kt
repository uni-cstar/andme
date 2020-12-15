@file:JvmName("Encoders")
package andme.lang

import android.net.Uri
import java.security.MessageDigest

/**
 * Created by Lucio on 2020/12/13.
 */
/**
 *  转换成MD5字符串（标准MD5加密算法）
 */
fun String?.toMd5(): String {
    if (this.isNullOrEmpty())
        return ""
    var bytes = this.toByteArray(Charsets.UTF_8)
    val md5 = MessageDigest.getInstance("MD5")
    bytes = md5.digest(bytes)

    val result = StringBuilder()
    for (item in bytes) {
        val hexStr = Integer.toHexString(0xFF and item.toInt())
        if (hexStr.length < 2) {
            result.append("0")
        }
        result.append(hexStr)
    }
    return result.toString()
}


/**
 * url编码
 * @see [BestPerformance]
 * @param allow set of additional characters to allow in the encoded form,
 *  null if no characters should be skipped
 * @return an encoded version of s suitable for use as a URI component,
 *  or null if s is null
 */
@Note("在url编码中，我门通常使用的java.net.URLEncoder.encode方法进行url编码，此方法存在一个问题就是会将空格转换成＋，导致iOS那边无法将＋解码成空格" +
        "详细请见：https://www.jianshu.com/p/4a7eb969235d")
@JvmOverloads
inline fun String.urlEncode(allow: String? = null): String {
    return Uri.encode(this, allow)
}

/**
 * url解码
 */
inline fun String.urlDecode(): String {
    return Uri.decode(this)
}
