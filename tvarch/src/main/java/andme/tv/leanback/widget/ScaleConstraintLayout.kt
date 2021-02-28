package andme.tv.leanback.widget

import andme.tv.AMTV
import andme.tv.arch.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use

/**
 * 获取焦点自动放大的[ConstraintLayout]
 * 备注：如果使用类似VerticalGridView+ItemBridgeAdapter组合方式，则内部绑定了同样的放大效果
 */
class ScaleConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnFocusChangeListener {

    private lateinit var mFocusHelper: FocusHighlightHandler

    private var mEnableScale: Boolean = true

    init {
        isFocusable = true
        isClickable = true
        isFocusableInTouchMode = AMTV.isFocusableInTouchMode
        clipChildren = false
        clipToPadding = false
        onFocusChangeListener = this

        context.obtainStyledAttributes(attrs, R.styleable.ScaleConstraintLayout).use {
            mEnableScale = it.getBoolean(R.styleable.ScaleConstraintLayout_enableScale, true)
            val zoomIndex = it.getInteger(
                R.styleable.ScaleConstraintLayout_zoomFactor,
                ZoomFactor.ZOOM_FACTOR_SMALL
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