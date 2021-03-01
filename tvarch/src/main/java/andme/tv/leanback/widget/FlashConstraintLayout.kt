package andme.tv.leanback.widget

import andme.tv.AMTV
import android.content.Context
import android.util.AttributeSet
import android.view.View.OnFocusChangeListener
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * 获取焦点之后闪一下效果的[ConstraintLayout]
 */
class FlashConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    //闪动效果的view
    private lateinit var mFlashView: FlashView

    private val mFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
        v?.let {
            if (hasFocus) {
                mFlashView.startAnim()
            } else {
                mFlashView.stopAnim()
            }
        }
    }

    init {
        mFlashView = FlashView(context)
        addView(mFlashView)
        isFocusable = true
        isClickable = true
        isFocusableInTouchMode = AMTV.isFocusableInTouchMode
        onFocusChangeListener = mFocusChangeListener
    }

}