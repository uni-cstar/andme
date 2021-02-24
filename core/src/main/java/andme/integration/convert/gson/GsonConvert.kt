package andme.integration.convert.gson

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Lucio on 2021/1/27.
 */


var gson: Gson = Gson()

inline fun Any?.toJson(): String? {
    if (this == null)
        return null
    return gson.toJson(this)
}

fun <T> toObject(json: String?, clzz: Class<T>): T? {
    if (json.isNullOrEmpty())
        return null
    return gson.fromJson(json, clzz)
}

fun <T> toObject(json: String?, typeOfT: Type): T? {
    if (json.isNullOrEmpty())
        return null
    return gson.fromJson(json, typeOfT)
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