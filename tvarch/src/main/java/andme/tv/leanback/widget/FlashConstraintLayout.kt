package andme.tv.leanback.widget

import andme.tv.AMTV
import andme.tv.arch.R
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener

/**
 * 获取焦点之后闪一下效果的[ConstraintLayout]
 */
class FlashConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mAnimator: ValueAnimator? = null

    //闪动效果的view
    private lateinit var mFlashView: ImageView

    private val mFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
        v?.let {
            if (hasFocus) {
                move(mFlashView)
            } else {
                remove()
            }
        }
    }

    init {
        mFlashView = ImageView(context).also {
            it.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            it.scaleType = ImageView.ScaleType.FIT_XY
            it.setImageResource(R.drawable.amtv_img_flash)
            it.visibility = View.GONE
        }
        super.addView(mFlashView)

        isFocusable = true
        isClickable = true
        isFocusableInTouchMode = AMTV.isFocusableInTouchMode
        onFocusChangeListener = mFocusChangeListener
    }

    /**
     * 动画实现方式：让[mFlashView]随着时间不断平移从而达到闪一下的效果
     */
    private fun createValueAnimator(): ValueAnimator {
        val width = this.width
        return ValueAnimator.ofFloat((-width - 30).toFloat(), (width + 30).toFloat())
            .also {
                it.addUpdateListener {
                    val aFloat = it.animatedValue as Float
                    mFlashView.translationX = aFloat
                    val alpha = aFloat / width
                    val a1 = if (alpha > 0) 1 - alpha else 1 + alpha
                    val a2 = (a1 / 2 + 0.3).toFloat()
                    mFlashView.alpha = a2
                }

                it.addListener(onStart = {
                    mFlashView.visibility = View.VISIBLE
                }, onEnd = {
                    mFlashView.visibility = View.GONE
                })

                it.interpolator = AccelerateDecelerateInterpolator()

                val d = width / 355 - 1
                val ff = 1000 * (d * 0.25f + 1)
                it.duration = ff.toLong()
                it.startDelay = 150
            }
    }

    private fun move(view: View) {
        view.bringToFront()
        mAnimator?.cancel()
        mAnimator = createValueAnimator().also {
            it.start()
        }
    }

    private fun remove() {
        mAnimator?.cancel()
    }

}