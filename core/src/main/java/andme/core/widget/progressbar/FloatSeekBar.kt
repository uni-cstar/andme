package andme.core.widget.progressbar

import andme.core.R
import andme.core.util.dip
import andme.core.util.sp
import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.FontMetricsInt
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import kotlin.math.ceil

/**
 * Created by Lucio on 2021/4/5.
 * 带浮动标记的ProgressBar
 * 具体效果查看doc/screencast/floatindicator_progressbar.png
 * 高度设置为wrap_content即可
 */
class FloatSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * 当前进度
     */
    private var mProgress: Int = 0

    //进度条的底色和完成进度的颜色
    private var mBgColor: Int = 0
    private var mFgColor: Int = 0

    //进度条高度
    private val mBarHeight: Int


    /*指示器颜色*/
    private var mIndicatorColor: Int = 0

    /*指示器半径*/
    private var mIndicatorRadius: Float = 0f

    /*指示器 内圆颜色*/
    private var mIndicatorInnerColor: Int = 0

    /*指示器 内圆半径*/
    private var mIndicatorInnerRadius: Int = 0

    /*进度条与指示器之间的空白间距*/
    private val mMarginSize: Int

    /*三角形高度*/
    private val mTriangleHeight: Int
    private val mTriangleWidth: Int
    private val mTrianglePath: Path = Path()

    //指示器文字大小
    private var mFloatTextSize: Int = 0

    //指示器文字颜色
    private var mFloatTextColor: Int = 0

    //指示器背景色
    private var mFloatBgColor: Int = 0

    /*指示器背景圆角*/
    private var mFloatBgRoundedRadius: Int = 0

    //绘制进度条圆角矩形的圆角
    private var mRoundedRadius: Float = 0f

    //用于测量文字显示区域的宽度和高度
    private lateinit var mTextFontMetrics: FontMetricsInt
    private val mTextRect: Rect = Rect()

    /*进度条画笔*/
    private val mBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBarRectF = RectF()

    /*文字画笔*/
    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 指示文本
     */
    private var mFloatText: String? = null

    private val mFloatTextHorizontalPadding: Int
    private val mFloatTextVerticalPadding: Int


    private var mFloatPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    @IntRange(from = 0, to = 255)
    private var mFloatPaintAlpha = 0
    private var mDrawFloat: Boolean = false

    private var mFloatInAnimRunning:Boolean = false
    private var mFloatOutAnimRunning:Boolean = false
    private val mFloatFadeInAnim = ValueAnimator.ofInt(0, 255).setDuration(300)
    private val mFloatFadeOutAnim = ValueAnimator.ofInt(255, 0).setDuration(300)


//    private var mIndicatorPositionChangedListener:OnIndicatorPositionChangedListener? = null

    init {

        mFloatTextHorizontalPadding = dip(4)
        mFloatTextVerticalPadding = dip(2)
        mFloatBgRoundedRadius = dip(5)
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.FloatSeekBar, defStyleAttr, 0
        )

        mProgress = a.getInt(R.styleable.FloatSeekBar_fsb_progress, 100)
        mBgColor = a.getColor(R.styleable.FloatSeekBar_fsb_bgColor, Color.LTGRAY)
        mFgColor = a.getColor(R.styleable.FloatSeekBar_fsb_fgColor, Color.DKGRAY)
        mBarHeight = a.getDimensionPixelSize(
            R.styleable.FloatSeekBar_fsb_progressBarHeight, dip(
                4
            )
        )
        mMarginSize = a.getDimensionPixelSize(
            R.styleable.FloatSeekBar_fsb_marginSize, dip(
                2
            )
        )

        mIndicatorColor = a.getColor(
            R.styleable.FloatSeekBar_fsb_indicatorColor,
            Color.BLUE
        )
        mIndicatorRadius = a.getDimensionPixelSize(
            R.styleable.FloatSeekBar_fsb_indicatorRadius,
            dip(5)
        ).toFloat()

        mIndicatorInnerColor = a.getColor(
            R.styleable.FloatSeekBar_fsb_indicatorInnerColor,
            Color.RED
        )
        mIndicatorInnerRadius = a.getDimensionPixelSize(
            R.styleable.FloatSeekBar_fsb_indicatorInnerRadius,
            mBarHeight / 2
        )

        mTriangleHeight = a.getDimensionPixelSize(
            R.styleable.FloatSeekBar_fsb_triangleHeight, dip(
                6
            )
        )

        mTriangleWidth = a.getDimensionPixelSize(
            R.styleable.FloatSeekBar_fsb_triangleWidth, dip(5)
        )

        mFloatTextSize = a.getDimensionPixelSize(
            R.styleable.FloatSeekBar_fsb_floatTextSize,
            sp(14)
        )
        mFloatTextColor = a.getColor(
            R.styleable.FloatSeekBar_fsb_floatTextColor,
            Color.WHITE
        )
        if (a.hasValue(R.styleable.FloatSeekBar_fsb_floatText))
            mFloatText = a.getString(R.styleable.FloatSeekBar_fsb_floatText)
        mFloatBgColor = a.getColor(
            R.styleable.FloatSeekBar_fsb_floatBgColor,
            Color.DKGRAY
        )

        mRoundedRadius = ceil(mBarHeight.toFloat() / 2)
        a.recycle()
        setup()

        mFloatText = "10:11:234"

        mFloatFadeInAnim.addUpdateListener {
            mFloatPaintAlpha = it.animatedValue as Int
            postInvalidate()
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
            postInvalidate()
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
    }

    private fun setup() {
        mTextPaint.color = mFloatTextColor
        mTextPaint.textSize = mFloatTextSize.toFloat()
        //文字居中对齐
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextFontMetrics = mTextPaint.fontMetricsInt

        mBarPaint.style = Paint.Style.FILL
//        mProgressPaint.setStrokeWidth(mProgressBarHeight.toFloat())
    }

    /**
     * @param progress 进度
     * @param floatText 提示文本；为空则不显示
     */
    fun setProgress(@IntRange(from = 0, to = 100) progress: Int, floatText: String?) {
        setProgressOnly(progress, floatText)
        postInvalidate()
    }

    fun setProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float, floatText: String?) {
        setProgress((progress * 100).toInt(), floatText)
    }

    /**
     * 只更改进度值，不进行渲染
     */
    fun setProgressOnly(@IntRange(from = 0, to = 100) progress: Int, floatText: String?) {
        mProgress = progress
        mFloatText = floatText
    }
//
//    fun setOnIndicatorPositionChangedListener(listener:OnIndicatorPositionChangedListener){
//        mIndicatorPositionChangedListener = listener
//    }
    /**
     * 淡出Float
     */
    fun fadeOutFloat() {
        if(mFloatOutAnimRunning)
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
    fun fadeInFloat() {
        if(mFloatInAnimRunning || mDrawFloat)
            return
        mFloatInAnimRunning = true
        if (mFloatFadeOutAnim.isRunning || mFloatFadeOutAnim.isStarted) {
            mFloatFadeOutAnim.cancel()
        }
        mFloatFadeInAnim.start()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //上padding
        var height = paddingTop

        //进度条高度
        height += indicatorHeight.toInt()

        //float 高度 =  文字高度+文字上下padding+三角形高度+与bar之间的间隙
        height += (mTextFontMetrics.bottom - mTextFontMetrics.top + mFloatTextVerticalPadding * 2) + mTriangleHeight + mMarginSize

        //下padding
        height += paddingBottom

        setMeasuredDimension(measuredWidth, height)
    }

    //指示器高度
    private val indicatorHeight: Float get() = mIndicatorRadius * 2

    //进度条宽度
    private val barWidth: Float
        get() = width - mIndicatorRadius * 2

    private val barRectLeft get() = mIndicatorRadius

    private val barRectRight get() = width - mIndicatorRadius

    /**
     * 绘制的bar在左右留出了半个indicator指示器的宽度，因此其他地方计算要注意这一点
     */
    private fun drawProgressBar(canvas: Canvas) {

        var barY = (height - paddingBottom - mBarHeight).toFloat()

        //减去指示器比进度条高的部分
        barY -= (indicatorHeight - mBarHeight) / 2
        mBarPaint.color = mBgColor

        //绘制背景
        mBarRectF.set(
            barRectLeft,
            barY,
            barRectRight,
            barY + mBarHeight
        )
        canvas.drawRoundRect(mBarRectF, mRoundedRadius, mRoundedRadius, mBarPaint)

        val progressX = barWidth * (mProgress / 100f) + mIndicatorInnerRadius
        //绘制进度
        mBarPaint.color = mFgColor
        mBarRectF.right = progressX
        canvas.drawRoundRect(mBarRectF, mRoundedRadius, mRoundedRadius, mBarPaint)
    }

    val indicatorX get() = mProgress / 100f * barWidth + mIndicatorRadius
    val indicatorY get() = height - paddingBottom - mIndicatorRadius


    private fun drawIndicator(canvas: Canvas) {

        val centerX = indicatorX
        val centerY = indicatorY
        mBarPaint.color = mIndicatorColor
        //外圆
        canvas.drawCircle(centerX, centerY.toFloat(), mIndicatorRadius.toFloat(), mBarPaint)

        //内圆
        mBarPaint.color = mIndicatorInnerColor
        canvas.drawCircle(centerX, centerY.toFloat(), mIndicatorInnerRadius.toFloat(), mBarPaint)

//        mIndicatorPositionChangedListener?.onIndicatorPositionChanged(centerX,centerY,mProgress)
    }

//    interface OnIndicatorPositionChangedListener {
//        fun onIndicatorPositionChanged(x: Float, y: Float, progress: Int)
//    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawProgressBar(canvas)
        drawIndicator(canvas)


        //文本不为空，绘制指示器文本
        val text = mFloatText
        if (!mDrawFloat || text.isNullOrEmpty())
            return
        mTextPaint.getTextBounds(text, 0, text.length, mTextRect)

        val textWidth: Int = mTextRect.width()//文本宽度
        val textHeight = mTextFontMetrics.bottom - mTextFontMetrics.top//文本高度

        val textRectWidth = textWidth + mFloatTextHorizontalPadding * 2f
        val textRectHeight = textHeight + mFloatTextVerticalPadding * 2f
        val progressX = width * (mProgress / 100f)

        var left = progressX - textRectWidth / 2f
        var right = progressX + textRectWidth / 2f
        if (left < 0) {
            left = 0f
            right = textRectWidth
        } else if (right > width) {
            right = width.toFloat()
            left = right - textRectWidth
        }
        //绘制文本背景:文本框的背景定位是根据整个控件的宽度来定位的
        mBarRectF.set(left, 0f, right, textRectHeight)
        mFloatPaint.color = mFloatBgColor
        mFloatPaint.alpha = mFloatPaintAlpha
        canvas.drawRoundRect(
            mBarRectF,
            mFloatBgRoundedRadius.toFloat(), mFloatBgRoundedRadius.toFloat(), mFloatPaint
        )

        //绘制文字
        val distance =
            (mTextFontMetrics.descent - mTextFontMetrics.ascent) / 2 - mTextFontMetrics.bottom
        val baseline: Float = mBarRectF.centerY() + distance
        canvas.drawText(text, mBarRectF.centerX(), baseline, mTextPaint)

        //绘制箭头
        mTrianglePath.reset()
        val tx = indicatorX
        val ty = mBarRectF.bottom + mTriangleHeight
        //移动到三角形顶点：三角形的顶点是在指示器的中间，所以在两端要考虑顶点的问题
        mTrianglePath.moveTo(tx, ty)

        var leftPointX = tx - mTriangleWidth / 2
        var leftPointY = ty - mTriangleHeight
        var rightPointX = tx + mTriangleWidth / 2
        var rightPointY = ty - mTriangleHeight

        if (leftPointX < 0) {
            //超出边界了
            leftPointX = 0f
            leftPointY = leftPointY - mFloatBgRoundedRadius
        }

        if (rightPointX > width) {
            rightPointX = width.toFloat()
            rightPointY = rightPointY - mFloatBgRoundedRadius
        }

        mTrianglePath.lineTo(rightPointX, rightPointY)
        mTrianglePath.lineTo(leftPointX, leftPointY)
        mTrianglePath.close()
        canvas.drawPath(mTrianglePath, mFloatPaint)

    }
}