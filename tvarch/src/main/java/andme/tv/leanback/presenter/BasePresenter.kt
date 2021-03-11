package andme.tv.leanback.presenter

import andme.core.content.layoutInflater
import andme.tv.arch.R
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.leanback.widget.Presenter

/**
 * Created by Lucio on 2021/3/10.
 */
abstract class BasePresenter : Presenter() {

    private var mItemClickListener: OnItemClickListener? = null

    private val mItemClickProvider: ItemClickProvider = object : ItemClickProvider {
        override fun getItemClickListener(): OnItemClickListener? {
            return mItemClickListener
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    final override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        if (mItemClickListener != null) {
            var listener =
                viewHolder.view.getTag(R.id.amtv_presenter_item_click) as? ItemClickListenerHolder

            if (listener == null) {
                listener = ItemClickListenerHolder(mItemClickProvider, viewHolder, item)
                viewHolder.view.setOnClickListener(listener)
                viewHolder.view.setTag(R.id.amtv_presenter_item_click, listener)
            } else {
                listener.data = item
            }
        }
        bindViewHolder(viewHolder, item)
    }

    protected  fun < VDB : ViewDataBinding> createBindingViewHolder(
        parent: ViewGroup,
        layoutId: Int
    ): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate<VDB>(
                parent.context.layoutInflater,
                layoutId,
                parent,
                false
            ).root
        )
    }

    protected fun <VDB: ViewDataBinding> getBinding(viewHolder: ViewHolder): VDB? {
        return DataBindingUtil.getBinding<VDB>(viewHolder.view)
    }

    protected fun createViewHolder(
        parent: ViewGroup,
        layoutInd: Int
    ): ViewHolder {
        return ViewHolder(
            parent.context.layoutInflater.inflate(
                layoutInd,
                parent,
                false
            )
        )
    }

    fun interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder, item: Any?)
    }

    private interface ItemClickProvider {
        fun getItemClickListener(): OnItemClickListener?
    }

    private class ItemClickListenerHolder(
        val provider: ItemClickProvider,
        var holder: ViewHolder,
        var data: Any?
    ) : View.OnClickListener {
        override fun onClick(v: View?) {
            provider.getItemClickListener()?.onItemClick(holder, data)
        }
    }

    protected abstract fun bindViewHolder(viewHolder: ViewHolder, item: Any)


}