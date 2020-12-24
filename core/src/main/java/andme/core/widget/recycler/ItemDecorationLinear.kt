package andme.core.widget.recycler

import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.View
import andme.lang.orDefault


/**
 * Created by Lucio on 17/12/18.
 * recycler  线性 分割线
 * @param drawBeginning 是否在开始的地方绘制一条分割线；因为Item的分割线是在Item的结束之后的绘制分割线，所以如果需要在第一条Item的前面绘制一条分割线，将此变量设置为true
 */

abstract class ItemDecorationLinear constructor(val space: Int,
                                                val drawBeginning: Boolean = false,
                                                val drawableEnding: Boolean = false,
                                                val orientation: Int = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {


    private fun drawItemBeginning(position: Int): Boolean {
        return position == 0 && drawBeginning
    }

    private fun drawItemEnding(position: Int, itemCount: Int): Boolean {
        //如果position 不是最后一个显示的view 或者 设置最后一个要绘制分割线，则需要绘制分割线
        return drawableEnding || position < itemCount - 1
    }

    final override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        var left: Int = 0
        var top: Int = 0
        var right: Int = 0
        var bottom: Int = 0


        val childCount = parent.childCount
        val position = parent.getChildAdapterPosition(view).orDefault(-4)
        //是否绘制第一个item的前一条线
        if (drawItemBeginning(position) && childCount > 0) {
            if (orientation == androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL) {
                left = space
            } else {
                top = space
            }
        }

        //如果不是最后一个Item或者允许最后一个item末尾绘制分割线
        if (drawItemEnding(position, parent.adapter?.itemCount.orDefault())) {
            if (orientation == androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL) {
                right = space
            } else {
                bottom = space
            }
        }
        outRect.set(left, top, right, bottom)
    }

    override fun onDraw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        if (parent.layoutManager == null)
            return
        c.run {
            if (orientation == androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL) {
                drawHorizontal(c, parent)
            } else {
                drawVertical(c, parent)
            }
        }
    }

    private fun drawVertical(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView) {
        //recycler的左右padding区域不在分割线内
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount

        //绘制第一条线
        if (childCount > 0 && drawItemBeginning(0)) {
            val first = parent.getChildAt(0)
            val params = first.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
            val bottom = first.top - params.topMargin
            val top = bottom - space
            draw(c, left, top, right, bottom)
        }

        val itemCount = parent.adapter?.itemCount.orDefault()
        //常规绘制
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)
            if (adapterPosition >= 0 && drawItemEnding(adapterPosition, itemCount)) {
                val params = child.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + space
                draw(c, left, top, right, bottom)
            }
        }
    }

    private fun drawHorizontal(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount

        Log.d("drawHorizontal", "childCount=$childCount")
        //绘制第一条线
        if (childCount > 0 && drawItemBeginning(0)) {
            val first = parent.getChildAt(0)
            val params = first.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
            val right = first.left - params.leftMargin
            val left = right - space
            draw(c, left, top, right, bottom)
        }

        val itemCount = parent.adapter?.itemCount.orDefault()
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)
            if (drawItemEnding(adapterPosition, itemCount)) {
                val params = child.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
                val left = child.right + params.rightMargin
                val right = left + space
                draw(c, left, top, right, bottom)
            }
        }
    }

    //根据指定区域绘制
    protected abstract fun draw(c: Canvas, left: Int, top: Int, right: Int, bottom: Int)

}