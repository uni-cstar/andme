package andme.tv.leanback.widget

import andme.lang.orDefault
import andme.tv.arch.R
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.animation.addListener

/**
 * Created by Lucio on 2021/2/28.
 * 闪一下动画的View
 */
class FlashView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mAnimator: ValueAnimator? = null

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        scaleType = ScaleType.CENTER_CROP
        adjustViewBounds = true
        setImageResource(R.drawable.amtv_img_flash)
        visibility = View.GONE
    }

    /**
     * 动画实现方式：让自己随着时间不断平移从而达到闪一下的效果
     */
    private fun createValueAnimator(): ValueAnimator {

        val parentWidth = (this.parent as? View)?.width.orDefault(this.width)

        return ValueAnimator.ofFloat((-parentWidth - 30).toFloat(), (parentWidth + 30).toFloat())
            .also {
                it.addUpdateListener {
                    val aFloat = it.animatedValue as Float
                    this.translationX = aFloat
                    val alpha = aFloat / parentWidth
                    val a1 = if (alpha > 0) 1 - alpha else 1 + alpha
                    val a2 = (a1 / 2 + 0.3).toFloat()
                    this.alpha = a2
                }

                it.addListener(onStart = {
                    this.visibility = View.VISIBLE
                }, onEnd = {
                    this.visibility = View.GONE
                })

                it.interpolator = AccelerateDecelerateInterpolator()

                val d = parentWidth / 355 - 1
                val ff = 1000 * (d * 0.25f + 1)
                it.duration = ff.toLong()
                it.startDelay = 150
            }
    }

    /**
     * 开始闪一下动画
     */
    fun startAnim() {
        bringToFront()

//        mAnimator?.let {
//            it.removeAllUpdateListeners()
//            it.removeAllListeners()
//            it.cancel()
//        }
        mAnimator = createValueAnimator().also {
            it.start()
        }
    }

    /**
     * 停止动画
     */
    fun stopAnim() {
        mAnimator?.cancel()
    }
}