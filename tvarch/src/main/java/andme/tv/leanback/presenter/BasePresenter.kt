package andme.tv.leanback.presenter

import andme.core.content.layoutInflater
import andme.tv.arch.R
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.leanback.widget.Presenter

/**
 * Created by Lucio on 2021/3/10.
 */
abstract class BasePresenter : Presenter() {

    private var mItemClickListener: OnItemClickListener? = null
    private var mItemFocusChangedListener: OnItemFocusChangedListener? = null


    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mItemClickListener = listener
    }

    fun setOnItemFocusChangedListener(listener: OnItemFocusChangedListener?) {
        mItemFocusChangedListener = listener
    }

    fun getOnItemFocusChangedListener(): OnItemFocusChangedListener? {
        return mItemFocusChangedListener
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

        if (mItemFocusChangedListener != null) {
            var listenerHolder =
                viewHolder.view.getTag(R.id.amtv_presenter_item_focus_changed) as? ItemFocusChangedListenerHolder
            if (listenerHolder == null) {
                listenerHolder = ItemFocusChangedListenerHolder(viewHolder, mItemFocusProvider)
                listenerHolder.mChainFocusChangedListener = viewHolder.view.onFocusChangeListener
                viewHolder.view.onFocusChangeListener = listenerHolder
                viewHolder.view.setTag(R.id.amtv_presenter_item_focus_changed, listenerHolder)
            }
            listenerHolder.bindData(item)
        }

        bindViewHolder(viewHolder, item)
    }

    protected fun <VDB : ViewDataBinding> createBindingViewHolder(
        parent: ViewGroup,
        layoutId: Int
    ): BaseBindingViewHolder<VDB> {
        return BaseBindingViewHolder<VDB>(parent.context.layoutInflater, layoutId, parent)
    }

    protected fun <VDB : ViewDataBinding> getBinding(viewHolder: ViewHolder): VDB? {
        return DataBindingUtil.getBinding<VDB>(viewHolder.view)
    }

    protected fun createViewHolder(
        parent: ViewGroup,
        layoutId: Int
    ): BaseViewHolder {
        return BaseViewHolder(
            parent.context.layoutInflater.inflate(
                layoutId,
                parent,
                false
            )
        )
    }

    open class BaseBindingViewHolder<VDB : ViewDataBinding>(val mBinding: VDB) :
        BaseViewHolder(mBinding.root) {

        constructor(layoutId: Int, parent: ViewGroup) : this(
            DataBindingUtil.inflate<VDB>(
                parent.context.layoutInflater,
                layoutId,
                parent,
                false
            )
        )

        constructor(inflater: LayoutInflater, layoutId: Int, parent: ViewGroup) : this(
            DataBindingUtil.inflate<VDB>(
                inflater,
                layoutId,
                parent,
                false
            )
        )

    }

    open class BaseViewHolder(view: View) : ViewHolder(view) {

        private val itemView = view

        /**
         * Views indexed with their IDs
         */
        private val views: SparseArray<View> = SparseArray()

        constructor(layoutId: Int, parent: ViewGroup) : this(
            parent.context.layoutInflater,
            layoutId,
            parent
        )

        constructor(inflater: LayoutInflater, layoutId: Int, parent: ViewGroup) :
                this(inflater.inflate(layoutId, parent, false))

        open fun <T : View> getView(@IdRes viewId: Int): T {
            val view = getViewOrNull<T>(viewId)
            checkNotNull(view) { "No view found with id $viewId" }
            return view
        }

        @Suppress("UNCHECKED_CAST")
        open fun <T : View> getViewOrNull(@IdRes viewId: Int): T? {
            val view = views.get(viewId)
            if (view == null) {
                itemView.findViewById<T>(viewId)?.let {
                    views.put(viewId, it)
                    return it
                }
            }
            return view as? T
        }

    }

    fun interface OnItemClickListener {
        fun onItemClick(viewHolder: ViewHolder, item: Any?)
    }

    fun interface OnItemFocusChangedListener {
        fun onItemFocusChanged(view: View, hasFocus: Boolean, viewHolder: ViewHolder, item: Any?)
    }

    private interface ItemClickProvider {
        fun getItemClickListener(): OnItemClickListener?
    }

    private interface ItemFocusChangedListenerProvider {
        fun getItemFocusChangedListener(): OnItemFocusChangedListener?
    }

    private val mItemClickProvider: ItemClickProvider = object : ItemClickProvider {
        override fun getItemClickListener(): OnItemClickListener? {
            return mItemClickListener
        }
    }

    private val mItemFocusProvider: ItemFocusChangedListenerProvider =
        object : ItemFocusChangedListenerProvider {
            override fun getItemFocusChangedListener(): OnItemFocusChangedListener? {
                return mItemFocusChangedListener
            }
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

    private class ItemFocusChangedListenerHolder(
        val viewHolder: ViewHolder,
        val provider: ItemFocusChangedListenerProvider
    ) :
        View.OnFocusChangeListener {

        private var mData: Any? = null

        internal var mChainFocusChangedListener: View.OnFocusChangeListener? = null

        fun bindData(data: Any?) {
            mData = data
        }

        override fun onFocusChange(v: View, hasFocus: Boolean) {
            provider.getItemFocusChangedListener()
                ?.onItemFocusChanged(v, hasFocus, viewHolder, mData)
            mChainFocusChangedListener?.onFocusChange(v, hasFocus)
        }
    }

    protected abstract fun bindViewHolder(viewHolder: ViewHolder, item: Any)


}