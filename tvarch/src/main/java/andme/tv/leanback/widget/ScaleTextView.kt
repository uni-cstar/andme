package andme.tv.leanback.widget

import andme.tv.AMTV
import andme.tv.arch.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout

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

    private var mCustomFocusChangedListener :View.OnFocusChangeListener? = null

    init {
        isFocusable = true
        isClickable = true
        isFocusableInTouchMode = AMTV.isFocusableInTouchMode
        super.setOnFocusChangeListener(this)

        val tp = context.obtainStyledAttributes(attrs, R.styleable.ScaleTextView)
        tp.also {
            mEnableScale = it.getBoolean(R.styleable.ScaleTextView_enableScale, true)
            val zoomIndex = it.getInteger(
                R.styleable.ScaleTextView_zoomFactor,
                ZoomFactor.ZOOM_FACTOR_XSMALL
            )
            val useDimmer = it.getBoolean(R.styleable.ScaleConstraintLayout_enableDimmer, false)
            mFocusHelper = ScaleFocusHighlightHelper.createScaleFocusHandler(zoomIndex, useDimmer)
        }
        tp.recycle()
    }

    /**
     * Register a callback to be invoked when focus of this view changed.
     *
     * @param l The callback that will run.
     */
    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        mCustomFocusChangedListener = l
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (mEnableScale && ::mFocusHelper.isInitialized) {
            mFocusHelper.onItemFocused(v, hasFocus)
        }
        mCustomFocusChangedListener?.onFocusChange(v,hasFocus)
    }
}