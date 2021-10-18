/**
 * Created by Lucio on 2020/12/13.
 */
package andme.lang


/**
 * 添加
 */
@Deprecated("请使用bas-core appendAllIfNotNull")
inline fun <E> MutableCollection<E>.addAllNotNull(elements: Collection<E>?) {
    if (!elements.isNullOrEmpty())
        addAll(elements)
}


/**
 * 两个集合的内容是否相等；集合中的element必须重写equals方法
 */
@Deprecated("请使用bas-core areItemsEqual")
inline fun <E> Collection<E>?.areContentEqual(other: Collection<E>?): Boolean {
    if (this == null) {
        return other == null
    }

    if (other == null || this.size != other.size)
        return false
    return this.containsAll(other)
}
