package andme.tv.leanback.presenter

import androidx.leanback.widget.ListRowPresenter

/**
 * Created by Lucio on 2021/4/29.
 */
abstract class BaseListRowPresenter : ListRowPresenter {
    constructor() : super()
    constructor(focusZoomFactor: Int) : super(focusZoomFactor)
    constructor(focusZoomFactor: Int, useFocusDimmer: Boolean) : super(
        focusZoomFactor,
        useFocusDimmer
    )

    /**
     * 去掉HorizontalGridView默认选中效果；
     * 如果启用该效果目前发现会存在以下问题：
     * 1、item的放大动画是以控件的左侧和上侧为缩放起点向右侧和底部进行放大，而不是居中显示
     * 2、item中定义的跑马灯textview在选中的时候无效果
     */
    override fun isUsingDefaultListSelectEffect(): Boolean {
        return false
    }
}