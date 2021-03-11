package andme.tv.leanback.presenter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

/**
 * Created by Lucio on 2021/3/2.
 */
open class BaseBindingPresenter<VDB : ViewDataBinding>(
    val layoutId: Int,
    val variableId:Int
) : BasePresenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return createBindingViewHolder<VDB>(parent,layoutId)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {

    }

    override fun bindViewHolder(viewHolder: ViewHolder, item: Any) {
        getBinding<VDB>(viewHolder)?.let {
            bindBindingData(it,item)
        }
    }


    open protected fun bindBindingData(binding: VDB, item: Any){
        binding.setVariable(variableId,item)
    }

}