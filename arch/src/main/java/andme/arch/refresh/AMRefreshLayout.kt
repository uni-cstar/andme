package andme.arch.refresh

/**
 * Created by Lucio on 2020/12/16.
 */
interface AMRefreshLayout {
    fun setHasMore(hasMore: Boolean)
    fun onRefreshSuccess()
    fun onRefreshSuccess(hasMore: Boolean)
    fun onRefreshFail(e: Throwable?)
    fun onLoadMoreSuccess(hasMore: Boolean)
    fun onLoadMoreFail(e: Throwable?)
}