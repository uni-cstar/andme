package andme.core.widget.progressbar

import andme.core.R
import andme.core.util.dip
import andme.core.util.sp
import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.FontMetricsInt
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.content.res.ResourcesCompat
import kotlin.math.ceil

/**
 * Created by Lucio on 2021/4/5.
 * 带浮动标记的ProgressBar
 * 具体效果查看doc/screencast/floatindicator_progressbar.png
 * 高度设置为wrap_content即可
 */
class FloatStatableSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /*bar背景色*/
    private var mBarColor: Int = 0

    //进度条高度
    private val mBarHeight: Int

    //进度条圆角半径：默认为高度的一半
    private var mBarRoundedRadius: Float = 0f

    /*进度条画笔*/
    private val mBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBarRectF = RectF()

    /*一级进度*/
    @IntRange(from = 0, to = 100)
    private var mProgress: Int = 0

    /*一级进度颜色*/
    private var mProgressColor: Int = 0

    /*二级进度*/
    @IntRange(from = 0, to = 100)
    private var mSecondaryProgress: Int = 0

    /*二级进度颜色*/
    private var mSecondaryProgressColor: Int = 0

    /*指示器颜色*/
    private var mIndicatorColor: Int = 0

    /*指示器半径*/
    private var mIndicatorRadius: Float = 0f

    /*指示器 内圆颜色*/
    private var mIndicatorInnerColor: Int = 0

    /*指示器 内圆半径*/
    private var mIndicatorInnerRadius: Int = 0

    /*二级指示器*/
    private var mSecondaryIndicatorDrawable: Drawable
    private var mSecondaryIndicatorRect: Rect
    private var mSecondaryIndicatorSize: Int

    /*二级指示器的缩放倍数*/
    @FloatRange(from = 0.0, to = 1.0)
    private var mSecondaryIndicatorScale: Float = 1f

    /**
     * 是否绘制二级指示器，如果绘制二级指示器，则不绘制一级指示器
     */
    private var mDrawSecondaryIndicator: Boolean = false

    private val mSecondaryIndicatorShowAnim: ValueAnimator
    private var mSecondaryIndicatorShowAnimRunning: Boolean = false
    private val mSecondaryIndicatorHideAnim: ValueAnimator
    private var mSecondaryIndicatorHideAnimRunning: Boolean = false


    //指示器背景色
    private var mFloatColor: Int = 0
    private val mFloatBgRectF = RectF()

    /*进度条与指示器之间的空白间距*/
    private val mFloatMargin: Int

    /*三角形高度*/
    private val mFloatTriangleHeight: Int
    private val mFloatTriangleWidth: Int

    /*三角形绘制path*/
    private val mFloatTrianglePath: Path = Path()

    /*指示器背景圆角*/
    private var mFloatRoundedRadius: Int = 0

    /*指示文本*/
    private var mFloatText: String? = null

    //指示器文字大小
    private var mFloatTextSize: Int = 0

    //指示器文字颜色
    private var mFloatTextColor: Int = 0

    //用于测量文字显示区域的宽度和高度
    private lateinit var mFloatTextFontMetrics: FontMetricsInt
    private val mFloatTextRect: Rect = Rect()

    /*文字画笔*/
    private var mFloatTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mFloatPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /*float文本的水平间距*/
    private val mFloatTextHorizontalPadding: Int

    /*float文本的垂直间距*/
    private val mFloatTextVerticalPadding: Int

    /*float的透明度*/
    @IntRange(from = 0, to = 255)
    private var mFloatPaintAlpha = 255

    /*是否绘制float*/
    private var mDrawFloat: Boolean = false

    /*是否正在显执行float显示动画*/
    private var mFloatInAnimRunning: Boolean = false

    /*是否正在执行float隐藏动画*/
    private var mFloatOutAnimRunning: Boolean = false

    /*float显示动画*/
    private val mFloatFadeInAnim = ValueAnimator.ofInt(0, 255).setDuration(1000)

    /*float隐藏动画*/
    private val mFloatFadeOutAnim = ValueAnimator.ofInt(255, 0).setDuration(1000)

    /**
     * 是否是裁剪模式，默认为裁剪模式；即进度条的高度会计算[mSecondaryIndicatorDrawable]指示器的高度,否则不计算，则Parent需要设置android:clipChildren = false
     */
    var isClipMode: Boolean = true

    init {

        val dip4 = dip(4)
        val dip2 = dip4 / 2
        val dip5 = dip(5)

        val a = context.obtainStyledAttributes(
            attrs, R.styleable.FloatStatableSeekBar, defStyleAttr, 0
        )
        mBarColor = a.getColor(R.styleable.FloatStatableSeekBar_fssb_barColor, Color.LTGRAY)
        mBarHeight = a.getDimensionPixelSize(R.styleable.FloatStatableSeekBar_fssb_barHeight, dip4)
        mBarRoundedRadius = ceil(mBarHeight.toFloat() / 2)
        mProgress = a.getInt(R.styleable.FloatStatableSeekBar_fssb_progress, 100)
        mProgressColor =
            a.getColor(R.styleable.FloatStatableSeekBar_fssb_progressColor, Color.DKGRAY)
        mSecondaryProgress = a.getInt(R.styleable.FloatStatableSeekBar_fssb_secondaryProgress, 0)
        mSecondaryProgressColor =
            a.getColor(R.styleable.FloatStatableSeekBar_fssb_progressColor, Color.GRAY)

        mIndicatorColor = a.getColor(
            R.styleable.FloatStatableSeekBar_fssb_indicatorColor,
            Color.BLUE
        )
        mIndicatorRadius = a.getDimensionPixelSize(
            R.styleable.FloatStatableSeekBar_fssb_indicatorRadius,
            dip5
        ).toFloat()

        mIndicatorInnerColor = a.getColor(
            R.styleable.FloatStatableSeekBar_fssb_indicatorInnerColor,
            Color.RED
        )
        mIndicatorInnerRadius = a.getDimensionPixelSize(
            R.styleable.FloatStatableSeekBar_fssb_indicatorInnerRadius,
            mBarRoundedRadius.toInt()
        )

        mSecondaryIndicatorDrawable =
            ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.amtv_media_video_play_ic_in_bar,
                context.theme
            )!!


        mSecondaryIndicatorSize = a.getDimensionPixelSize(
            R.styleable.FloatStatableSeekBar_fssb_secondaryIndicatorSize,
            dip4 * 6
        )

        mSecondaryIndicatorRect = Rect()

        mFloatColor = a.getColor(
            R.styleable.FloatStatableSeekBar_fssb_floatColor,
            Color.DKGRAY
        )

        mFloatMargin = a.getDimensionPixelSize(
            R.styleable.FloatStatableSeekBar_fssb_floatMarginSize, dip2
        )

        mFloatTriangleWidth = a.getDimensionPixelSize(
            R.styleable.FloatStatableSeekBar_fssb_floatTriangleWidth, dip5
        )
        mFloatTriangleHeight = a.getDimensionPixelSize(
            R.styleable.FloatStatableSeekBar_fssb_floatTriangleHeight, dip2 * 3
        )

        mFloatRoundedRadius = a.getDimensionPixelSize(
            R.styleable.FloatStatableSeekBar_fssb_floatRoundedRadius, dip5
        )

        if (a.hasValue(R.styleable.FloatStatableSeekBar_fssb_floatText))
            mFloatText = a.getString(R.styleable.FloatStatableSeekBar_fssb_floatText)

        mFloatTextSize = a.getDimensionPixelSize(
            R.styleable.FloatStatableSeekBar_fssb_floatTextSize,
            sp(14)
        )
        mFloatTextColor = a.getColor(
            R.styleable.FloatStatableSeekBar_fssb_floatTextColor,
            Color.WHITE
        )

        mFloatTextHorizontalPadding = a.getDimensionPixelSize(
            R.styleable.FloatStatableSeekBar_fssb_floatTextHorizontalPadding,
            dip4
        )
        mFloatTextVerticalPadding = a.getDimensionPixelSize(
            R.styleable.FloatStatableSeekBar_fssb_floatTextVerticalPadding,
            dip2
        )
        a.recycle()
        setup()

        if (isInEditMode) {
            mFloatText = "10:11:234"
            mDrawFloat = true
            isClipMode = false
        }


        mFloatFadeInAnim.addUpdateListener {
            mFloatPaintAlpha = it.animatedValue as Int
            doInvalidateAndCancelPrevious()
        }

        mFloatFadeInAnim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                mDrawFloat = true
            }

            override fun onAnimationEnd(animation: Animator?) {
                mFloatInAnimRunning = false
            }

            override fun onAnimationCancel(animation: Animator?) {
                mFloatInAnimRunning = false
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })

        mFloatFadeOutAnim.addUpdateListener {
            mFloatPaintAlpha = it.animatedValue as Int
            doInvalidateAndCancelPrevious()
        }

        mFloatFadeOutAnim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                mDrawFloat = false
                mFloatOutAnimRunning = false
            }

            override fun onAnimationCancel(animation: Animator?) {
                mFloatOutAnimRunning = false
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })

        val startScale = (mIndicatorRadius * 2f) / mSecondaryIndicatorSize
        mSecondaryIndicatorShowAnim = ValueAnimator.ofFloat(startScale, 1f)
        mSecondaryIndicatorShowAnim.addUpdateListener {
            mSecondaryIndicatorScale = it.animatedValue as Float
            doInvalidateAndCancelPrevious()
        }
        mSecondaryIndicatorShowAnim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                mDrawSecondaryIndicator = true
            }

            override fun onAnimationEnd(animation: Animator?) {
                mSecondaryIndicatorShowAnimRunning = false
            }

            override fun onAnimationCancel(animation: Animator?) {
                mSecondaryIndicatorShowAnimRunning = false
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })

        mSecondaryIndicatorHideAnim = ValueAnimator.ofFloat(1f, startScale)
        mSecondaryIndicatorHideAnim.addUpdateListener {
            mSecondaryIndicatorScale = it.animatedValue as Float
            doInvalidateAndCancelPrevious()
        }

        mSecondaryIndicatorHideAnim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                mDrawSecondaryIndicator = false
                doInvalidateAndCancelPrevious()
            }

            override fun onAnimationCancel(animation: Animator?) {
                mSecondaryIndicatorHideAnimRunning = false
            }

            override fun onAnimationRepeat(animation: Animator?) {
                mSecondaryIndicatorHideAnimRunning = false
            }

        })
    }

    private val mInvalidateStrategy = object : Runnable {
        override fun run() {
            removeCallbacks(this)
            invalidate()
        }
    }

    /**
     * 执行重绘，并取消待重绘的请求
     */
    private fun doInvalidateAndCancelPrevious() {
        postDelayed(mInvalidateStrategy, 0)
    }

    private fun setup() {
        mFloatTextPaint.color = mFloatTextColor
        mFloatTextPaint.textSize = mFloatTextSize.toFloat()
        //文字居中对齐
        mFloatTextPaint.textAlign = Paint.Align.CENTER
        mFloatTextFontMetrics = mFloatTextPaint.fontMetricsInt

        mBarPaint.style = Paint.Style.FILL
    }

    //指示器高度
    private val indicatorHeight: Int get() = if (isClipMode) (mIndicatorRadius * 2).toInt() else mSecondaryIndicatorSize

    //指示器比进度条高的部分
    val indicatorThanBarHeight get() = (indicatorHeight - mBarHeight) / 2f

    //bar 横坐标偏移位置
    private val barOffsetX get() = mIndicatorRadius

    //进度条宽度
    val barWidth: Float
        get() = width - barOffsetX * 2

    private val barRectLeft get() = drawRectLeft + barOffsetX

    private val barRectBottom get() = drawRectBottom - indicatorThanBarHeight


    private val drawRectLeft get() = paddingLeft
    private val drawRectTop get() = paddingTop
    private val drawRectRight get() = width - paddingRight
    private val drawRectBottom get() = height - paddingBottom

    /**
     * @param progress 进度
     * @param floatText 提示文本；为空则不显示
     */
    @JvmOverloads
    fun setProgress(
        @IntRange(from = 0, to = 100) progress: Int,
        floatText: String? = mFloatText
    ) {
        setProgress(progress, mSecondaryProgress, floatText)
    }

    fun setProgress(
        @IntRange(from = 0, to = 100) progress: Int,
        @IntRange(from = 0, to = 100) secondaryProgress: Int,
        floatText: String?
    ) {
        setProgressOnly(progress, secondaryProgress, floatText)
        doInvalidateAndCancelPrevious()
    }

    /**
     * 只更改进度值，不进行渲染
     */
    fun setProgressOnly(
        @IntRange(from = 0, to = 100) progress: Int,
        @IntRange(from = 0, to = 100) secondaryProgress: Int,
        floatText: String?
    ) {
        mProgress = progress
        mSecondaryProgress = secondaryProgress
        mFloatText = floatText
    }

    /**
     * 淡出Float
     */
    fun showFloat() {
        if (mFloatOutAnimRunning)
            return
        mFloatOutAnimRunning = true
        if (mFloatFadeInAnim.isRunning || mFloatFadeInAnim.isStarted) {
            mFloatFadeInAnim.cancel()
        }
        mFloatFadeOutAnim.start()
    }

    /**
     * 淡入Float;如果正在进行淡入动画，或者已经在绘制状态，则不处理
     */
    fun hideFloat() {
        if (mFloatInAnimRunning || mDrawFloat)
            return
        mFloatInAnimRunning = true
        if (mFloatFadeOutAnim.isRunning || mFloatFadeOutAnim.isStarted) {
            mFloatFadeOutAnim.cancel()
        }
        mFloatFadeInAnim.start()
    }

    fun showFirstIndicatorWithAnim() {
        if (mSecondaryIndicatorHideAnimRunning || !mDrawSecondaryIndicator)
            return
        mSecondaryIndicatorHideAnimRunning = true
        if (mSecondaryIndicatorShowAnim.isStarted || mSecondaryIndicatorShowAnim.isRunning) {
            mSecondaryIndicatorShowAnim.cancel()
        }
        mSecondaryIndicatorHideAnim.start()
    }

    /**
     * 显示二级指示器
     */
    fun showSecondaryIndicatorWithAnim() {
        if (mSecondaryIndicatorShowAnimRunning || mDrawSecondaryIndicator)
            return
        mSecondaryIndicatorShowAnimRunning = true
        if (mSecondaryIndicatorHideAnim.isStarted || mSecondaryIndicatorHideAnim.isRunning) {
            mSecondaryIndicatorHideAnim.cancel()
        }
        mSecondaryIndicatorShowAnim.start()
    }

    fun showFirstIndicator(){
        mSecondaryIndicatorShowAnim.cancel()
        mSecondaryIndicatorHideAnim.cancel()
        mDrawSecondaryIndicator = false
    }

    fun showSecondaryIndicator(){
        mSecondaryIndicatorShowAnim.cancel()
        mSecondaryIndicatorHideAnim.cancel()
        mDrawSecondaryIndicator = true
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //上padding
        var height = paddingTop

        //float 高度 =  文字高度+文字上下padding+三角形高度+与bar之间的间隙
        height += (mFloatTextFontMetrics.bottom - mFloatTextFontMetrics.top + mFloatTextVerticalPadding * 2) + mFloatTriangleHeight + mFloatMargin

        //进度条高度
        height += indicatorHeight

        //下padding
        height += paddingBottom

        setMeasuredDimension(measuredWidth, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制原则：绘制bar，从下往上计算位置
        //绘制float，从上往下计算位置
        drawProgressBar(canvas)
        drawIndicator(canvas)
        drawFloat(canvas)
    }

    /**
     * 根据进度计算进度条x坐标
     */
    private fun calculateBarProgressX(progress: Int): Float {
        return barWidth * (progress / 100f) + barOffsetX
    }

    /**
     * 绘制的bar在左右留出了半个indicator指示器的宽度，因此其他地方计算要注意这一点
     */
    private fun drawProgressBar(canvas: Canvas) {
        val barWidth = barWidth
        val left = barRectLeft
        val right = left + barWidth
        val bottom = barRectBottom
        val top = bottom - mBarHeight

        //绘制背景
        mBarPaint.color = mBarColor
        mBarRectF.set(left, top, right, bottom)
        canvas.drawRoundRect(mBarRectF, mBarRoundedRadius, mBarRoundedRadius, mBarPaint)

        //二级进度
        if (mSecondaryProgress > 0) {
            mBarPaint.color = mSecondaryProgressColor
            mBarRectF.right = calculateBarProgressX(mSecondaryProgress)
            canvas.drawRoundRect(mBarRectF, mBarRoundedRadius, mBarRoundedRadius, mBarPaint)
        }

        //一级进度
        if (mProgress > 0) {
            mBarPaint.color = mProgressColor
            mBarRectF.right = calculateBarProgressX(mProgress)
            canvas.drawRoundRect(mBarRectF, mBarRoundedRadius, mBarRoundedRadius, mBarPaint)
        }
    }

    private fun drawIndicator(canvas: Canvas) {
        val x = calculateBarProgressX(mProgress)
        val y = barRectBottom - mBarHeight / 2f

        if (mDrawSecondaryIndicator) {
            if (mSecondaryIndicatorScale > 0) {
                //计算缩放之后的大小
                val size = mSecondaryIndicatorSize * mSecondaryIndicatorScale
                val half = size / 2
                mSecondaryIndicatorRect.set(
                    (x - half).toInt(), (y - half).toInt(), (x + half).toInt(),
                    (y + half).toInt()
                )
                mSecondaryIndicatorDrawable.bounds = mSecondaryIndicatorRect
                mSecondaryIndicatorDrawable.draw(canvas)
            }
        } else {
            mBarPaint.color = mIndicatorColor
            //外圆
            canvas.drawCircle(x, y, mIndicatorRadius, mBarPaint)

            //内圆
            mBarPaint.color = mIndicatorInnerColor
            canvas.drawCircle(
                x,
                y,
                mIndicatorInnerRadius.toFloat(),
                mBarPaint
            )
        }


    }


    /**
     * 绘制float，从上往下计算位置
     */
    private fun drawFloat(canvas: Canvas) {
        //文本不为空，绘制指示器文本
        val text = mFloatText
        if (!mDrawFloat || text.isNullOrEmpty())
            return


        val progressX = calculateBarProgressX(mProgress)
        mFloatPaint.color = mFloatColor
        mFloatPaint.alpha = mFloatPaintAlpha
        mFloatTextPaint.alpha = mFloatPaintAlpha
        mFloatTextPaint.color = mFloatTextColor

        mFloatTextPaint.getTextBounds(text, 0, text.length, mFloatTextRect)
        val textWidth: Int = mFloatTextRect.width()//文本宽度
        val textHeight = mFloatTextFontMetrics.bottom - mFloatTextFontMetrics.top//文本高度
        val textRectWidth = textWidth + mFloatTextHorizontalPadding * 2f
        val textRectHeight = textHeight + mFloatTextVerticalPadding * 2f

        val top = drawRectTop.toFloat()
        var left = progressX - textRectWidth / 2f
        var right = left + textRectWidth
        if (left < drawRectLeft) {
            left = drawRectLeft.toFloat()
            right = textRectWidth
        } else if (right > drawRectRight) {
            right = drawRectRight.toFloat()
            left = right - textRectWidth
        }
        //绘制文本背景:文本框的背景定位是根据整个控件的宽度来定位的
        mFloatBgRectF.set(left, top, right, top + textRectHeight)
        canvas.drawRoundRect(
            mFloatBgRectF,
            mFloatRoundedRadius.toFloat(), mFloatRoundedRadius.toFloat(), mFloatPaint
        )

        //绘制文字
        val distance =
            (mFloatTextFontMetrics.descent - mFloatTextFontMetrics.ascent) / 2 - mFloatTextFontMetrics.bottom
        val baseline: Float = mFloatBgRectF.centerY() + distance
        canvas.drawText(text, mFloatBgRectF.centerX(), baseline, mFloatTextPaint)

        //绘制的三角形高度实际增加了文本框的半个下边padding，预防左右临界区域三角形与文本框之间有缝隙
        val triangleOffsetY =  mFloatTextVerticalPadding / 2f

        var leftPointX = progressX - mFloatTriangleWidth / 2f
        var leftPointY = mFloatBgRectF.bottom  - triangleOffsetY
        var rightPointX = progressX + mFloatTriangleWidth / 2
        var rightPointY = leftPointY
        if (leftPointX < drawRectLeft + mFloatRoundedRadius) {
            //超出边界了
            leftPointX = drawRectLeft.toFloat()
            //y坐标向上偏移一个圆角半径，避免y坐标刚好在圆角范围内，导致没有封口的问题
            leftPointY -= mFloatRoundedRadius
        }

        if (rightPointX > drawRectRight - mFloatRoundedRadius) {
            rightPointX = drawRectRight.toFloat()
            //y坐标向上偏移一个圆角半径，避免y坐标刚好在圆角范围内，导致没有封口的问题
            rightPointY -= mFloatRoundedRadius
        }

        //绘制箭头
        mFloatTrianglePath.reset()
        //移动到三角形顶点：三角形的顶点是在指示器的中间，所以在两端要考虑顶点的问题
        mFloatTrianglePath.moveTo(progressX, mFloatBgRectF.bottom + mFloatTriangleHeight)

        mFloatTrianglePath.lineTo(rightPointX, rightPointY)
        mFloatTrianglePath.lineTo(leftPointX, leftPointY)
        mFloatTrianglePath.close()
        canvas.drawPath(mFloatTrianglePath, mFloatPaint)
    }
}