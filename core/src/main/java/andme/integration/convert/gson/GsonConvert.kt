package andme.integration.convert.gson

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Lucio on 2021/1/27.
 */


var gson: Gson = Gson()

inline fun Any?.toJson(): String? {
    if (this == null)
        return null
    return gson.toJson(this)
}

inline fun <reified T> String?.toObject(): T? {
    if (this.isNullOrEmpty())
        return null
    return gson.fromJson(this, T::class.java)
}

inline fun <reified T> String?.toObjectList(): List<T>? {
    if (this.isNullOrEmpty())
        return null
    return gson.fromJson(this, object : TypeToken<List<T>>() {}.type)
}