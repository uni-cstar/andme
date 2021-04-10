package andme.core.paging

import andme.core.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter

/**
 * Created by Lucio on 2021/3/21.
 */
class AMPagingLoadMoreViewInGrid @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val loadingLayout: View
    val notLoadingLayout: View
    val failLayout: View
    val nomoreLayout: View

    val nomoreText: TextView
    val notloadingText: TextView


    init {
        inflate(context, R.layout.am_paging_loadmore_view_in_grid, this)

        loadingLayout = findViewById<View>(R.id.am_paging_loading_layout)
        notLoadingLayout = findViewById<View>(R.id.am_paging_complete_layout)
        failLayout = findViewById<View>(R.id.am_paging_fail_layout)
        nomoreLayout = findViewById<View>(R.id.am_paging_no_more_layout)

        nomoreText = findViewById<TextView>(R.id.am_paging_no_more_text)
        notloadingText = findViewById<TextView>(R.id.am_paging_complete_text)

        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
    }

    /**
     */
    var loadState: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
        set(loadState) {
            if (field != loadState) {
                val oldItem = shouldDisplay(field)
                val newItem = shouldDisplay(loadState)
                applyState(loadState)
                if (oldItem && !newItem) {
                    visibility = View.GONE
                } else if (newItem && !oldItem) {
                    visibility = View.VISIBLE
                } else if (oldItem && newItem) {
                }
                field = loadState
            }
        }

    private fun applyState(loadState: LoadState) {
        loadingLayout.isVisible = loadState is LoadState.Loading
        notLoadingLayout.isVisible =
            loadState is LoadState.NotLoading && !loadState.endOfPaginationReached
        nomoreLayout.isVisible =
            loadState is LoadState.NotLoading && loadState.endOfPaginationReached
        failLayout.isVisible = loadState is LoadState.Error

        val isPreviousFocused = this.isFocused
        val isFocusable =   loadState is LoadState.Error // || (loadState is LoadState.NotLoading && !loadState.endOfPaginationReached)

        this.isFocusable = isFocusable
        this.isFocusableInTouchMode = isFocusable
        this.isClickable = isFocusable

        //放弃焦点
        if(isPreviousFocused && !isFocusable){
            clearFocus()
        }
    }

    private val mLoadStateListener: (CombinedLoadStates) -> Unit = { state ->
        this.loadState = state.append
    }

    /**
     * 获取当前控件的加载状态监听；
     * 调用[PagingDataAdapter.addLoadStateListener]方法关联此回调，或者调用[bindAdapter]方法绑定适配器
     */
    fun getLoadStateListener(): (CombinedLoadStates) -> Unit {
        return mLoadStateListener
    }

    /**
     * 绑定Adapter,
     * 此方法用于单独使用View，而不是作为[PagingDataAdapter]的Footer情况，
     * 如需作为[PagingDataAdapter]的FooterAdapter，请使用[AMPagingLoadFooterAdapter]即可
     */
    fun bindAdapter(adapter: PagingDataAdapter<*, *>) {
        adapter.removeLoadStateListener(mLoadStateListener)
        adapter.addLoadStateListener(mLoadStateListener)
    }

//    /**
//     * 绑定Adapter,
//     * 此方法用于单独使用View，而不是作为[PagingDataAdapter]的Footer情况
//     * 此方法绑定了rv的滚动监听，因为Paging具备预加载，在作为单独的View使用时，如果Paging处于预加载情况，则会导致该view显示
//     */
//    fun bindAdapter(adapter: PagingDataAdapter<*, *>, rv: RecyclerView) {
//        bindAdapter(adapter)
//        rv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//
//        })
//    }

    /**
     * 是否显示
     */
    protected open fun shouldDisplay(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading || loadState is LoadState.Error //|| (loadState.endOfPaginationReached && loadState is LoadState.NotLoading)
    }

    private inline var View.isVisible: Boolean
        get() = visibility == View.VISIBLE
        set(value) {
            visibility = if (value) View.VISIBLE else View.GONE
        }
}