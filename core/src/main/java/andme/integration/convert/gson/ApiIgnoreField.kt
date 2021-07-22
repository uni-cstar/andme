package andme.integration.convert.gson

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by Lucio on 2021/6/18.
 * api序列化忽略的字段
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(
    AnnotationTarget.FIELD)
annotation class GsonApiIgnoreField(
    /**
     * If `true`, the field marked with this annotation is written out in the JSON while
     * serializing. If `false`, the field marked with this annotation is skipped from the
     * serialized output. Defaults to `true`.
     * @since 1.4
     */
    val serialize: Boolean = false,
    /**
     * If `true`, the field marked with this annotation is deserialized from the JSON.
     * If `false`, the field marked with this annotation is skipped during deserialization.
     * Defaults to `true`.
     * @since 1.4
     */
    val deserialize: Boolean = false,
)



open class GsonApiSerializationExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes): Boolean {
        val anno =  f.getAnnotation(GsonApiIgnoreField::class.java)
        return anno != null && !anno.serialize
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}

open class GsonApiDeserializationExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes): Boolean {
        val anno =  f.getAnnotation(GsonApiIgnoreField::class.java)
        return anno != null && !anno.deserialize
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}

