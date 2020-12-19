package andme.arch.refresh

/**
 * Created by Lucio on 2020/12/16.
 */
interface AMRefreshLayout {

    /**
     * 开启刷新
     */
    fun autoRefresh(delay:Long = 0)

    /**
     * 刷新成功
     */
    fun onRefreshSuccessAM()

    /**
     * 刷新失败
     */
    fun onRefreshFailAM(e: Throwable?)

    /**
     * 是否支持刷下
     */
    var enableRefreshAM: Boolean

    /**
     * 是否正在刷新
     */
    var isRefreshingAM: Boolean

    fun setOnRefreshListenerAM(listener: OnRefreshListenerAM?)

    fun interface OnRefreshListenerAM {
        fun onRefreshAM()
    }
}

interface AMLoadMoreLayout {
    /**
     * 加载成功回调
     */
    fun onLoadMoreSuccessAM(hasMore: Boolean)

    /**
     * 加载失败回调
     */
    fun onLoadMoreFailAM(e: Throwable?)

    /**
     * 设置是否还有更多
     */
    fun setHasMoreAM(hasMore: Boolean)

    /**
     * 是否支持加载更多
     */
    var enableLoadMoreAM: Boolean

    /**
     * 是否正在加载
     */
    val isLoadingAM: Boolean

    fun setOnLoadMoreListenerAM(listener: OnLoadMoreListenerAM?)

    fun interface OnLoadMoreListenerAM{
        fun onLoadMoreAM()
    }
}