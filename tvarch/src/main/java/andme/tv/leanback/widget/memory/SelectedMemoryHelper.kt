package andme.tv.leanback.widget.memory

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import java.util.*

/**
 * Created by Lucio on 2021/3/31.
 */
object SelectedMemoryHelper {

    /**
     * 查找第一个isSelected为true的View用于获取焦点；
     */
    @JvmStatic
    internal fun addFocusables(
        viewGroup: ViewGroup,
        views: ArrayList<View>?,
        direction: Int,
        focusableMode: Int
    ): Boolean {
        if (!viewGroup.hasFocus()) {
            val view = viewGroup.children.find {
                it.isSelected && it.visibility == View.VISIBLE
            }
            if (view != null) {
                views?.add(view)
                return views != null
            }
        }
        return false
    }
}

