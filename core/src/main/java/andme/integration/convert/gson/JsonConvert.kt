package andme.integration.convert.gson

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
    return jsonConvert.toObjectList(this, T::class.java)
}