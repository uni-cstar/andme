package andme.tv.leanback.widget.memory

import andme.core.widget.isVisible
import andme.tv.arch.R
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import java.util.*

/**
 * Created by Lucio on 2021/3/31.
 */
object MemoryHelper {

    /**
     * 查找第一个用于获取焦点的拥有hasMemoryTag为true的ChildView
     */
    @JvmStatic
    fun addFocusablesByMemoryTag(
        viewGroup: ViewGroup,
        views: ArrayList<View>?,
        direction: Int,
        focusableMode: Int
    ): Boolean {
        if (viewGroup.hasFocus())
            return false

        val view = viewGroup.children.find {
            it.hasMemoryTag && it.canTakeFocus
        }
        if (view != null) {
            views?.add(view)
            return true
        }

        return false
    }

    /**
     * 设置单选的焦点记忆Tag
     */
    @JvmStatic
    fun setSingleMemoryTag(child: View): Boolean {
        val parent = child.parent as ViewGroup? ?: return false
        parent.children.forEach {
            it.hasMemoryTag = it == child
        }
        return true
    }

    /**
     * 设置child 选中状态，其他child为未选中状态
     */
    @JvmStatic
    fun setSingleSelectedInParent(child: View): Boolean {
        val parent = child.parent as ViewGroup? ?: return false
        parent.children.forEach {
            it.isSelected = it == child
        }
        return true
    }

    /**
     * 设置child激活状态，其他child为未激活状态
     */
    @JvmStatic
    fun setSingleActivedInParent(child: View): Boolean {
        val parent = child.parent as ViewGroup? ?: return false
        parent.children.forEach {
            it.isActivated = it == child
        }
        return true
    }
}

internal val View.canTakeFocus: Boolean
    get() = isFocusable && isVisible && isEnabled

/**
 * 是否包含自定义的焦点记忆Tag
 */
var View.hasMemoryTag: Boolean
    get() = this.getTag(R.id.am_focus_memory_flag) as? Boolean ?: false
    set(value) {
        this.setTag(R.id.am_focus_memory_flag, value)
    }
