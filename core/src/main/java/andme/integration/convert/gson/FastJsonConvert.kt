package andme.integration.convert.gson

import com.alibaba.fastjson.JSON

/**
 * Created by Lucio on 2021/7/6.
 */
class FastJsonConvert : JsonConvert{
    override fun <T> toObject(json: String?, clazz: Class<T>): T? {
        if (json.isNullOrEmpty())
            return null
        return JSON.parseObject(json, clazz)
    }

    override fun <T> toObjectList(json: String?, clazz: Class<T>): List<T>? {
        if (json.isNullOrEmpty())
            return null
        return JSON.parseArray(json, clazz)
    }

    override fun toJson(obj: Any?): String? {
        if (obj == null)
            return null
        return JSON.toJSONString(obj)
    }
}