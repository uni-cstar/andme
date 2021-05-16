package andme.core.statelayout

import android.view.View


interface StateLayoutAction {

    /**
     * 显示内容布局
     */
    fun showContentView()

    fun showLoadingView() = showLoadingView(null)

    fun showLoadingView(setup: (StateView.() -> Unit)?)

    fun showEmptyView() = showEmptyView(null)

    fun showEmptyView(setup: (StateView.() -> Unit)?)

    fun showErrorView() = showErrorView(null)

    fun showErrorView(setup: (StateView.() -> Unit)?)

    fun showNoNetworkView() = showNoNetworkView(null)

    fun showNoNetworkView(setup: (StateView.() -> Unit)?)

    fun showCustomStateView(state: Int, setup: (View.() -> Unit)?)
}

/**
 * Created by Lucio on 2021/3/30.
 */
interface StateLayout : StateLayoutAction {

    fun setCustomViewFactory(factory: StateViewFactory?)

    /**
     * 设置自定义LoadingView
     * @param attachToViewGroup 是否将view添加到当前ViewGroup中
     */
    fun setCustomLoadingView(customView: StateView, attachToViewGroup: Boolean = false)

    fun setCustomEmptyView(customView: StateView, attachToViewGroup: Boolean = false)

    fun setCustomErrorView(customView: StateView, attachToViewGroup: Boolean = false)

    fun setCustomNoNetworkView(customView: StateView, attachToViewGroup: Boolean = false)

}