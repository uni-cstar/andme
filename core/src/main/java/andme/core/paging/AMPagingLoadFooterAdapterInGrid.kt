package andme.core.paging

import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Lucio on 2021/3/21.
 */
class AMPagingLoadFooterAdapterInGrid : LoadStateAdapter<AMPagingLoadFooterAdapterInGrid.StateViewHolder>() {

    private var mOnRetry:((View)->Unit)? = null

    fun setOnRetryClick(onClick:(View)->Unit){
        mOnRetry = onClick
    }

    override fun onBindViewHolder(holder: StateViewHolder, loadState: LoadState) {
        holder.loadMoreView.loadState = loadState
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): StateViewHolder {
        return StateViewHolder(AMPagingLoadMoreViewInGrid(parent.context))
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return super.displayLoadStateAsItem(loadState)// || (loadState.endOfPaginationReached && loadState is LoadState.NotLoading)
    }

    class StateViewHolder(val loadMoreView: AMPagingLoadMoreViewInGrid) : RecyclerView.ViewHolder(loadMoreView)

}