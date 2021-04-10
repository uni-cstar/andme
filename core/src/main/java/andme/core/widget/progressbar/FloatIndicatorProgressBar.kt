package andme.core.widget.progressbar

import andme.core.R
import andme.core.util.dip
import andme.core.util.sp
import android.content.Context
import android.graphics.*
import android.graphics.Paint.FontMetricsInt
import android.util.AttributeSet
import android.view.View
import kotlin.math.ceil

/**
 * Created by Lucio on 2021/4/5.
 * 带浮动标记的ProgressBar
 * 具体效果查看doc/screencast/floatindicator_progressbar.png
 * 高度设置为wrap_content即可
 */
class FloatIndicatorProgressBar @JvmOverloads constructor(
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

    /*是否显示进度指示器*/
    private var mShowIndicator: Boolean = true

    /*是否显示进度指示器*/
    private var mIndicatorColor: Int = 0
    private var mIndicatorWidth: Int = 0
    private var mIndicatorProgress: Int = 0
    private var mIndicatorHeightThanBar = 0

    /*进度条与指示器之间的空白间距*/
    private val mMarginSize: Int

    /*三角形高度*/
    private val mTriangleHeight: Int
    private val mTriangleWidth: Int
    private val mTrianglePath: Path = Path()

    private var mShowFloat: Boolean = true

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


    init {

        mFloatTextHorizontalPadding = dip(4)
        mFloatTextVerticalPadding = dip(2)
        mFloatBgRoundedRadius = dip(5)
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.FloatIndicatorProgressBar, defStyleAttr, 0
        )

        mProgress = a.getInt(R.styleable.FloatIndicatorProgressBar_fpb_progress, 50)
        mIndicatorProgress = mProgress
        mBgColor = a.getColor(R.styleable.FloatIndicatorProgressBar_fpb_bgColor, Color.LTGRAY)
        mFgColor = a.getColor(R.styleable.FloatIndicatorProgressBar_fpb_fgColor, Color.DKGRAY)
        mBarHeight = a.getDimensionPixelSize(
            R.styleable.FloatIndicatorProgressBar_fpb_progressBarHeight, dip(
                8
            )
        )
        mMarginSize = a.getDimensionPixelSize(
            R.styleable.FloatIndicatorProgressBar_fpb_marginSize, dip(
                2
            )
        )

        mShowIndicator =
            a.getBoolean(R.styleable.FloatIndicatorProgressBar_fpb_showBarIndicator, true)
        mIndicatorColor = a.getColor(
            R.styleable.FloatIndicatorProgressBar_fpb_barIndicatorColor,
            Color.BLUE
        )
        mIndicatorWidth = a.getDimensionPixelSize(
            R.styleable.FloatIndicatorProgressBar_fpb_barIndicatorWidth,
            dip(6)
        )
        mIndicatorHeightThanBar = a.getDimensionPixelSize(
            R.styleable.FloatIndicatorProgressBar_fpb_barIndicatorHeightThanBar, dip(4)
        )

        mTriangleHeight = a.getDimensionPixelSize(
            R.styleable.FloatIndicatorProgressBar_fpb_triangleHeight, dip(
                6
            )
        )

        mTriangleWidth = a.getDimensionPixelSize(
            R.styleable.FloatIndicatorProgressBar_fpb_triangleWidth, dip(5)
        )


        mShowFloat = a.getBoolean(R.styleable.FloatIndicatorProgressBar_fpb_showFloat, true)

        mFloatTextSize = a.getDimensionPixelSize(
            R.styleable.FloatIndicatorProgressBar_fpb_floatTextSize,
            sp(14)
        )
        mFloatTextColor = a.getColor(
            R.styleable.FloatIndicatorProgressBar_fpb_floatTextColor,
            Color.WHITE
        )
        if (a.hasValue(R.styleable.FloatIndicatorProgressBar_fpb_floatText))
            mFloatText = a.getString(R.styleable.FloatIndicatorProgressBar_fpb_floatText)
        mFloatBgColor = a.getColor(
            R.styleable.FloatIndicatorProgressBar_fpb_floatBgColor,
            Color.DKGRAY
        )


        mRoundedRadius = ceil(mBarHeight.toFloat() / 2)
        a.recycle()

        setup()
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

    fun setProgress(progress: Int) {
        mProgress = progress
        postInvalidate()
    }

    fun setProgress(progress: Int, indicatorProgress: Int) {
        mProgress = progress
        mIndicatorProgress = indicatorProgress
        postInvalidate()
    }

    fun setProgress(progress: Int, indicatorProgress: Int, floatText: String?) {
        mProgress = progress
        mIndicatorProgress = indicatorProgress
        mFloatText = floatText
        postInvalidate()
    }

    fun setShowFloat(show: Boolean) {
        if (mShowFloat != show) {
            mShowFloat = show
            invalidate()
        }
    }

    fun setShowIndicator(show: Boolean) {
        if (mShowIndicator != show) {
            mShowIndicator = show
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //文字高度 + 三角形高度 +间隙 + 进度条指示器高度（指示器比进度条本身更高）
        var height = paddingTop

        if (mShowIndicator) {//如果不显示indicator，则只加上进度条高度
            height += indicatorHeight
        } else {
            height += mBarHeight
        }

        if (mShowFloat) {//加上浮动框大小
            height += (mTextFontMetrics.bottom - mTextFontMetrics.top + mFloatTextVerticalPadding * 2) + mTriangleHeight + mMarginSize
        }

        setMeasuredDimension(measuredWidth, height)
    }

    private val indicatorHeight: Int get() = mBarHeight + 2 * mIndicatorHeightThanBar

    //进度条宽度
    private val barWidth: Int
        get() = width - mIndicatorWidth

    /**
     * 绘制的bar在左右留出了半个indicator指示器的宽度，因此其他地方计算要注意这一点
     */
    private fun drawProgressBar(canvas: Canvas) {
        val barLeft = mIndicatorWidth / 2f
        val barRight = width - mIndicatorWidth / 2f

        mBarPaint.color = mBgColor
        var barY = (height - mBarHeight).toFloat()
        if (mShowIndicator) {
            barY -= mIndicatorHeightThanBar
        }
       
        //绘制背景
        mBarRectF.set(
            barLeft,
            barY,
            barRight,
            barY + mBarHeight
        )
        canvas.drawRoundRect(mBarRectF, mRoundedRadius, mRoundedRadius, mBarPaint)

        val progressX = width * (mProgress / 100f)
        //绘制进度
        mBarPaint.color = mFgColor
        mBarRectF.right = progressX
        canvas.drawRoundRect(mBarRectF, mRoundedRadius, mRoundedRadius, mBarPaint)
    }

    private fun calIndicatorCenterX(): Float {
        return mIndicatorProgress / 100f * barWidth + mIndicatorWidth / 2
    }

    private fun drawIndicator(canvas: Canvas) {
        //绘制指示器
        if (mShowIndicator) {
            val progressX = calIndicatorCenterX()
            mBarPaint.color = mIndicatorColor
            val indicatorLeft = progressX - mIndicatorWidth / 2
            val indicatorRight = progressX + mIndicatorWidth / 2
            if (indicatorLeft < 0) {
                mBarRectF.set(
                    0f, (height - indicatorHeight).toFloat(),
                    mIndicatorWidth.toFloat(), height.toFloat()
                )
            } else if (indicatorRight > width) {
                mBarRectF.set(
                    (width - mIndicatorWidth).toFloat(), (height - indicatorHeight).toFloat(),
                    width.toFloat(), height.toFloat()
                )
            } else {
                mBarRectF.set(
                    indicatorLeft,
                    (height - indicatorHeight).toFloat(),
                    indicatorRight,
                    height.toFloat()
                )
            }
            canvas.drawRoundRect(mBarRectF, mIndicatorWidth / 2f, mIndicatorWidth / 2f, mBarPaint)

        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawProgressBar(canvas)
        drawIndicator(canvas)

        val text = mFloatText
        if (!mShowFloat || text.isNullOrEmpty())
            return
        //文本不为空，绘制指示器文本

        mTextPaint.getTextBounds(text, 0, text.length, mTextRect)

        val textWidth: Int = mTextRect.width()//文本宽度
        val textHeight = mTextFontMetrics.bottom - mTextFontMetrics.top//文本高度

        val textRectWidth = textWidth + mFloatTextHorizontalPadding * 2f
        val textRectHeight = textHeight + mFloatTextVerticalPadding * 2f
        val progressX = width * (mIndicatorProgress / 100f)

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
        mBarPaint.color = mFloatBgColor
        canvas.drawRoundRect(
            mBarRectF,
            mFloatBgRoundedRadius.toFloat(), mFloatBgRoundedRadius.toFloat(), mBarPaint
        )

        //绘制文字
        val distance =
            (mTextFontMetrics.descent - mTextFontMetrics.ascent) / 2 - mTextFontMetrics.bottom
        val baseline: Float = mBarRectF.centerY() + distance
        canvas.drawText(text, mBarRectF.centerX(), baseline, mTextPaint)

        //绘制箭头
        mTrianglePath.reset()
        val tx = calIndicatorCenterX()
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
        canvas.drawPath(mTrianglePath, mBarPaint)

    }
}