package andme.core.widget.recycler

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View

/**
 * Created by Lucio on 2018/5/30.
 */

class ItemDecorationLinearBlock private constructor(builder: Builder) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    val checker: BlockAttrChecker

    //上下边界
    internal var blockEdgeDrawable: Drawable? = null
    //边界线高度 默认1px
    internal var blockEdgeSize = 1

    //内部分割线
    internal var blockDividerDrawable: Drawable? = null

    //内部分割线 左边距 默认12px
    internal var blockDividerLeftMargin: Int = 12
    //内部分割线 右边距
    internal var blockDividerRightMargin: Int = 0
    internal var blockMarginDrawable: Drawable? = null

    //内部分割线 高度 默认1px
    internal var blockDividerSize = 1

    //分组
    internal var groupDividerDrawable: Drawable? = null
    //分组高度 默认12
    internal var groupDividerSize: Int = 12

    //在列表开始时先显示一个分组块
    internal var showFirstGroupDivider: Boolean = true

    //是否显示最后的分组块
    internal var showLastGroupDivider: Boolean = false

    init {
        checker = builder.checker
        blockEdgeDrawable = builder.blockEdgeDrawable
        blockEdgeSize = builder.blockEdgeSize
        blockDividerDrawable = builder.blockDividerDrawable
        blockDividerLeftMargin = builder.blockDividerLeftMargin
        blockDividerRightMargin = builder.blockDividerRightMargin
        blockDividerSize = builder.blockDividerSize
        groupDividerDrawable = builder.groupDividerDrawable
        groupDividerSize = builder.groupDividerSize
        showFirstGroupDivider = builder.showFirstGroupDivider
        showLastGroupDivider = builder.showLastGroupDivider
        blockMarginDrawable = builder.blockMarginDrawable
    }

    override fun onDraw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        parent.run {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                val position = getChildAdapterPosition(child)
                if (position < 0)//不是适配器中的元素，不绘制
                    continue

                val params = child.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
                var left = parent.paddingLeft
                var right = parent.width - parent.paddingRight

                if (checker.isTopEdge(position)) {
                    //绘制块分组线
                    var bottom = child.top - params.topMargin
                    var top = bottom

                    if (blockEdgeDrawable != null) {
                        top -= blockEdgeSize
                        blockEdgeDrawable?.setBounds(left, top, right, bottom)
                        blockEdgeDrawable?.draw(c)
                    }

                    //显示第一个分组快
                    if (isShowFirstDivider(position)) {
                        bottom = top
                        top -= groupDividerSize
                        groupDividerDrawable?.setBounds(left, top, right, bottom)
                        groupDividerDrawable?.draw(c)
                    }
                }

                if (checker.isBottomEdge(position)) {
                    //绘制下边线
                    var top = child.bottom + params.bottomMargin
                    var bottom = top

                    if (blockEdgeDrawable != null) {
                        bottom += blockEdgeSize
                        blockEdgeDrawable?.setBounds(left, top, right, bottom)
                        blockEdgeDrawable?.draw(c)
                    }

                    if (isShowGroupDivider(position)) {
                        //绘制块分组线
                        top = bottom
                        bottom += groupDividerSize
                        groupDividerDrawable?.setBounds(left, top, right, bottom)
                        groupDividerDrawable?.draw(c)
                    }
                } else {
                    if (isShowBlockDivider(position)) {//绘制块中线
                        val top = child.bottom + params.bottomMargin
                        val bottom = top + blockDividerSize
                        left += blockDividerLeftMargin
                        right -= blockDividerRightMargin

                        blockDividerDrawable?.setBounds(left, top, right, bottom)
                        blockDividerDrawable?.draw(c)
                        blockMarginDrawable?.run {
                            if (blockDividerLeftMargin > 0) {
                                //填充左侧透明区域
                                val leftMarginRight = left
                                val leftMarginLeft = leftMarginRight - blockDividerLeftMargin
                                setBounds(leftMarginLeft, top, leftMarginRight, bottom)
                                draw(c)
                            }

                            if (blockDividerRightMargin > 0) {
                                //填充右侧透明区域
                                val leftMarginLeft = right
                                val leftMarginRight = leftMarginLeft + blockDividerRightMargin
                                setBounds(leftMarginLeft, top, leftMarginRight, bottom)
                                draw(c)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 是否显示最开始的分组分割线
     */
    private fun isShowFirstDivider(position: Int): Boolean {
        return groupDividerDrawable != null && position == 0 && showFirstGroupDivider
    }

    /**
     * 是否显示块末分组分割线
     */
    private fun isShowGroupDivider(position: Int): Boolean {
        return groupDividerDrawable != null && (!checker.isLastItem(position) || (showLastGroupDivider))
    }

    /**
     * 是否显示块内分割线
     */
    private fun isShowBlockDivider(position: Int): Boolean {
        return blockDividerDrawable != null && checker.isBlockDivider(position)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)// item position
        outRect.run {
            left = 0
            right = 0
            top = 0
            bottom = 0

            if (checker.isTopEdge(position)) {//上边线
                //第一个分组快
                if (isShowFirstDivider(position)) {//第一个之前绘制分组块
                    top = groupDividerSize
                }
                //块始边线
                if (blockEdgeDrawable != null) {
                    top += blockEdgeSize
                }
            }

            if (isShowBlockDivider(position)) {//如果是块中的分割线
//                left = blockDividerLeftMargin
//                right = blockDividerRightMargin
                bottom = blockDividerSize
            }

            if (checker.isBottomEdge(position)) {
                //块末边线
                if (blockEdgeDrawable != null) {
                    bottom = blockEdgeSize
                }

                //分组块
                if (isShowGroupDivider(position))
                    bottom += groupDividerSize

            }
        }
    }




    interface BlockAttrChecker {
        /**
         * 逻辑实现参考：
         * 第一个元素 或者 当前元素与前一个元素的分组号不同 则应该绘制TopEdge
         */
        fun isTopEdge(positon: Int): Boolean

        /**
         * 逻辑实现参考：
         * 列表的最后一个元素 或者 当前元素与下一个元素的分组号不同 则应该绘制BottomEdge
         */
        fun isBottomEdge(positon: Int): Boolean

        /**
         * 逻辑实现参考：
         * 不是最后一个元素 并且当前元素与下一个元素的分组号相同 则应该绘制块分割线
         */
        fun isBlockDivider(positon: Int): Boolean

        //是否是最后一个元素
        fun isLastItem(positon: Int): Boolean
    }

    /**
     * 网格分割线构造器
     */
    class Builder(val checker: BlockAttrChecker) {

        //上下边界
        internal var blockEdgeDrawable: Drawable? = null
        //边界线高度 默认1px
        internal var blockEdgeSize = 1

        //内部分割线
        internal var blockDividerDrawable: Drawable? = null

        //内部分割线 左边距 默认12px
        internal var blockDividerLeftMargin: Int = 12
        //内部分割线 右边距
        internal var blockDividerRightMargin: Int = 0

        internal var blockMarginDrawable: Drawable? = null

        //内部分割线 高度 默认1px
        internal var blockDividerSize = 1

        //分组
        internal var groupDividerDrawable: Drawable? = null
        //分组高度 默认12
        internal var groupDividerSize: Int = 12

        //在列表开始时先显示一个分组块
        internal var showFirstGroupDivider: Boolean = true

        //是否显示最后的分组块
        internal var showLastGroupDivider: Boolean = false

        /**
         * 边框（上下边界）
         * @param size 分割线大小
         */
        fun setBlockEdge(drawable: Drawable): Builder {
            blockEdgeDrawable = drawable
            return this
        }

        fun setBlockEdgeColor(color: Int): Builder {
            return setBlockEdge(ColorDrawable(color))
        }

        fun setBlockEdgeSize(size: Int): Builder {
            blockEdgeSize = size
            return this
        }

        fun setBlockDivider(drawable: Drawable): Builder {
            blockDividerDrawable = drawable
            return this
        }

        fun setBlockDividerColor(color: Int): Builder {
            return setBlockDivider(ColorDrawable(color))
        }

        fun setBlockDividerLeftMargin(leftMargin: Int): Builder {
            blockDividerLeftMargin = leftMargin
            return this
        }

        /**
         * 如果leftmargin 或者 rightmargin 大于0，则margin区域将会透明，导致能够看到parent的背景，所以用设置的颜色填充
         */
        fun setBlockDividerMarginColor(color: Int): Builder {
            blockMarginDrawable = ColorDrawable(color)
            return this
        }

        fun setBlockDividerRightMargin(rightMargin: Int): Builder {
            blockDividerRightMargin = rightMargin
            return this
        }

        fun setBlockDividerSize(size: Int): Builder {
            blockDividerSize = size
            return this
        }

        fun setGroupDivider(drawable: Drawable): Builder {
            groupDividerDrawable = drawable
            return this
        }

        fun setGroupDividerColor(color: Int): Builder {
            return setGroupDivider(ColorDrawable(color))
        }

        fun setGroupDividerSize(size: Int): Builder {
            groupDividerSize = size
            return this
        }

        fun isShowFirstGroupDivider(show: Boolean): Builder {
            showFirstGroupDivider = show
            return this
        }

        fun isShowLastGroupDivider(show: Boolean): Builder {
            showLastGroupDivider = show
            return this
        }

        fun build(): ItemDecorationLinearBlock {
            return ItemDecorationLinearBlock(this)
        }

    }
}