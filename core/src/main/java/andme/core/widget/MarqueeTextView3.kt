package andme.core.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * 可以支持多条跑马灯：效果是一条显示完成之后再显示下一条
 */
class MarqueeTextView3 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    /**
     * 是否正在滚动文本
     */
    var mScrolling = false

    private var mCompleteListener: onMarqueeScrollCompleteListener? = null
    private var mTextLength = 0f
    private var mViewWidth = 0f
    private var mTextContent = ""

    /**
     * 文本滚动的总距离：文本宽度 + 偏移量
     */
    private var mScrollDistance = 0.0f

    /**
     * X方向已经滚动的距离
     */
    private var xScrollDistance = 0f

    /**
     * 偏移位置
     */
    private var mOffsetX = 0f


    private var mAlignFrom: Int = ALIGN_FROM_RIGHT

    /**
     * 跑马灯完成的次数
     */
    private var mCurrentMarqueeCompleteCount = 0

    /**
     * 调试模式的时候是否启用动画：默认不启用；启用之后在调试模式下无法进行调试，
     * 因为这个控件在不断的请求重绘
     */
    var isEnableInDebugMode: Boolean = true

    override fun setMarqueeRepeatLimit(marqueeLimit: Int) {
        super.setMarqueeRepeatLimit(marqueeLimit)
    }

    override fun getMarqueeRepeatLimit(): Int {
        return super.getMarqueeRepeatLimit()
    }

    private fun setup() {
        mTextContent = text.toString()
        mTextLength = paint.measureText(mTextContent)
        mViewWidth = width.toFloat()
        if (mViewWidth == 0f) {
            mViewWidth = context.resources.displayMetrics.widthPixels.toFloat()
        }

        xScrollDistance = 0f
        if (mAlignFrom == ALIGN_FROM_RIGHT) {
            mOffsetX = mViewWidth
            mScrollDistance = mViewWidth + mTextLength
        } else {
            mOffsetX = 0f
            mScrollDistance = mTextLength
        }
    }

    fun resetScroll() {
        xScrollDistance = 0f
        if (mAlignFrom == ALIGN_FROM_RIGHT) {
            mOffsetX = mViewWidth
        } else {
            mOffsetX = 0f
        }
    }

    fun setContents(contents: List<String>?) {
        val notEmptyContents = contents?.filter {
            it.isNotEmpty()
        }
        if (notEmptyContents.isNullOrEmpty()) {
            text = ""
            setOnMarqueeCompleteListener(null)
            return
        }

        marqueeRepeatLimit = -1
        if (notEmptyContents.size == 1) {
            text = contents.first()
            setOnMarqueeCompleteListener(object : onMarqueeScrollCompleteListener {
                override fun onMarqueeScrollComplete() {
                    text = contents.first()
                }
            })
        } else {
            var currentContentIndex = 0
            text = contents[currentContentIndex]
            setOnMarqueeCompleteListener(object : onMarqueeScrollCompleteListener {
                override fun onMarqueeScrollComplete() {
                    currentContentIndex++
                    text = contents[currentContentIndex % contents.size]
                }
            })
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.step = xScrollDistance
        ss.isStarting = mScrolling
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        val ss = state
        super.onRestoreInstanceState(ss.superState)
        xScrollDistance = ss.step
        mScrolling = ss.isStarting
    }

    class SavedState : BaseSavedState {
        var isStarting = false
        var step = 0.0f

        internal constructor(superState: Parcelable?) : super(superState) {}

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(if(isStarting) 1 else 0)
            out.writeFloat(step)
        }

        private constructor(`in`: Parcel) : super(`in`) {
            isStarting = `in`.readInt() > 0
            step = `in`.readFloat()
        }

        companion object {
            val CREATOR: Parcelable.Creator<SavedState?> =
                object : Parcelable.Creator<SavedState?> {
                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }

                    override fun createFromParcel(`in`: Parcel): SavedState? {
                        return SavedState(`in`)
                    }
                }
        }
    }

    fun startScroll() {
        if (mScrolling)
            return

        if(!isEnableInDebugMode)
            return

        mScrolling = true
        invalidate()
    }

    fun stopScroll() {
        mScrolling = false
//        invalidate()
    }

    public override fun onDraw(canvas: Canvas) {
//        val paintY = textSize + paddingTop
        paint.color = currentTextColor

        if (mTextContent != text.toString()) {
            println("重置参数")
            setup()
        }
        val fontMetrics: Paint.FontMetrics = paint.fontMetrics
        val paintY = (height - fontMetrics.bottom - fontMetrics.top) / 2
        canvas.drawText(
            mTextContent,
            xScrollDistance + mOffsetX,
            paintY,
            paint
        )
        if (!mScrolling) {
            return
        }
        //不断往左偏移
        xScrollDistance -= 1.5f
        //偏移的距离大于目标距离时，偏移结束
        if (-xScrollDistance >= mScrollDistance) {
            //当前滑动完毕
            mCurrentMarqueeCompleteCount++
            resetScroll()
            if (mCurrentMarqueeCompleteCount >= marqueeRepeatLimit) {
                mCompleteListener?.onMarqueeScrollComplete()
            }
        } else {
            invalidate()
        }
    }

    fun setOnMarqueeCompleteListener(listener: onMarqueeScrollCompleteListener?) {
        this.mCompleteListener = listener
    }

    interface onMarqueeScrollCompleteListener {
        fun onMarqueeScrollComplete()
    }

    companion object {

        /**
         * 对齐右侧开始滑动
         */
        const val ALIGN_FROM_RIGHT = 0

        /**
         * 对齐左侧开始滑动
         */
        const val ALIGN_FROM_LEFT = 1

    }
}