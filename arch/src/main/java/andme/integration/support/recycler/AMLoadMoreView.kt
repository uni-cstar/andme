package andme.integration.support.recycler

import andme.arch.R
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.util.getItemView
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by Lucio on 2020-08-11.
 */
class AMLoadMoreView : BaseLoadMoreView() {

    override fun getRootView(parent: ViewGroup): View {
        return parent.getItemView(R.layout.am_itg_recycer_loadmore_view)
    }

    override fun getLoadingView(holder: BaseViewHolder): View =
            holder.getView(R.id.am_itg_load_progress_layout)

    override fun getLoadComplete(holder: BaseViewHolder): View =
            holder.getView(R.id.am_itg_load_complete_layout)

    override fun getLoadEndView(holder: BaseViewHolder): View =
            holder.getView(R.id.am_itg_load_no_more_layout)

    override fun getLoadFailView(holder: BaseViewHolder): View =
            holder.getView(R.id.am_itg_load_fail_layout)
}