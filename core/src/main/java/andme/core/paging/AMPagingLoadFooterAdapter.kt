package andme.core.paging

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Lucio on 2021/3/21.
 */
class AMPagingLoadFooterAdapter : LoadStateAdapter<AMPagingLoadFooterAdapter.StateViewHolder>() {



    override fun onBindViewHolder(holder: StateViewHolder, loadState: LoadState) {
        holder.loadMoreView.loadState = loadState
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): StateViewHolder {
        return StateViewHolder(AMPagingLoadMoreView(parent.context))
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return super.displayLoadStateAsItem(loadState) || (loadState.endOfPaginationReached && loadState is LoadState.NotLoading)
    }

    class StateViewHolder(val loadMoreView: AMPagingLoadMoreView) : RecyclerView.ViewHolder(loadMoreView)

}