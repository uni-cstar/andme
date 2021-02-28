package andme.tv.leanback.widget

import andme.tv.AMTV
import andme.tv.arch.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use

/**
 * 获取焦点自动放大的[ConstraintLayout]
 */
class ScaleTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), View.OnFocusChangeListener {

    private lateinit var mFocusHelper: FocusHighlightHandler

    private var mEnableScale: Boolean = true

    init {
        isFocusable = true
        isClickable = true
        isFocusableInTouchMode = AMTV.isFocusableInTouchMode
        onFocusChangeListener = this

        context.obtainStyledAttributes(attrs, R.styleable.ScaleTextView).use {
            mEnableScale = it.getBoolean(R.styleable.ScaleTextView_enableScale, true)
            val zoomIndex = it.getInteger(
                R.styleable.ScaleTextView_zoomFactor,
                ZoomFactor.ZOOM_FACTOR_XSMALL
            )
            val useDimmer = it.getBoolean(R.styleable.ScaleConstraintLayout_enableDimmer, false)
            mFocusHelper = ScaleFocusHighlightHelper.createScaleFocusHandler(zoomIndex, useDimmer)
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (mEnableScale && ::mFocusHelper.isInitialized) {
            mFocusHelper.onItemFocused(v, hasFocus)
        }
    }
}