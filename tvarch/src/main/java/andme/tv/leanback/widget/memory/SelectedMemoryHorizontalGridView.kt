package andme.tv.leanback.widget.memory

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.leanback.widget.HorizontalGridView
import androidx.leanback.widget.OnChildViewHolderSelectedListener
import java.util.*

/**
 * Created by Lucio on 2021/3/31.
 */
class SelectedMemoryHorizontalGridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HorizontalGridView(context, attrs, defStyleAttr) {

    val memoryHelper: GridViewMemoryHelper

    init {
        memoryHelper = GridViewMemoryHelper.apply(this)
    }

    @GridViewMemoryHelper.MemoryState
    var memoryViewState:Int get() = memoryHelper.memoryViewState
    set(value) {
        memoryHelper.memoryViewState = value
    }


    @Deprecated("请使用addOnChildViewHolderSelectedListener")
    override fun setOnChildViewHolderSelectedListener(listener: OnChildViewHolderSelectedListener?) {
        //通过该方法设置监听，会移除已设置的监听，所以需要重新添加监听
        super.setOnChildViewHolderSelectedListener(listener)
        addOnChildViewHolderSelectedListener(memoryHelper)
    }

    @SuppressLint("RestrictedApi")
    override fun addFocusables(views: ArrayList<View>?, direction: Int, focusableMode: Int) {
        if(memoryHelper.addFocusables(views, direction, focusableMode))
            return
        super.addFocusables(views, direction, focusableMode)
    }

     override fun onRequestFocusInDescendants(
        direction: Int,
        previouslyFocusedRect: Rect?
    ): Boolean {
         if(memoryHelper.onRequestFocusInDescendants(direction, previouslyFocusedRect)){
             return true
         }
         return super.onRequestFocusInDescendants(direction, previouslyFocusedRect)
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