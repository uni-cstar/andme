package andme.arch.multistate

import andme.core.lifecycle.MutableLiveEvent
import andme.core.statelayout.StateLayout
import andme.core.statelayout.StateLayoutAction
import andme.core.statelayout.StateView
import android.app.Application
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel

/**
 * Created by Lucio on 2021/3/3.
 */
open class MultiStateLayoutViewModel(application: Application) : AndroidViewModel(application),
    StateLayoutAction {

    fun interface StateLayoutEventAction {
        fun onStateLayoutActionEvent(layout: StateLayout)
    }

    val stateLayoutActionEvent = MutableLiveEvent<StateLayoutEventAction>()

    /**
     * 执行与context相关的操作
     */
    @MainThread
    fun performStateLayoutAction(ctxAction: StateLayoutEventAction) {
        stateLayoutActionEvent.value = ctxAction
    }

    @MainThread
    override fun showContentView() {
        performStateLayoutAction {
            it.showContentView()
        }
    }

    @MainThread
    override fun showLoadingView(setup: (StateView.() -> Unit)?) {
        performStateLayoutAction {
            it.showLoadingView(setup)
        }
    }

    @MainThread
    override fun showEmptyView(setup: (StateView.() -> Unit)?) {
        performStateLayoutAction {
            it.showEmptyView(setup)
        }
    }

    @MainThread
    override fun showErrorView(setup: (StateView.() -> Unit)?) {
        performStateLayoutAction {
            it.showErrorView(setup)
        }
    }

    @MainThread
    override fun showNoNetworkView(setup: (StateView.() -> Unit)?) {
        performStateLayoutAction {
            it.showNoNetworkView(setup)
        }
    }

    @MainThread
    override fun showCustomStateView(state: Int, setup: (View.() -> Unit)?) {
      performStateLayoutAction {
          it.showCustomStateView(state, setup)
      }
    }
}