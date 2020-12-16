package andme.integration.support.recycler

import com.chad.library.adapter.base.module.LoadMoreModule

/**
 * Created by Lucio on 2020/12/16.
 */
abstract class AMLoadableAdapter<T, VH : AMViewHolder<T>> constructor(
        layoutResId: Int,
        data: MutableList<T>? = null
) : AMAdapter<T, VH>(layoutResId, data), LoadMoreModule {

    init {
        //自定义加载控件
        loadMoreModule.loadMoreView = AMLoadMoreView()
        //不足一屏时不执行加载更多
        loadMoreModule.isEnableLoadMoreIfNotFullPage = false
    }

}