package andme.core.os

import andme.lang.orDefaultIfNullOrEmpty
import android.os.Build
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by Lucio on 2021/6/10.
 */

const val UNKNOWN_PROPERTY = Build.UNKNOWN

fun readPropertyByBuild(key: String, defVal: String = UNKNOWN_PROPERTY): String {
    try {
        val cls = Class.forName("android.os.Build")
        val method = cls.getDeclaredMethod("getString", String::class.java)
        method.isAccessible = true
        return (method.invoke(null, key) as? String) ?: defVal
    } catch (e: Exception) {
        e.printStackTrace()
        return defVal
    }
}

fun readPropertyBySystemProperties(key: String, defVal: String = UNKNOWN_PROPERTY): String {
    try {
        val cls = Class.forName("android.os.SystemProperties")
        val method = cls.getMethod("get", String::class.java, String::class.java)
        return method.invoke(cls, key, "unknown") as? String ?: defVal
    } catch (e: Exception) {
        e.printStackTrace()
        return defVal
    }
}

/**
 * Returns a SystemProperty
 * @param propName The Property to retrieve
 * @return The Property, or NULL if not found
 * 此方法来源于[https://searchcode.com/codesearch/view/41537878/]
 */
fun readPropertyByRuntime(key: String, defVal: String = UNKNOWN_PROPERTY): String {
    val line = java.lang.StringBuilder()
    var input: BufferedReader? = null
    try {
        val p = Runtime.getRuntime().exec("getprop ${key}")
        input = BufferedReader(InputStreamReader(p.inputStream), 10240)
        var temp: String? = null
        do {
            temp = input.readLine()
            if (temp != null) {
                line.append(temp)
            }
        } while (temp != null)
        input.close()
        return line.toString().orDefaultIfNullOrEmpty(defVal)
    } catch (ex: Exception) {
        Log.e("getSystemProperty", "Unable to read sysprop ", ex)
        return defVal

    } finally {
        if (input != null) {
            try {
                input.close()
            } catch (e: IOException) {
                Log.e("getSystemProperty", "Exception while closing InputStream", e)
            }
        }
    }

}