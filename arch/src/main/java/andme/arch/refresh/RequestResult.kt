package andme.arch.refresh

/**
 * Created by Lucio on 2020/12/17.
 * @param pageIndex 当前页码
 * @param hasMore 是否还有更多数据
 * @param data 本次返回的数据
 *
 */
data class RequestResult<T>(val pageIndex: Int, val hasMore: Boolean, val data: List<T>?)