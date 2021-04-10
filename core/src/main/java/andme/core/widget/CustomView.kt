package andme.core.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec
import android.view.View.resolveSize

/**
 * Created by Lucio on 2021/3/15.
 * 自定义view相关工具函数
 */


/**
 * 获取测量大小
 * @param expectSize 不指定大小或者默认情况下设置的大小，<=0 则直接使用测量的大小
 * @return 测量之后的大小
 */
fun View.resolveMeasureSize(expectSize: Int, measureSpec: Int): Int {
    return if (expectSize > 0) {
        resolveSize(expectSize, measureSpec)
    } else {
        MeasureSpec.getSize(measureSpec)
    }
}

fun View.measureSize(
    expectWidth: Int,
    widthMeasureSpec: Int,
    expectHeight: Int,
    heightMeasureSpec: Int
): Pair<Int, Int> {
    val width = resolveMeasureSize(expectWidth, widthMeasureSpec)
    val height = resolveMeasureSize(expectHeight, heightMeasureSpec)
    return Pair(width, height)
}

class CustomViewSample @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size =
            measureSize(getExpectWidth(), widthMeasureSpec, getExpectHeight(), heightMeasureSpec)
        setMeasuredDimension(size.first, size.second)
    }

    fun getExpectWidth(): Int {
        return suggestedMinimumWidth
    }

    fun getExpectHeight(): Int {
        return suggestedMinimumHeight
    }
}