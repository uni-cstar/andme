package andme.core.paging

import andme.core.content.layoutInflater
import android.content.Context
import android.view.LayoutInflater
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Lucio on 2021/3/22.
 */
abstract class AMPagingDataAdapter<T : Any, VH : RecyclerView.ViewHolder> @JvmOverloads constructor(
    diffCallback: DiffUtil.ItemCallback<T>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default
) : PagingDataAdapter<T, VH>(diffCallback, mainDispatcher, workerDispatcher) {

    lateinit var inflater: LayoutInflater
        private set

    lateinit var context: Context
        private set

    private var mOnItemClick: OnItemClickListener<T>? = null

    init {
        addLoadStateListener {
            onLoadStateChanged(it)
        }
    }

    protected open fun onLoadStateChanged(state: CombinedLoadStates) {

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
        inflater = context.layoutInflater
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T>?) {
        mOnItemClick = listener
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        mOnItemClick?.let { onItemClick ->
            holder.itemView.setOnClickListener {
                onItemClick.onItemClick(getItem(position)!!, holder, position)
            }
        }
        onBindViewHolderImpl(holder, position)
    }

    protected abstract fun onBindViewHolderImpl(holder: VH, position: Int)

    fun interface OnItemClickListener<T> {
        fun onItemClick(data: T, viewHolder: RecyclerView.ViewHolder, position: Int)
    }

}