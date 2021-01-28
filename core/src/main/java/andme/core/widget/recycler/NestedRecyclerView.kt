package andme.core.widget.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by Lucio on 2018/9/3.
 */

open class NestedRecyclerView @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet? = null,
                                                        defStyle: Int = 0)
    : androidx.recyclerview.widget.RecyclerView(context, attrs, defStyle) {

    init {
        fixNestedScroll()
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        layoutManager?.apply {
            if (this is androidx.recyclerview.widget.LinearLayoutManager) {
                //GridLayoutManager]是LinearLayoutManager的子类
                isSmoothScrollbarEnabled = true
            }
            isAutoMeasureEnabled = true
        }
    }

    /**
     * 重写，避免RecyclerView作为Item，阻塞parent的事件
     * 即：点击RecyclerView内的空白区域（即RecyclerView及其item不响应事件时能够向下传递事件）
     */
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        super.onTouchEvent(e)
        return false
    }
}