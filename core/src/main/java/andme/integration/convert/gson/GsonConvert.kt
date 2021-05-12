package andme.integration.convert.gson

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Lucio on 2021/1/27.
 */
class GsonConvert @JvmOverloads constructor(val gson: Gson = Gson()) : JsonConvert {

    override fun <T> toObject(json: String?, clazz: Class<T>): T? {
        if (json.isNullOrEmpty())
            return null
        return gson.fromJson(json, clazz)
    }


    override fun <T> toObjectList(json: String?, clazz: Class<T>): List<T>? {
        if (json.isNullOrEmpty())
            return null
        val type: Type = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(json, type)
    }

    override fun toJson(obj: Any?): String? {
        if (obj == null)
            return null
        return gson.toJson(obj)
    }
}