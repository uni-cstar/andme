package andme.tv.leanback.widget.memory

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.annotation.IntDef
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.OnChildViewHolderSelectedListener
import androidx.leanback.widget.VerticalGridView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by Lucio on 2021/4/18.
 */
class GridViewMemoryHelper private constructor(val view: BaseGridView) :
    OnChildViewHolderSelectedListener() {

    /**
     * 是否启用焦点记忆
     */
    var isMemoryEnable: Boolean = true
        set(value) {
            field = value
            if (!value) {
                memoryFocusPositionInAdapter = VerticalGridView.NO_POSITION
            }
        }

    /**
     * 焦点记忆View状态
     */
    @MemoryState
    var memoryViewState:Int = MEMORY_VIEW_STATE_SELECTED

    /**
     * 焦点记忆的位置
     */
    var memoryFocusPositionInAdapter: Int = VerticalGridView.NO_POSITION

    init {
        view.addOnChildViewHolderSelectedListener(this)
    }

    private fun log(msg: String) {
        Log.d("MemoryFocus", msg)
    }


    /**
     * 焦点记忆位置观察
     */
    override fun onChildViewHolderSelected(
        parent: RecyclerView?,
        child: RecyclerView.ViewHolder?,
        position: Int,
        subposition: Int
    ) {
        super.onChildViewHolderSelected(parent, child, position, subposition)
        if (isMemoryEnable){
            memoryFocusPositionInAdapter = position
            child?.itemView?.let {
                updateMemoryViewState(it)
            }
        }
        log("当前记忆位置：$memoryFocusPositionInAdapter")
    }

    private fun updateMemoryViewState(memory:View){
        if(memoryViewState == MEMORY_VIEW_STATE_IGNORE)
            return

        if(memoryViewState == MEMORY_VIEW_STATE_SELECTED){
            MemoryHelper.setSingleSelectedInParent(memory)
            return
        }

        if(memoryViewState == MEMORY_VIEW_STATE_ACTIVE){
            MemoryHelper.setSingleActivedInParent(memory)
            return
        }
    }

    /**
     * 是否处理焦点分发：如果当前未启用焦点记忆或者当前控件已经具备焦点或者当前focus策略是[BaseGridView.FOCUS_SCROLL_ALIGNED],则不采用焦点记忆规则
     * @return true:应该处理焦点分发规则
     */
    @SuppressLint("RestrictedApi")
    private fun shouldHandleFocusDispatch():Boolean{
        return isMemoryEnable && !view.hasFocus() && view.focusScrollStrategy != VerticalGridView.FOCUS_SCROLL_ALIGNED
                && memoryFocusPositionInAdapter != RecyclerView.NO_POSITION
    }

    /**
     * 用于ViewGr
     */

    fun addFocusables(views: ArrayList<View>?, direction: Int, focusableMode: Int): Boolean {
        if (!shouldHandleFocusDispatch()) {
            log("addFocusables：不启用焦点记忆")
            return false
        }

        val viewHolder =
            view.findViewHolderForAdapterPosition(memoryFocusPositionInAdapter) ?: return false

        val pendingFocusView = viewHolder.itemView

        if (pendingFocusView.canTakeFocus) {
            log("addFocusables：焦点记忆查找成功")
            //添加焦点记忆寻找的ChildView
            views?.add(pendingFocusView)
            return true
        }
        log("addFocusables：焦点记忆查找失败")
        return false
    }


    fun onRequestFocusInDescendants(direction: Int, previouslyFocusedRect: Rect?):Boolean{
        if(!shouldHandleFocusDispatch()){
            log("onRequestFocusInDescendants：不启用焦点记忆分发")
            return false
        }

        val viewHolder =
            view.findViewHolderForAdapterPosition(memoryFocusPositionInAdapter) ?: return false

        val pendingFocusView = viewHolder.itemView

        if (pendingFocusView.canTakeFocus) {
            log("addFocusables：焦点记忆查找成功")
            //添加焦点记忆寻找的ChildView
            pendingFocusView.requestFocus(direction)
            return true
        }
        return false
    }

    companion object {

        /**
         * 不处理任何焦点记忆View的状态
         */
        const val MEMORY_VIEW_STATE_IGNORE = 0

        /**
         * 焦点记忆的view状态类型
         * 即保持当前焦点记忆的[View]设置[View.setSelected]为true，而其他view则设置为false
         */
        const val MEMORY_VIEW_STATE_SELECTED = 1

        /**
         * 焦点记忆的view状态类型
         * 即保持当前焦点记忆的[View]设置[View.setActivated]为true，而其他view则设置为false
         */
        const val MEMORY_VIEW_STATE_ACTIVE = 2

        @JvmStatic
        fun apply(view: BaseGridView): GridViewMemoryHelper {
            return GridViewMemoryHelper(view)
        }
    }

    /**
     * 焦点记忆状态类型；默认none
     */
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(value = [MEMORY_VIEW_STATE_IGNORE, MEMORY_VIEW_STATE_SELECTED, MEMORY_VIEW_STATE_ACTIVE])
    annotation class MemoryState


}