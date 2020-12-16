package andme.integration.support.refresh

interface AMLoadMoreEvent {
    fun onLoadMoreSuccess(hasMore: Boolean)
    fun onLoadMoreError(e: Throwable?)
    fun setHasMore(hasMore: Boolean)
}