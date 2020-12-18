package andme.arch.refresh.scene

import andme.arch.refresh.AMRefreshableViewModel
import andme.arch.refresh.RequestResult
import andme.core.exception.onCatch
import andme.core.exception.tryCatch
import andme.core.exceptionHandlerAM
import android.app.Application
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Lucio on 2020/12/17.
 * 提供了下拉刷新和上拉加载更多的ViewModel 模版代码
 * 使用时只需要对应实现[doRefreshRequest]和[doLoadMoreRequest]方法处理下拉刷新和上拉加载更多的业务逻辑即可
 *
 * 在用户下拉操作时调用[onRefresh]
 * 上拉加载时调用[onLoadMore]
 */
abstract class AMRefreshableSceneViewModel<T>(application: Application) : AMRefreshableViewModel(application) {

    protected  var _onRefreshResultEvent: MutableLiveData<List<T>?> =  MutableLiveData()
    val refreshResultEvent: LiveData<List<T>?> get() = _onRefreshResultEvent

    protected var _onLoadMoreResultEvent: MutableLiveData<List<T>?> = MutableLiveData()
    val loadMoreResultEvent: LiveData<List<T>?> get() = _onLoadMoreResultEvent

    protected var currentPageIndex: Int = 1

    /**
     * 用于提供初始加载的数据
     */
    protected fun loadInitData(): List<T>? {
        return null
    }

    fun onRefresh() {
        viewModelScope.launch {
            tryCatch {
                val result = withContext(Dispatchers.IO) {
                    doRefreshRequest()
                }
                onRefreshRequestSuccess(result)
            }?.onCatch {
                onRefreshRequestError(it)
            }
        }
    }

    @WorkerThread
    abstract suspend fun doRefreshRequest(): RequestResult<T>

    @MainThread
    protected open fun onRefreshRequestError(err: Throwable) {
        onRefreshFail(err)
        invokeContextAction {
            exceptionHandlerAM.handleUIException(it, err)
        }
    }

    @MainThread
    protected open fun onRefreshRequestSuccess(result: RequestResult<T>) {
        currentPageIndex = result.pageIndex
        _onRefreshResultEvent.value = result.data
        onRefreshSuccess(result.hasMore)
    }

    fun onLoadMore() {
        viewModelScope.launch {
            tryCatch {
                val result = withContext(Dispatchers.IO) {
                    doLoadMoreRequest(currentPageIndex + 1)
                }
                onLoadMoreRequestSuccess(result)
            }?.apply {
                onLoadMoreRequestError(this)
            }
        }
    }

    @WorkerThread
    abstract suspend fun doLoadMoreRequest(pageIndex: Int): RequestResult<T>

    @MainThread
    protected open fun onLoadMoreRequestError(err: Throwable) {
        onLoadMoreFail(err)
        invokeContextAction {
            exceptionHandlerAM.handleUIException(it, err)
        }
    }

    @MainThread
    protected open fun onLoadMoreRequestSuccess(result: RequestResult<T>) {
        currentPageIndex = result.pageIndex
        _onLoadMoreResultEvent.value = result.data
        onLoadMoreSuccess(result.hasMore)
    }
}