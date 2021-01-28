package andme.core.widget.dimension

/**
 * Created by Lucio on 2019/6/16.
 */


import andme.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * View Dimension Ratio 帮助类
 */
internal open class DimensionRatioViewHelper(val interaction: OnDimensionRatioInteraction) {

    protected var mRatioWidth: Int = 0
    protected var mRatioHeight: Int = 0
    protected var mMode: Int = AspectMode.BASED_ON_WIDTH

    internal interface OnDimensionRatioInteraction {
        fun drRequestLayout()
        fun drSetMeasuredDimension(measuredWidth: Int, measuredHeight: Int)
        fun drCallSuperOnMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    }


    internal fun resolveAttributes(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null)
            return
        val a = ctx.obtainStyledAttributes(attrs, R.styleable.DimensionRatioView, defStyleAttr, 0)
        val dimensionRatioValue = a.getString(R.styleable.DimensionRatioView_dimensionRatio)
        if (dimensionRatioValue.isNullOrEmpty()) {
            mRatioWidth = -1
            mRatioHeight = -1
        } else {
            val dimensionRatios = dimensionRatioValue.split(":")
            mRatioWidth = dimensionRatios[0].toInt()
            mRatioHeight = dimensionRatios[1].toInt()
        }
        mMode = a.getInteger(R.styleable.DimensionRatioView_mode, AspectMode.BASED_ON_WIDTH)
        a.recycle()
    }

    fun setDimensionRatio(ratioWidth: Int, ratioHeight: Int) {
        val shouldRequestLayout = this.mRatioWidth != ratioWidth || this.mRatioHeight != ratioHeight
        mRatioWidth = ratioWidth
        mRatioHeight = ratioHeight
        if (shouldRequestLayout) {
            interaction.drRequestLayout()
        }
    }

    fun setMode(@AspectMode mode: Int) {
        val shouldRequestLayout = this.mMode != mode
        this.mMode = mode
        if (shouldRequestLayout) {
            interaction.drRequestLayout()
        }
    }

    @AspectMode
    fun getMode():Int = this.mMode

    open fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mRatioWidth > 0 && mRatioHeight > 0) {
            val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
            val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
            val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
            val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

            if (mMode == AspectMode.BASED_ON_HEIGHT) {
                if ( heightMode != View.MeasureSpec.UNSPECIFIED) {
                    //layout_height == match_parent  or 固定值，则根据高度计算宽度
                    val exactlyWith = heightSize.toDouble() / mRatioHeight * mRatioWidth
                    interaction.drSetMeasuredDimension(exactlyWith.toInt(), heightSize)
                } else {
                    interaction.drCallSuperOnMeasure(widthMeasureSpec, heightMeasureSpec)
                }
            } else {
                if (widthMode != View.MeasureSpec.UNSPECIFIED) {
                    //layout_width == match_parent  or 固定值，则根据宽度计算高度
                    val exactlyHeight = widthSize.toDouble() / mRatioWidth * mRatioHeight
                    interaction.drSetMeasuredDimension(widthSize, exactlyHeight.toInt())
                } else {
                    interaction.drCallSuperOnMeasure(widthMeasureSpec, heightMeasureSpec)
                }
            }
        } else {
            interaction.drCallSuperOnMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}

/**
 * View Group Ratio 帮助类
 */
internal class DimensionRatioViewGroupHelper(interaction: OnDimensionRatioInteraction)
    : DimensionRatioViewHelper(interaction) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mRatioWidth > 0 && mRatioHeight > 0) {
            val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
            val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

            if (mMode == AspectMode.BASED_ON_HEIGHT) {
                val exactlyWith = heightSize.toDouble() / mRatioHeight * mRatioWidth
                interaction.drCallSuperOnMeasure(
                        View.MeasureSpec.makeMeasureSpec(exactlyWith.toInt(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(heightSize, View.MeasureSpec.EXACTLY))
            } else {
                val exactlyHeight = widthSize.toDouble() / mRatioWidth * mRatioHeight
                interaction.drCallSuperOnMeasure(
                        View.MeasureSpec.makeMeasureSpec(widthSize, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(exactlyHeight.toInt(), View.MeasureSpec.EXACTLY))
            }
        } else {
            interaction.drCallSuperOnMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}

