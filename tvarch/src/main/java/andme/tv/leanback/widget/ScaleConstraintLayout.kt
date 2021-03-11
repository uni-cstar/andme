package andme.tv.leanback.widget

import andme.core.exception.tryIgnore
import andme.tv.AMTV
import andme.tv.arch.R
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * 获取焦点自动放大的[ConstraintLayout]
 * 备注：如果使用类似VerticalGridView+ItemBridgeAdapter组合方式，
 * 则内部绑定了同样的放大效果，就不适合再使用此布局作为根布局，可以考虑[FlashConstraintLayout]提供闪一下的效果
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

    //用来包mFlashView,避免动画效果溢出布局所在的rect
    private lateinit var mFlashGroup: ViewGroup

    private var mEnableScale: Boolean = true
    private var mEnableDimmer: Boolean = false
    private var mEnableFlash: Boolean = false
    private var mAutoBringToFront: Boolean = false

    private val mFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
        v?.let {
            if (mAutoBringToFront) {
                bringToFront()
            }

            tryIgnore {
                if ((mEnableScale || mEnableDimmer) && ::mFocusHelper.isInitialized) {
                    mFocusHelper.onItemFocused(v, hasFocus)
                }
            }

            tryIgnore {
                if (mEnableFlash && ::mFlashView.isInitialized) {
                    if (hasFocus) {
                        mFlashGroup.bringToFront()
                        mFlashView.startAnim()
                    } else {
                        mFlashView.stopAnim()
                    }
                }
            }
        }
    }

    init {
        val tp = context.obtainStyledAttributes(attrs, R.styleable.ScaleConstraintLayout)
        tp.let {
            mAutoBringToFront =
                it.getBoolean(R.styleable.ScaleConstraintLayout_autoBringToFront, false)
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

                //fix 处理当前view parent group设置clipChildren 为false，闪一下的view效果溢出当前布局的问题
                mFlashGroup = FrameLayout(context)
                addView(mFlashGroup, LayoutParams(0, 0).also {
                    it.topToTop = LayoutParams.PARENT_ID
                    it.bottomToBottom = LayoutParams.PARENT_ID
                    it.leftToLeft = LayoutParams.PARENT_ID
                    it.rightToRight = LayoutParams.PARENT_ID
                })

                mFlashGroup.addView(mFlashView)

//                addView(mFlashView, LayoutParams(LayoutParams.WRAP_CONTENT, 0).also {
//                    it.topToTop = LayoutParams.PARENT_ID
//                    it.bottomToBottom = LayoutParams.PARENT_ID
//                    it.leftToLeft = LayoutParams.PARENT_ID
//                })
            }
        }
        tp.recycle()

        isFocusable = true
        isClickable = true
        isFocusableInTouchMode = AMTV.isFocusableInTouchMode
//        clipChildren = false
//        clipToPadding = false
        onFocusChangeListener = mFocusChangeListener
    }

}