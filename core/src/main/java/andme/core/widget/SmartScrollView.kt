package andme.core.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

/**
 * Created by Lucio on 2021/4/7.
 */
class SmartScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {


    private var mScrolledToTop = true
    private var mScrolledToBottom = false
    private var mScrollChangeListener:OnScrollChangedListener? = null

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (scrollY == 0) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
            mScrolledToTop = true;
            mScrolledToBottom = false;
        } else if (scrollY + height - paddingTop - paddingBottom == getChildAt(0).height) {
            // 小心踩坑2: 这里不能是 >=
            // 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
            mScrolledToBottom = true
            mScrolledToTop = false
        } else {
            mScrolledToBottom = false
            mScrolledToBottom = false
        }
        notifyScrollChangedListeners()
    }

    private fun notifyScrollChangedListeners() {
        mScrollChangeListener?.let {

        }
    }

    interface OnScrollChangedListener{

        fun onScrollToTop()

        fun onScrollToMid()

        fun onScrollToEnd()
    }


}