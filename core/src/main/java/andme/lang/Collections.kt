/**
 * Created by Lucio on 2020/12/13.
 */
package andme.lang


/**
 * 添加
 */
inline fun <E> MutableCollection<E>.addAllNotNull(elements: Collection<E>?){
    if(!elements.isNullOrEmpty())
        addAll(elements)
}

