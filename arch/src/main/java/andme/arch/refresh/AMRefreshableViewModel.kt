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
@Note("由于可能只需要刷新事件，所以加载更多的事件变量均为懒加载")
open class AMRefreshableViewModel(application: Application) : AMViewModel(application) {

    val refreshSuccessEvent = SingleEvent()

    val refreshSuccessEvent2 = SingleLiveEvent<Boolean>()

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
        val lifecycleOwner = owner.getLifecycleOwner()

        refreshSuccessEvent.removeObservers(lifecycleOwner)
        refreshSuccessEvent2.removeObservers(lifecycleOwner)
        refreshFailEvent.removeObservers(lifecycleOwner)

        if (hasMoreEventDelegate.isInitialized()) {
            hasMoreEvent.removeObservers(lifecycleOwner)
        }
        if (loadMoreSuccessEventDelegate.isInitialized()) {
            loadMoreSuccessEvent.removeObservers(lifecycleOwner)
        }
        if (loadMoreFailEventDelegate.isInitialized()) {
            loadMoreFailEvent.removeObservers(lifecycleOwner)
        }
        super.unregister(owner)
    }

    @MainThread
    fun setHasMore(hasMore: Boolean) {
        hasMoreEvent.call(hasMore)
    }

    fun postHasMore(hasMore: Boolean) {
        hasMoreEvent.postCall(hasMore)
    }

    @MainThread
    fun onRefreshSuccess() {
        refreshSuccessEvent.call()
    }

    fun postRefreshSuccess() {
        refreshSuccessEvent.postCall()
    }

    @MainThread
    fun onRefreshSuccess(hasMore: Boolean) {
        refreshSuccessEvent2.call(hasMore)
    }

    fun postRefreshSuccess(hasMore: Boolean) {
        refreshSuccessEvent2.postCall(hasMore)
    }

    @MainThread
    fun onRefreshFail(e: Throwable?) {
        refreshFailEvent.call(e)
    }

    fun postRefreshFail(e: Throwable?) {
        refreshFailEvent.postCall(e)
    }

    @MainThread
    fun onLoadMoreSuccess(hasMore: Boolean) {
        loadMoreSuccessEvent.call(hasMore)
    }

    fun postLoadMoreSuccess(hasMore: Boolean) {
        loadMoreSuccessEvent.postCall(hasMore)
    }

    @MainThread
    fun onLoadMoreFail(e: Throwable?) {
        loadMoreFailEvent.call(e)
    }

    fun postLoadMoreFail(e: Throwable?) {
        loadMoreFailEvent.call(e)
    }

}