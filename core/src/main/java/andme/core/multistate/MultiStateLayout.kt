package andme.core.multistate

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes

/**
 * Created by Lucio on 2021/3/30.
 */
interface MultiStateLayout {

    fun getAttachedViewGroup():ViewGroup

    /**
     * 显示内容布局
     */
    fun showContent()

    /**
     * 显示loading
     */
    fun showLoading(): View

    /**
     * 显示loading
     */
    fun showLoading(hint: String): View

    /**
     * 显示loading
     * @param hintViewId 设置hint的view id
     * @param hint 设置的提示信息
     */
    fun showLoading(@IdRes hintViewId: Int, hint: String): View

    /**
     * 设置自定义
     * @param customView 自定义view
     */
    fun setCustomLoadingView(customView: View)

    fun showEmpty(): View

    fun showEmpty(hint: String): View

    fun showEmpty(@IdRes hintViewId: Int, hint: String): View

    fun setCustomEmptyView(customView: View)

    fun showError(): View

    fun showError(hint: String): View

    fun showError(@IdRes hintViewId: Int, hint: String): View

    fun setCustomErrorView(customView: View)


    fun showNoNetworkView(): View

    fun showNoNetworkView(hint: String): View

    fun showNoNetworkView(@IdRes hintViewId: Int, hint: String): View

    fun setCustomNoNetworkView(customView: View)

    fun showCustomStateView(state: Int): View

    /**
     * 获取当前显示的State
     */
    fun getCurrentState():Int

    fun interface OnItemViewClickListener{
        fun onItemViewClick(view:View,state:Int)
    }
    fun setOnItemViewClickListener(listener:OnItemViewClickListener?)
}