package andme.integration.support.recycler

import com.chad.library.adapter.base.module.LoadMoreModule

/**
 * Created by Lucio on 2020/12/16.
 */

private val DEFAULT_LOAD_MORE_VIEW = AMLoadMoreView()

abstract class AMLoadableAdapter<T, VH : AMViewHolder<T>> @JvmOverloads constructor(
        layoutResId: Int,
        data: MutableList<T>? = null
) : AMAdapter<T, VH>(layoutResId, data), LoadMoreModule {

    init {
        //自定义加载控件
        loadMoreModule.loadMoreView = DEFAULT_LOAD_MORE_VIEW
        //不足一屏时不执行加载更多
        loadMoreModule.isEnableLoadMoreIfNotFullPage = false
    }

    /**
     * 替换所有数据
     */
    fun replaceAll(data: List<T>?) {
        if (data.isNullOrEmpty()) {
            setNewInstance(null)
        } else {
            setNewInstance(data.toMutableList())
        }
    }
}