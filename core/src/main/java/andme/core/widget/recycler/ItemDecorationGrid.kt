package andme.core.widget.recycler

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View

/**
 * Created by Lucio on 17/12/18.
 */
/**
 * 适用于GridView，支持在item之间设置任何类型的间距，支持控制是否显示上下左右间隔及是否绘制上下左右背景
 */
class ItemDecorationGrid private constructor(builder: Builder)
    : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    private val mDivider: Drawable?
    private val spanCount: Int
    private val spaceSize: Int
    private val includeLREdge: Boolean
    private val includeTBEdge: Boolean
    private val drawLREdge: Boolean
    private val drawTBEdge: Boolean

    init {
        mDivider = builder.divider
        spanCount = builder.spanCount
        spaceSize = builder.spaceSize
        includeLREdge = builder.includeLREdge
        includeTBEdge = builder.includeTBEdge
        drawLREdge = builder.drawLREdge
        drawTBEdge = builder.drawTBEdge
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column
        if (includeLREdge) {
            outRect.left = spaceSize - column * spaceSize / spanCount
            outRect.right = (column + 1) * spaceSize / spanCount
        } else {
            outRect.left = column * spaceSize / spanCount
            outRect.right = spaceSize - (column + 1) * spaceSize / spanCount
        }
        if (includeTBEdge) {
            if (position < spanCount) outRect.top = spaceSize // top edge
            outRect.bottom = spaceSize // item bottom
        } else {
            if (position >= spanCount) outRect.top = spaceSize // item top
        }
    }

    override fun onDraw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        mDivider?.run {
            val childCount = parent.childCount
            for (i in 0..childCount - 1) {
                val child = parent.getChildAt(i)
                //drawHorizontal
                val remainder = i % spanCount
                if (i >= spanCount) {
                    var left = child.left - spaceSize
                    if (remainder == 0) left = child.left
                    var right = child.right
//                    //add by lucio:解决最后一个非整除列item 有个小黑边框未封闭的问题
//                    if (i == childCount - 1 && (i + 1) % spanCount != 0) {
//                        right += spaceSize
//                    }
                    val top = child.top - spaceSize
                    val bottom = top + spaceSize
                    mDivider.setBounds(left, top, right, bottom)
                    mDivider.draw(c)
                }

                //drawVertical divider
                if (remainder != 0) {
                    val top = child.top
                    val bottom = child.bottom
                    val left = child.left - spaceSize
                    val right = left + spaceSize
                    mDivider.setBounds(left, top, right, bottom)
                    mDivider.draw(c)
                }
            }

//            drawHorizontal(c, parent)
//            drawVertical(c, parent)
            if (includeLREdge && drawLREdge) drawLR(c, parent)
            if (includeTBEdge && drawTBEdge) drawTB(c, parent)
        }
    }

    private fun drawLR(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView) {
        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)
            //最左边那条线
            if (i % spanCount == 0) {
                val left = child.left - spaceSize
                val right = left + spaceSize
                val bottom = child.bottom
                var top = child.top - spaceSize
                if (i == 0) top = child.top //【左上方】那一块交给drawTB绘制
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
            //最右边那条线
            if ((i + 1) % spanCount == 0) {
                val left = child.right
                val right = left + spaceSize
                val bottom = child.bottom
                var top = child.top - spaceSize
                if (i == spanCount - 1) top = child.top //【右上方】那一块交给drawTB绘制
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
        }
    }

    private fun drawTB(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView) {
        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)
            //最上边那条线
            if (i < spanCount) {
                val top = child.top - spaceSize
                val bottom = top + spaceSize
                val left = child.left
                var right = child.right + spaceSize
                if ((i + 1) % spanCount == 0 || childCount < spanCount && i == childCount - 1)
                    right = child.right  //上边最右边那条线已经绘制了
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
            //最下边那条线
            if (childCount % spanCount == 0 && i >= spanCount * (childCount / spanCount - 1)) {
                val top = child.bottom
                val bottom = top + spaceSize
                val left = child.left - spaceSize
                var right = child.right
                if ((i + 1) % spanCount == 0) right = child.right + spaceSize    //最右边那条线
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            } else if (i >= spanCount * (childCount / spanCount)) {
                val top = child.bottom
                val bottom = top + spaceSize
                val right = child.right
                var left = child.left - spaceSize
                if (!drawLREdge && i % spanCount == 0) left = child.left //最左边那条线
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
        }
    }

    /**
     * 网格分割线构造器
     */
    class Builder {
        internal var divider: Drawable? = null
        internal var spanCount: Int = 1
        internal var spaceSize: Int = 0
        internal var includeLREdge = false
        internal var includeTBEdge = false
        internal var drawLREdge = true
        internal var drawTBEdge = true
        /**
         * 设定分割线图片
         */
        fun setDivider(drawable: Drawable): Builder {
            divider = drawable
            return this
        }

        /**
         * 设置分割线颜色 与＃setDivider方法互斥
         */
        fun setDividerColor(color: Int): Builder {
            divider = ColorDrawable(color)
            return this
        }

        /**
         * 行数或列数
         */
        fun setSpanCount(count: Int): Builder {
            spanCount = count
            return this
        }

        /**
         * 行列间距大小
         */
        fun setSpaceSize(size: Int): Builder {
            spaceSize = size
            return this
        }

        /**
         * 是否包含左右边界
         */
        fun setIncludeLREdge(include: Boolean): Builder {
            includeLREdge = include
            return this
        }

        /**
         * 是否包含上下边界
         */
        fun setIncludeTBEdge(include: Boolean): Builder {
            includeTBEdge = include
            return this
        }

        /**
         * 是否绘制左右边界（默认绘制，如果为false，则不用分割线颜色绘制左右边界）
         */
        fun setDrawLREdge(draw: Boolean): Builder {
            drawLREdge = draw
            return this
        }

        /**
         * 是否绘制上下边界（默认绘制，如果为false，则不用分割线颜色绘制左右边界）
         */
        fun setDrawTBEdge(draw: Boolean): Builder {
            drawTBEdge = draw
            return this
        }

        fun build(): ItemDecorationGrid {
            return ItemDecorationGrid(this)
        }
    }
}