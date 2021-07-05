package andme.arch.multistate

import andme.arch.app.AMViewModel
import andme.core.exception.friendlyMessage
import andme.core.statelayout.StateView
import andme.lang.orDefaultIfNullOrEmpty
import android.app.Application
import android.view.View
import androidx.annotation.MainThread
import java.lang.RuntimeException

/**
 * Created by Lucio on 2021/3/3.
 * 用于Activity/Fragment的主ViewModel
 * 通过[bindMultiStateViewModel]方法绑定一个[MultiStateLayoutViewModel],从而将两个ViewModel绑定在一起
 */
open class MultiStateViewModel(application: Application) : AMViewModel(application) {

    protected lateinit var multiStateLayoutViewModel: MultiStateLayoutViewModel
        private set

    fun bindMultiStateLayoutViewModel(vm: MultiStateLayoutViewModel) {
        this.multiStateLayoutViewModel = vm
        onMultiStateLayoutViewModelPrepared(vm)
    }

    protected open fun onMultiStateLayoutViewModelPrepared(vm: MultiStateLayoutViewModel) {}

    /**
     * 执行与context相关的操作
     */
    @MainThread
    fun invokeMultiStateAction(ctxAction: MultiStateLayoutViewModel.StateLayoutEventAction) {
        multiStateLayoutViewModel.performStateLayoutAction(ctxAction)
    }

    @MainThread
    fun showMultiStateContentView() {
        multiStateLayoutViewModel.showContentView()
    }

    @MainThread
    fun showMultiStateLoadingView() {
        multiStateLayoutViewModel.showLoadingView()
    }

    @MainThread
    fun showMultiStateLoadingView(msg: String?) {
        showMultiStateLoadingView {
            showLoadingMsg(msg)
        }
    }

    @MainThread
    fun showMultiStateLoadingView(setup: (StateView.() -> Unit)?) {
        multiStateLayoutViewModel.showLoadingView(setup)
    }

    @MainThread
    fun showMultiStateEmptyView() {
        multiStateLayoutViewModel.showEmptyView()
    }

    @MainThread
    fun showMultiStateEmptyView(msg: String) {
        showMultiStateEmptyView {
            showEmptyMsgWithoutButton(msg)
        }
    }

    @MainThread
    fun showMultiStateEmptyView(setup: (StateView.() -> Unit)?) {
        multiStateLayoutViewModel.showEmptyView(setup)
    }

    @MainThread
    fun showMultiStateErrorView() {
        showMultiStateErrorView(RuntimeException("未知异常。"))
    }

    @MainThread
    fun showMultiStateErrorView(e: Throwable) {
        showMultiStateErrorView {
            showErrorMsgWithoutButton(e.friendlyMessage.orDefaultIfNullOrEmpty("未知错误"))
        }
    }

    @MainThread
    fun showMultiStateErrorView(
        e: Throwable,
        buttonText: String = "重试",
        requestFocus: Boolean = false,
        onBtnClick: (View) -> Unit
    ) {
        showMultiStateErrorView {
            showErrorMsgWithButton(
                e.friendlyMessage.orDefaultIfNullOrEmpty("未知错误"),
                buttonText,
                requestFocus,
                onBtnClick
            )
        }
    }

    @MainThread
    fun showMultiStateErrorView(setup: (StateView.() -> Unit)?) {
        multiStateLayoutViewModel.showErrorView(setup)
    }

    @MainThread
    fun showMultiStateNoNetworkView() {
        multiStateLayoutViewModel.showNoNetworkView()
    }

    @MainThread
    fun showMultiStateNoNetworkView(setup: (StateView.() -> Unit)?) {
        multiStateLayoutViewModel.showNoNetworkView(setup)
    }


}