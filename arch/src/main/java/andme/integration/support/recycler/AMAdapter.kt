package andme.integration.support.recycler

import andme.core.content.layoutInflater
import android.view.LayoutInflater
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Created by Lucio on 2020-08-12.
 */

abstract class AMAdapter<T, VH : AMViewHolder<T>> @JvmOverloads constructor(
        layoutResId: Int,
        data: MutableList<T>? = null
) : BaseQuickAdapter<T, VH>(layoutResId, data) {

    protected val inflater: LayoutInflater by lazy {
        context.layoutInflater
    }

    /**
     * 查找指定条件的数据
     */
    open fun find(predicate: (T) -> Boolean): T? {
        return data.find(predicate)
    }

    /**
     * 移除指定条件的数据，并刷新
     */
    open fun remove(predicate: (T) -> Boolean) {
        val index = this.data.indexOfFirst(predicate)
        if (index < 0) {
            return
        }
        removeAt(index)
    }

    override fun convert(holder: VH, item: T) {
        holder.bindValue(item)
    }
}