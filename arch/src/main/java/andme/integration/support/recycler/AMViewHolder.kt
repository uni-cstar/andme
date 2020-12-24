package andme.integration.support.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by Lucio on 2020-11-12.
 */
abstract class AMViewHolder<T>(view: View) : BaseViewHolder(view){

    constructor(inflater: LayoutInflater, @LayoutRes layoutId: Int, parent: ViewGroup?)
            : this(inflater.inflate(layoutId, parent, false))

    abstract fun bindValue(data:T)

}

inline fun BaseViewHolder.setVisibleOrGone(@IdRes id:Int,visible:Boolean){
    getViewOrNull<View>(id)?.visibility = if (visible) View.VISIBLE else View.GONE
}

inline fun BaseViewHolder.bindVisibleOrNot(@IdRes id: Int, visible: Boolean) {
    getViewOrNull<View>(id)?.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}