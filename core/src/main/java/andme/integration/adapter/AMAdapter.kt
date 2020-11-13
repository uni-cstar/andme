package andme.integration.adapter

import andme.core.content.layoutInflater
import android.view.LayoutInflater
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule

/**
 * Created by Lucio on 2020-08-12.
 */

abstract class AMAdapter<T, VH : AMViewHolder> @JvmOverloads constructor(
    layoutResId: Int,
    data: MutableList<T>? = null
) :
    BaseQuickAdapter<T, VH>(layoutResId, data), LoadMoreModule {

    protected val inflater: LayoutInflater by lazy {
        context.layoutInflater
    }
//
//    protected val inflater: LayoutInflater by lazy {
//        context.layoutInflater
//    }
//
//    init {
//        //自定义加载控件
//        loadMoreModule.loadMoreView = AndmeBRVAHLoadMoreView()
//        //不足一屏时不执行加载更多
//        loadMoreModule.isEnableLoadMoreIfNotFullPage = false
//    }
//
//    override fun convert(holder: VH, item: T) {
//        bindViewHolder(holder, item)
//    }
//
//    protected open fun bindViewHolder(holder: VH, data: T) {
//        holder.bindData(data)
//    }

    open fun find(predicate: (T) -> Boolean): T? {
        return data.find(predicate)
    }

    open fun remove(predicate: (T) -> Boolean) {
        val index = this.data.indexOfFirst(predicate)
        if (index < 0) {
            return
        }
        removeAt(index)
    }
}