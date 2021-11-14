@file:JvmName("AppIDProvider")
package andme.core.content

import andme.core.DEVICE_UNIQUE_ID
import andme.core.appManagerAM
import andme.lang.toMd5
import andme.core.sharedPrefAM
import android.os.Build
import java.util.*

/**
 * Created by Lucio on 2020/11/23.
 */

/**
 * 应用唯一设备id
 */
val appUniqueDeviceId: String
    get() {

        if(appManagerAM.isDebuggable){
            return "super_luo_debug"
        }

        val pref = sharedPrefAM
        val cacheId = pref.getString(DEVICE_UNIQUE_ID, null)
        if (cacheId.isNullOrEmpty()) {
            val genId = generateAppUniqueDeviceIDAsMD5()
            pref.edit {
                putString(DEVICE_UNIQUE_ID, genId)
            }
            return genId
        }
        return cacheId
    }

/**
 * 生成设备唯一id
 */
private fun generateAppUniqueDeviceID(): String {
    val sb: StringBuilder = StringBuilder()
    sb.append(Build.BRAND)//品牌
            .append("_")
            .append(Build.MODEL)//型号
            .append("_")
            .append(Build.VERSION.RELEASE)//设备系统版本号
            .append("_")
            .append(System.currentTimeMillis())//时间戳
            .append(UUID.randomUUID().toString().replace("-", ""))
    return sb.toString()
}

/**
 * 生成
 */
fun generateAppUniqueDeviceIDAsMD5(): String {
    return generateAppUniqueDeviceID().toMd5()
}
