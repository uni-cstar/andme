package andme.integration.support.recycler

import andme.integration.support.binding.bindTextOrGone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by Lucio on 2020-11-12.
 */
abstract class AMViewHolder<T>(view: View) : BaseViewHolder(view) {

    constructor(inflater: LayoutInflater, @LayoutRes layoutId: Int, parent: ViewGroup?)
            : this(inflater.inflate(layoutId, parent, false))

    abstract fun bindValue(data: T)

}

/**
 * databinding viewholder
 */
open class AMDataBindingVH<T, B : ViewDataBinding>(val mBinding: B, val variableId: Int) : AMViewHolder<T>(mBinding.root) {

    constructor(itemView: View, variableId: Int) : this(DataBindingUtil.getBinding<B>(itemView)!!, variableId)

    constructor(inflater: LayoutInflater, layoutId: Int, parent: ViewGroup?, variableId: Int) : this(DataBindingUtil.inflate<B>(inflater, layoutId, parent, false), variableId)

    override fun bindValue(data: T) {
        mBinding.setVariable(variableId, data)
    }
}

inline fun BaseViewHolder.setVisibleOrGone(@IdRes id: Int, visible: Boolean) {
    getViewOrNull<View>(id)?.visibility = if (visible) View.VISIBLE else View.GONE
}

inline fun BaseViewHolder.setVisibleOrNot(@IdRes id: Int, visible: Boolean) {
    getViewOrNull<View>(id)?.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

inline fun BaseViewHolder.setTextOrGone(@IdRes id:Int,text:CharSequence?){
    getViewOrNull<TextView>(id)?.also {
        bindTextOrGone(it,text)

    }
}