package andme.tv.leanback.widget

import andme.lang.orDefault
import andme.tv.arch.R
import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView

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
        setImageResource(R.drawable.amtv_img_flash)
        visibility = View.GONE
    }

    /**
     * 获取平移的偏移量：如果当前控件宽度不为0，则为当前控件宽度，否则根据当前图片背景宽高和所在父容器大小进行计算；如果前面计算规则不可用，则默认一个固定值偏移量
     * @param parentWidth 父控件宽度
     * @param parentHeight 父控件高度
     */
    private fun getTranslationOffset(parentWidth: Int, parentHeight: Int): Float {
        var offset = this.width.toFloat()
        if (offset <= 0) {
            var drawableWidth = this.drawable?.intrinsicWidth.orDefault()
            var drawableHeight = this.drawable?.intrinsicHeight.orDefault()
            if (drawableWidth <= 0 || drawableHeight <= 0) {
                val bgDrawable = context.resources.getDrawable(R.drawable.amtv_img_flash)
                drawableWidth = bgDrawable.intrinsicWidth
                drawableHeight = bgDrawable.intrinsicHeight
            }

            if (drawableWidth <= 0 || drawableHeight <= 0) {
                return 60f
            } else {
                return drawableWidth.toFloat() / drawableHeight * parentHeight
            }
        }
        return offset
    }

    /**
     * 动画实现方式：让自己随着时间不断平移从而达到闪一下的效果
     */
    private fun createValueAnimator(parent: ViewGroup): ValueAnimator {
        val parentWidth = parent.width
        val offset = getTranslationOffset(parentWidth, parent.height)
//        println("parentWidth:$parentWidth offset:${offset} ")
//        println("f1:${(-offset).toFloat()} f2:${(parentWidth + offset).toFloat()} ")

        return ValueAnimator.ofFloat((-offset).toFloat(), (parentWidth + offset).toFloat())
            .also {
                it.addUpdateListener {
                    val aFloat = it.animatedValue as Float

                    this.translationX = aFloat
                    val alpha = aFloat / parentWidth
                    val a1 = if (alpha > 0) 1 - alpha else 1 + alpha
                    val a2 = (a1 / 2 + 0.3).toFloat()
                    this.alpha = a2.coerceAtLeast(0f).coerceAtMost(1f)
//                    println("translationX:$aFloat alpha:${a2.coerceAtLeast(0f).coerceAtMost(1f)} ")
                }

                it.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                        this@FlashView.visibility = View.VISIBLE

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        this@FlashView.visibility = View.GONE
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                    }
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
        val parent = this.parent as? ViewGroup ?: return
        bringToFront()

//        mAnimator?.let {
//            it.removeAllUpdateListeners()
//            it.removeAllListeners()
//            it.cancel()
//        }
        mAnimator = createValueAnimator(parent).also {
            it.start()
        }
    }

    /**
     * 停止动画
     */
    fun stopAnim() {
        mAnimator?.cancel()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }
}