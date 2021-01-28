package andme.core.widget.recycler

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Lucio on 2019/1/10.
 */

/**
 * 解决NestedScrollView嵌套RecyclerView时，在RecyclerView上滑动没有了滚动效果的情况
 */
fun RecyclerView.fixNestedScroll() {
    layoutManager?.apply {
        if (this is androidx.recyclerview.widget.LinearLayoutManager) {
            //GridLayoutManager]是LinearLayoutManager的子类
            isSmoothScrollbarEnabled = true
        }
        isAutoMeasureEnabled = true
    }
    setHasFixedSize(true)
    isNestedScrollingEnabled = false
}


