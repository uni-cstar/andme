package andme.tv.leanback.widget

import andme.tv.leanback.presenter.BasePresenter
import android.view.View
import androidx.leanback.widget.Presenter

/**
 * Created by Lucio on 2021/3/29.
 */
class RecyclerViewItemSelectedHelper() : View.OnFocusChangeListener {

    private val mChangedView = mutableMapOf<Boolean, View?>()

    private var mLastSelectedView: View? = null

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        changeSelectView(v, hasFocus)
    }

    private fun changeSelectView(v: View,hasFocus: Boolean){
        mChangedView[hasFocus] = v
        if (mChangedView[true] == mChangedView[false]) {
            // 获得焦点和失去焦点的是同一个item，会有以下两种情况：
            //  RecyclerView失去焦点
            //  RecyclerView重新获得焦点
            // 让此item保持选中状态，
            mLastSelectedView = v
            v.isSelected = true
        } else {
            mLastSelectedView?.isSelected = false
        }
    }

    private class PresenterOnItemFocusChangedListenerChain(
        val presenter: BasePresenter.OnItemFocusChangedListener?,
        val selectedHolder: RecyclerViewItemSelectedHelper
    ) :
        BasePresenter.OnItemFocusChangedListener {
        override fun onItemFocusChanged(
            view: View,
            hasFocus: Boolean,
            viewHolder: Presenter.ViewHolder,
            item: Any?
        ) {
            presenter?.onItemFocusChanged(view, hasFocus, viewHolder, item)
            selectedHolder.onFocusChange(view, hasFocus)
        }
    }

    /**
     * 强制设置某个view为选中状态
     */
    fun setViewSelectForce(v: View) {
        mChangedView[true] = v
        mChangedView[false] = v
        changeSelectView(v, true)
    }

    companion object {

        /**
         * 为保障[BasePresenter.setOnItemFocusChangedListener]设置的监听有效，请在调用此方法前设置。
         */
        @JvmStatic
        fun attach(presenter: BasePresenter): RecyclerViewItemSelectedHelper {
            val presenterListener = presenter.getOnItemFocusChangedListener()
            val helper = RecyclerViewItemSelectedHelper()
            val chain = PresenterOnItemFocusChangedListenerChain(
                presenterListener,
                helper
            )
            presenter.setOnItemFocusChangedListener(chain)
            return helper
        }
    }
}