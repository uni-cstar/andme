package andme.integration.convert.gson

import com.google.gson.Gson
import com.google.gson.JsonParser

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
        //Gson不能解析泛型类型，只能用下面方法折中处理：gson可以支持内联的泛型方法
        if (json.isNullOrEmpty())
            return null
        val results = mutableListOf<T>()

        val arry = JsonParser().parse(json).asJsonArray
        arry.forEach {
            results.add(gson.fromJson(it, clazz))
        }
        return results
//        if (json.isNullOrEmpty())
//            return null
//        val type: Type = object : TypeToken<List<T>>() {}.type
//        return gson.fromJson(json, type)
    }

    override fun toJson(obj: Any?): String? {
        if (obj == null)
            return null
        return gson.toJson(obj)
    }
}