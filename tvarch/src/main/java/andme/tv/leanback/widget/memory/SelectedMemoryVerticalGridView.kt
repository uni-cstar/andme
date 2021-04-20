package andme.tv.leanback.widget.memory

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.leanback.widget.OnChildViewHolderSelectedListener
import androidx.leanback.widget.VerticalGridView
import java.util.*

/**
 * Created by Lucio on 2021/3/31.
 */
class SelectedMemoryVerticalGridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : VerticalGridView(context, attrs, defStyleAttr) {

    val memoryHelper: GridViewMemoryHelper

    init {
        memoryHelper = GridViewMemoryHelper.apply(this)
    }

    @Deprecated("请使用addOnChildViewHolderSelectedListener")
    override fun setOnChildViewHolderSelectedListener(listener: OnChildViewHolderSelectedListener?) {
        super.setOnChildViewHolderSelectedListener(listener)
        //设置监听之后会移除之前添加的所有监听，因此要重新添加焦点记忆监听
        addOnChildViewHolderSelectedListener(memoryHelper)
    }

    @SuppressLint("RestrictedApi")
    override fun addFocusables(views: ArrayList<View>?, direction: Int, focusableMode: Int) {
        if(memoryHelper.addFocusables(views, direction, focusableMode))
            return
        super.addFocusables(views, direction, focusableMode)
    }

//    protected override fun onRequestFocusInDescendants(
//        direction: Int,
//        previouslyFocusedRect: Rect?
//    ): Boolean {
//        //焦点记忆
//        val position: Int =
//            if (mSelectedPosition == NO_POSITION || !mIsMemoryFocus) getFirstVisibleAndFocusablePosition() else mSelectedPosition
//        val child =
//            if (null != layoutManager) layoutManager!!.findViewByPosition(position) else null
//        if (null != child) {
//            if (null != onFocusChangeListener) {
//                onFocusChangeListener.onFocusChange(this, true)
//            }
//            return child.requestFocus(direction, previouslyFocusedRect)
//        }
//        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect)
//    }

    override fun requestChildFocus(child: View?, focused: View?) {
        super.requestChildFocus(child, focused)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
    }


    override fun swapAdapter(adapter: Adapter<*>?, removeAndRecycleExistingViews: Boolean) {
        super.swapAdapter(adapter, removeAndRecycleExistingViews)
    }
}