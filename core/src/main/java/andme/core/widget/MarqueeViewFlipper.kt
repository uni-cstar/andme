package andme.core.widget

import andme.core.R
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ViewFlipper
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView


/**
 * Created by Lucio on 2021/3/9.
 */
class MarqueeViewFlipper @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewFlipper(context, attrs) {

    private val mContents = mutableListOf<String>()

    private var mFlipping: Boolean = false

    /**
     * 每条文本滚动的时间
     */
    private var mDuration = DEFAULT_DURATION

    private var mStayDuration = DEFAULT_STAY_DURATION

    private var mTextColor: Int = Color.BLACK
    private var mTextSize: Float = 14f

    private val mFlipRunnable = object : Runnable {
        override fun run() {
            if (mFlipping) {
                showNext()
                if (isMultiContent)
                    postDelayed(this, mDuration + mStayDuration)
            }
        }
    }

    init {
        val tp = context.obtainStyledAttributes(attrs, R.styleable.MarqueeViewFlipper).also {
            mDuration = it.getInteger(
                R.styleable.MarqueeViewFlipper_mvf_duration,
                DEFAULT_DURATION.toInt()
            ).toLong()

            mStayDuration = it.getInteger(
                R.styleable.MarqueeViewFlipper_mvf_stayTime,
                DEFAULT_STAY_DURATION.toInt()
            ).toLong()

            mTextSize = it.getDimension(
                R.styleable.MarqueeViewFlipper_mvf_text_size,
                mTextSize
            )
            mTextColor = it.getColor(R.styleable.MarqueeViewFlipper_mvf_text_color, mTextColor)
        }
        tp.recycle()

        outAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                if (!isMultiContent)
                    return

                val childCount = childCount
                val currentIndex = indexOfChild(currentView)
                val nextIndex = (currentIndex - 1 + childCount) % childCount
                val preView = getChildAt(nextIndex) as? ScrollTextView ?: return
                preView.reset()

                val currentView = currentView as ScrollTextView
                val dx = currentView.textWidth - currentView.width / 3
                if (dx > 0) {
                    currentView.start(dx)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    private val isMultiContent: Boolean
        get() {
            return mContents.size > 1
        }

    private fun prepareChildren() {
        removeAllViews()
        if (!isMultiContent) {
            val content = mContents.firstOrNull()
            if (content.isNullOrEmpty())
                return
            //如果当前只有一个文本，则添加一个标准的跑马灯
            val tv = NormalMarqueeView(context).also {
                it.text = content
                it.setTextColor(mTextColor)
                it.textSize = mTextSize
            }
            addView(tv)
        } else {
            //添加多个可以滚动的自定义跑马灯
            mContents.forEach {
                val view: ScrollTextView = ScrollTextView(context, mDuration, mTextSize, mTextColor)
                view.setText(it)
                addView(view)
            }
        }
    }

    fun setContents(contents: List<String>) {
        mContents.clear()
        mContents.addAll(contents)
        prepareChildren()
    }

    /**
     * 启动切换动画
     */
    fun startFlip() {
        mFlipping = true
        post(mFlipRunnable)
    }

    fun stopFlip() {
        mFlipping = false
        removeCallbacks(mFlipRunnable)
    }

    class ScrollTextView constructor(
        context: Context,
        val duration: Long,
        val textSize: Float,
        @ColorInt
        val textColor: Int
    ) : View(context) {

        private var mText: String? = null
        private val mPaint = Paint()
        private val mAnimator = ValueAnimator()
        private val mRect = Rect()
        private var dx = 0

        init {
            mPaint.isAntiAlias = true
            mPaint.isDither = true
            mPaint.textSize = textSize
            mPaint.color = textColor

            //设置跑马灯动画
            mAnimator.interpolator = LinearInterpolator()
            mAnimator.duration = duration
            mAnimator.addUpdateListener(AnimatorUpdateListener { animation ->
                dx = -(animation.animatedValue as Int)
                invalidate()
            })
        }

        fun setText(text: String?) {
            mText = text
        }

        /**
         * 开始滚动；滚动距离为文本宽度
         */
        fun start() {
            val textWidth = textWidth
            if (textWidth < width) {
                //文本比控件短，则不执行滚动动画
                Log.w("MarqueeViewFlipper", "文本宽度比控件宽度短，不做滚动")
                return
            }
            //滚动距离为 多出的文本宽度+ 一半控件宽度
            val dx = (textWidth - width) + width / 2
            start(dx)
        }

        /**
         * 开始滚动动画
         * @param dx 滚动的距离
         */
        fun start(dx: Int) {
            mAnimator.setIntValues(0, dx)
            mAnimator.start()
        }

        /**
         * 重置
         */
        fun reset() {
            dx = 0
            mAnimator.cancel()
        }

        /**
         * 文本宽度
         */
        val textWidth: Int
            get() {
                return if (!mText.isNullOrEmpty()) {
                    mPaint.getTextBounds(mText, 0, mText!!.length, mRect)
                    mRect.width()
                } else {
                    0
                }
            }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val text = mText
            if (text.isNullOrEmpty()) {
                return
            }
            mPaint.getTextBounds(text, 0, text.length, mRect)
            val fontMetrics: Paint.FontMetrics = mPaint.fontMetrics
            val baseLine = (height - fontMetrics.bottom - fontMetrics.top) / 2
            canvas.drawText(text, dx.toFloat(), baseLine, mPaint)
        }
    }


    class NormalMarqueeView(context: Context) :
        AppCompatTextView(context) {

        init {
            setSingleLine()
            ellipsize = TextUtils.TruncateAt.MARQUEE
            marqueeRepeatLimit = -1
        }

        override fun onFocusChanged(
            focused: Boolean, direction: Int,
            previouslyFocusedRect: Rect?
        ) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect)
        }

        override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
            if (hasWindowFocus) super.onWindowFocusChanged(hasWindowFocus)
        }

        override fun isFocused(): Boolean {
            return true
        }
    }

    companion object {

        const val DEFAULT_DURATION = 10000L

        /**
         * 每条文本停留时间：停留即文本静止显示的时间
         */
        const val DEFAULT_STAY_DURATION = 3000L
    }
}