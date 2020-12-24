package andme.core.widget.recycler

import android.graphics.Canvas
import android.graphics.Paint

/**
 * Created by Lucio on 17/12/18.
 * 颜色 线性分割线
 */

class ItemDecorationLinearColor @JvmOverloads constructor(space: Int,
                                                          color: Int,
                                                          orientation: Int = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                                                          drawBeginning: Boolean = false,
                                                          drawEnding: Boolean = true)
    : ItemDecorationLinear(space, drawBeginning, drawEnding, orientation) {


    private val mPaint: Paint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        paint.strokeWidth = space.toFloat()
        paint
    }

    init {
        mPaint.color = color
    }

    override fun draw(c: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
    }

}