package andme.tv.leanback.widget.focusborder

import andme.tv.arch.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by Lucio on 2021/2/28.
 * 圆角边框效果
 */
class RoundedRectFocusBorderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        setBackgroundResource(R.drawable.amtv_focus_border_rounded_slt)
        isDuplicateParentStateEnabled = true
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}