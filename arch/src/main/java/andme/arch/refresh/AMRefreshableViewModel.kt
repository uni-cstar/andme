package andme.arch.refresh

import andme.arch.app.AMViewModel
import andme.arch.app.AMViewModelOwner
import andme.core.lifecycle.SingleEvent
import andme.core.lifecycle.SingleLiveEvent
import andme.lang.Note
import android.app.Application
import androidx.annotation.MainThread

/**
 * Created by Lucio on 2020/12/16.
 * 支持刷新和加载的事件模型
 */
@Note("由于可能只需要刷新事件，所以加载更多的事件变量均为懒加载:这么做其实也没啥必要，对性能没啥影响")
open class AMRefreshableViewModel(application: Application) : AMViewModel(application) {

    val refreshSuccessEvent = SingleEvent()
    val refreshFailEvent = SingleLiveEvent<Throwable?>()

    /**
     * 刷新事件
     */
    private val hasMoreEventDelegate = lazy {
        SingleLiveEvent<Boolean>()
    }
    val hasMoreEvent by hasMoreEventDelegate

    private val loadMoreSuccessEventDelegate = lazy {
        SingleLiveEvent<Boolean>()
    }
    val loadMoreSuccessEvent by loadMoreSuccessEventDelegate

    private val loadMoreFailEventDelegate = lazy {
        SingleLiveEvent<Throwable?>()
    }
    val loadMoreFailEvent by loadMoreFailEventDelegate

    override fun unregister(owner: AMViewModelOwner) {
        //移除常规事件监听
        refreshSuccessEvent.removeObservers(owner)
        refreshFailEvent.removeObservers(owner)

        if (hasMoreEventDelegate.isInitialized()) {
            hasMoreEvent.removeObservers(owner)
        }
        if (loadMoreSuccessEventDelegate.isInitialized()) {
            loadMoreSuccessEvent.removeObservers(owner)
        }
        if (loadMoreFailEventDelegate.isInitialized()) {
            loadMoreFailEvent.removeObservers(owner)
        }
        super.unregister(owner)
    }

    @MainThread
    protected fun setHasMore(hasMore: Boolean) {
        hasMoreEvent.call(hasMore)
    }

    protected fun postHasMore(hasMore: Boolean) {
        hasMoreEvent.postCall(hasMore)
    }

    @MainThread
    protected fun onRefreshSuccess() {
        refreshSuccessEvent.call()
    }

    protected fun postRefreshSuccess() {
        refreshSuccessEvent.postCall()
    }

    @MainThread
    protected fun onRefreshSuccess(hasMore: Boolean) {
        onRefreshSuccess()
        setHasMore(hasMore)
    }

    protected fun postRefreshSuccess(hasMore: Boolean) {
        postRefreshSuccess()
        postHasMore(hasMore)
    }

    @MainThread
    protected fun onRefreshFail(e: Throwable?) {
        refreshFailEvent.call(e)
    }

    protected fun postRefreshFail(e: Throwable?) {
        refreshFailEvent.postCall(e)
    }

    @MainThread
    protected fun onLoadMoreSuccess(hasMore: Boolean) {
        loadMoreSuccessEvent.call(hasMore)
    }

    protected fun postLoadMoreSuccess(hasMore: Boolean) {
        loadMoreSuccessEvent.postCall(hasMore)
    }

    @MainThread
    protected fun onLoadMoreFail(e: Throwable?) {
        loadMoreFailEvent.call(e)
    }

    protected fun postLoadMoreFail(e: Throwable?) {
        loadMoreFailEvent.call(e)
    }

}