package andme.tv.leanback.widget.memory

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.leanback.widget.HorizontalGridView
import java.util.*

/**
 * Created by Lucio on 2021/3/31.
 */
class SelectedMemoryHorizontalGridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HorizontalGridView(context, attrs, defStyleAttr) {

    @SuppressLint("RestrictedApi")
    override fun addFocusables(views: ArrayList<View>?, direction: Int, focusableMode: Int) {
        val focusStrategy = focusScrollStrategy
        //align模式的会自动记住焦点
        if(focusStrategy != FOCUS_SCROLL_PAGE && focusStrategy != FOCUS_SCROLL_ITEM){
            invokeSuperAddFocusables(views, direction, focusableMode)
            return
        }

        //查找第一个设置isSelected = true的view 为焦点
        if (SelectedMemoryHelper.addFocusables(this, views, direction, focusableMode))
            return
        invokeSuperAddFocusables(views, direction, focusableMode)
    }

    /**
     * 用于重写[addFocusables]但是无法调用super的情况
     */
    protected fun invokeSuperAddFocusables(views: ArrayList<View>?, direction: Int, focusableMode: Int){
        super.addFocusables(views, direction, focusableMode)
    }
//
//    init {
//        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
//    }

//    override fun focusSearch(focused: View?, direction: Int): View {
//        val manager = layoutManager
//        if (!hasFocus() && manager != null && focusViewPosition >= 0) {
//            val view = manager.findViewByPosition(focusViewPosition)
//            if (view != null) {
//                return view
//            }
//        }
//
//        return super.focusSearch(focused, direction)
//    }

    //    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
//        return super.requestFocus(direction, previouslyFocusedRect)
//    }
//
//    override fun requestChildFocus(child: View?, focused: View?) {
//        val manager = layoutManager
//        if (!hasFocus() && manager != null && focusViewPosition >= 0) {
//            val view = manager.findViewByPosition(focusViewPosition)
//            if (view != null && view != child) {
//                super.requestChildFocus(view, view)
//                return
//            }
//        }
//
//        super.requestChildFocus(child, focused)
//    }


//    override fun onRequestFocusInDescendants(
//        direction: Int,
//        previouslyFocusedRect: Rect?
//    ): Boolean {
//        val manager = layoutManager
//        if (!hasFocus() && manager != null && focusViewPosition >= 0) {
//            val view = manager.findViewByPosition(focusViewPosition)
//            if (view != null) {
//                view.requestFocus()
//                return true
//            }
//        }
//        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect)
//    }
}