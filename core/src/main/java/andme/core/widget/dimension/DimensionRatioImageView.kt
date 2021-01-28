package andme.core.widget.dimension

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * 按比例设置Size的ImageView
 * 比如设置属性：dimensionRatio="16:9"
 * 已测试
 * note：view的大小根据比例进行设置，而不是view的展示内容根据比例进行设置，两者的区别是前者没有单独考虑设置的padding。所以最好是不要设置padding
 */
class DimensionRatioImageView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr), DimensionRatioViewHelper.OnDimensionRatioInteraction {

    private val mHelper: DimensionRatioViewHelper

    init {
        mHelper = DimensionRatioViewHelper(this)
        mHelper.resolveAttributes(context, attrs, defStyleAttr)
    }

    fun setDimensionRatio(ratioWidth: Int, ratioHeight: Int): DimensionRatioImageView {
        mHelper.setDimensionRatio(ratioWidth, ratioHeight)
        return this
    }

    fun setMode(@AspectMode mode: Int): DimensionRatioImageView {
        mHelper.setMode(mode)
        return this
    }

    override fun drRequestLayout() {
        requestLayout()
    }

    override fun drSetMeasuredDimension(measuredWidth: Int, measuredHeight: Int) {
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    @SuppressLint("WrongCall")
    override fun drCallSuperOnMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //根据drawable的比例设置大小；
        val drawable = this.drawable
        if (drawable == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        if (drawableWidth <= 0 || drawableHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        //这一部分代码的有效性待完整测试
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (mHelper.getMode() == AspectMode.BASED_ON_HEIGHT) {
            if (heightMode != MeasureSpec.UNSPECIFIED) {
                //layout_height == match_parent  or 固定值，则根据高度计算宽度
                val exactlyWith = heightSize.toDouble() / drawableHeight * drawableWidth
                setMeasuredDimension(exactlyWith.toInt(), heightSize)
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        } else {
            if (widthMode != MeasureSpec.UNSPECIFIED) {
                //layout_width == match_parent  or 固定值，则根据宽度计算高度
                val exactlyHeight = widthSize.toDouble() / drawableWidth * drawableHeight
                setMeasuredDimension(widthSize, exactlyHeight.toInt())
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mHelper.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}