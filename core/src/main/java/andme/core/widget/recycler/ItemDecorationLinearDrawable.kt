package andme.core.widget.recycler

import android.graphics.Canvas
import android.graphics.drawable.Drawable

class ItemDecorationLinearDrawable constructor(space: Int,
                                               divider: Drawable,
                                               orientation: Int = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                                               drawBeginning: Boolean = false,
                                               drawEnding: Boolean = true)
    : ItemDecorationLinear(space, drawBeginning, drawEnding, orientation) {

    /**
     * 此方法待测
     * @param divider 如果是用xml写的drawable，则需要定义size节点指明高度
     */
    constructor(divider: Drawable,
                orientation: Int = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                drawBeginning: Boolean = false,
                drawEnding: Boolean = true) :
            this(if (orientation == androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL) divider.intrinsicWidth else divider.intrinsicHeight,
                    divider,
                    orientation,
                    drawBeginning,
                    drawEnding) {

    }

    val mDivider: Drawable

    init {
        mDivider = divider
    }

    override fun draw(c: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
        mDivider.setBounds(left, top, right, bottom)
        mDivider.draw(c)
    }

}