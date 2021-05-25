package andme.integration.convert.gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Lucio on 2021/3/13.
 */
interface JsonConvert {

    fun <T> toObject(json: String?, clazz: Class<T>): T?

    fun <T> toObjectList(json: String?, clazz: Class<T>): List<T>?

    fun toJson(obj: Any?): String?
}

var jsonConvert: JsonConvert = JacksonConvert()

inline fun Any?.toJson(): String? {
    return jsonConvert.toJson(this)
}

inline fun <reified T> String?.toObject(): T? {
    return jsonConvert.toObject(this, T::class.java)
}

inline fun <reified T> String?.toObjectList(): List<T>? {
    if (this.isNullOrEmpty())
        return null
    val convert = jsonConvert
    if (convert is GsonConvert) {
        //gson不支持泛型方法，所以必须在内联方法中实现；
        val type: Type = object : TypeToken<List<T>>() {}.type
        return convert.gson.fromJson(this, type)
    } else {
        return convert.toObjectList(this, T::class.java)
    }
}