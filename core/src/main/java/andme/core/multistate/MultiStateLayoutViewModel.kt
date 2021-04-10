package andme.core.multistate

import andme.core.lifecycle.MutableLiveEvent
import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel

/**
 * Created by Lucio on 2021/3/3.
 */
open class MultiStateLayoutViewModel(application: Application) : AndroidViewModel(application) {

    fun interface MultiStateLayoutAction {
        fun onMultiStateLayout(layout: MultiStateLayout)
    }

    val multiStateLayoutEvent = MutableLiveEvent<MultiStateLayoutAction>()

    /**
     * 执行与context相关的操作
     */
    @MainThread
    fun invokeMultiStateAction(ctxAction: MultiStateLayoutAction) {
        multiStateLayoutEvent.value = ctxAction
    }

    @MainThread
    fun showContent() {
        invokeMultiStateAction {
            it.showContent()
        }
    }

    @MainThread
    fun showLoading() {
        invokeMultiStateAction {
            it.showLoading()
        }
    }

    @MainThread
    fun showEmpty() {
        invokeMultiStateAction {
            it.showEmpty()
        }
    }

    @MainThread
    fun showError() {
        invokeMultiStateAction {
            it.showError()
        }
    }

    fun showNoNetworkView() {
        invokeMultiStateAction {
            it.showNoNetworkView()
        }
    }

}