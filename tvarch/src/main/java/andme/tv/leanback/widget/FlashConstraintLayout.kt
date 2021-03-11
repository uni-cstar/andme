package andme.tv.leanback.widget

import andme.tv.AMTV
import android.content.Context
import android.util.AttributeSet
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.FrameLayout
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

    //用来包mFlashView,避免动画效果溢出布局所在的rect
    private lateinit var mFlashGroup: ViewGroup


    private val mFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
        v?.let {
            if (hasFocus) {
                mFlashGroup.bringToFront()
                mFlashView.startAnim()
            } else {
                mFlashView.stopAnim()
            }
        }
    }

    init {
        mFlashView = FlashView(context)

        //fix 处理当前view parent group设置clipChildren 为false，闪一下的view效果溢出当前布局的问题
        mFlashGroup = FrameLayout(context)
        addView(mFlashGroup, LayoutParams(0, 0).also {
            it.topToTop = LayoutParams.PARENT_ID
            it.bottomToBottom = LayoutParams.PARENT_ID
            it.leftToLeft = LayoutParams.PARENT_ID
            it.rightToRight = LayoutParams.PARENT_ID
        })

        mFlashGroup.addView(mFlashView)

        isFocusable = true
        isClickable = true
        isFocusableInTouchMode = AMTV.isFocusableInTouchMode
        onFocusChangeListener = mFocusChangeListener
    }

}