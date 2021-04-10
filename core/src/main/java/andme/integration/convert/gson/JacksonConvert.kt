package andme.integration.convert.gson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper


/**
 * Created by Lucio on 2021/3/13.
 */
class JacksonConvert @JvmOverloads constructor(
    val objectMapper: ObjectMapper = ObjectMapper().also {
        it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
) : JsonConvert {

    override fun <T> toObject(json: String?, clazz: Class<T>): T? {
        if (json.isNullOrEmpty())
            return null
        return objectMapper.readValue<T>(json, clazz)
    }

    override fun <T> toObjectList(json: String?, clazz: Class<T>): List<T>? {
        if (json.isNullOrEmpty())
            return null
        val javaType: JavaType = objectMapper.typeFactory.constructParametricType(
            MutableList::class.java, clazz
        )
        return objectMapper.readValue(json, javaType)
    }

    override fun toJson(obj: Any?): String? {
        if (obj == null)
            return null
        return objectMapper.writeValueAsString(obj)
    }


}