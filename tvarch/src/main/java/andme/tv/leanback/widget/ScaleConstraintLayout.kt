package andme.tv.leanback.widget

import andme.core.exception.tryIgnore
import andme.tv.AMTV
import andme.tv.arch.R
import android.content.Context
import android.util.AttributeSet
import android.view.View.OnFocusChangeListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use

/**
 * 获取焦点自动放大的[ConstraintLayout]
 * 备注：如果使用类似VerticalGridView+ItemBridgeAdapter组合方式，则内部绑定了同样的放大效果
 *
 * 注意事项：闪一下效果是通过add的一个view实现的，因此如果跟该布局设置了padding，则将缩小闪一下的显示范围
 */
class ScaleConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var mFocusHelper: FocusHighlightHandler

    //闪动效果的view
    private lateinit var mFlashView: FlashView

    private var mEnableScale: Boolean = true
    private var mEnableDimmer: Boolean = false
    private var mEnableFlash: Boolean = false

    private val mFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
        v?.let {
            tryIgnore {
                if ((mEnableScale || mEnableDimmer) && ::mFocusHelper.isInitialized) {
                    mFocusHelper.onItemFocused(v, hasFocus)
                }
            }

            tryIgnore {
                if (mEnableFlash && ::mFlashView.isInitialized) {
                    if (hasFocus) {
                        mFlashView.startAnim()
                    } else {
                        mFlashView.stopAnim()
                    }
                }
            }
        }
    }

    init {

        context.obtainStyledAttributes(attrs, R.styleable.ScaleConstraintLayout).use {
            mEnableScale = it.getBoolean(R.styleable.ScaleConstraintLayout_enableScale, true)
            var zoomIndex = it.getInteger(
                R.styleable.ScaleConstraintLayout_zoomFactor,
                ZoomFactor.ZOOM_FACTOR_SMALL
            )
            if (!mEnableScale) {
                zoomIndex = ZoomFactor.ZOOM_FACTOR_NONE
            }
            mEnableDimmer = it.getBoolean(R.styleable.ScaleConstraintLayout_enableDimmer, false)

            if (mEnableScale || mEnableDimmer)
                mFocusHelper =
                    ScaleFocusHighlightHelper.createScaleFocusHandler(zoomIndex, mEnableDimmer)

            mEnableFlash = it.getBoolean(R.styleable.ScaleConstraintLayout_enableFlash, false)
            if (mEnableFlash) {
                mFlashView = FlashView(context)
                addView(mFlashView, LayoutParams(LayoutParams.WRAP_CONTENT, 0).also {
                    it.topToTop = LayoutParams.PARENT_ID
                    it.bottomToBottom = LayoutParams.PARENT_ID
                    it.leftToLeft = LayoutParams.PARENT_ID
                })
            }
        }

        isFocusable = true
        isClickable = true
        isFocusableInTouchMode = AMTV.isFocusableInTouchMode
//        clipChildren = false
//        clipToPadding = false
        onFocusChangeListener = mFocusChangeListener
    }

}