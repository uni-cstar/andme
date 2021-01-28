package andme.arch.refresh.scene

import andme.arch.refresh.AMLoadMoreLayout
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.module.BaseLoadMoreModule
import com.chad.library.adapter.base.module.LoadMoreModule

/**
 * Created by Lucio on 2020/12/17.
 * SwipeRefreshLayout +  BRVAHAdapter 刷新场景
 */
open class AMSwipeRefreshBRVAHLayout : AMSwipeRefreshLayout, AMLoadMoreLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var mBrvahAdapter: BaseQuickAdapter<*, *>? = null

    private val loadMoreModule: BaseLoadMoreModule
        get() {
        if(mBrvahAdapter == null){
            throw IllegalStateException("请先调用attachAdapter方法")
        }
        return mBrvahAdapter!!.loadMoreModule
    }

    /**
     * 绑定BRVAH 适配器
     */
    fun attachAdapter(adapter: BaseQuickAdapter<*, *>) {
        if (adapter !is LoadMoreModule)
            throw IllegalArgumentException("your adapter don't implement LoadMoreModule interface.")
        this.mBrvahAdapter = adapter
    }

    fun detachAdapter() {
        this.mBrvahAdapter = null
    }

    override fun onLoadMoreSuccessAM(hasMore: Boolean) {
        if(hasMore){
            loadMoreModule.loadMoreComplete()
        }else{
            loadMoreModule.loadMoreEnd()
        }
    }

    override fun onLoadMoreFailAM(e: Throwable?) {
        loadMoreModule.loadMoreFail()
    }

    override fun setHasMoreAM(hasMore: Boolean) {
        if (!hasMore) {
            loadMoreModule.loadMoreEnd()
        } else {
            Log.w("AMWarn", "BRVAH 当前已设置没有更多数据时，调用此方法设置有更多数据会失效，如果要设置有更多，必须重新调用adapter的setNewInstance方法设置数据")
        }
    }

    override var enableLoadMoreAM: Boolean
        get() = loadMoreModule.isEnableLoadMore
        set(value) {
            loadMoreModule.isEnableLoadMore = value
        }

    override var isLoadingAM: Boolean = false
        get() = loadMoreModule.isLoading
        set(value) {

            if(value){
                loadMoreModule.loadMoreToLoading()
            }else{
                loadMoreModule.loadMoreComplete()
            }
            field = value
        }

    override fun setOnLoadMoreListenerAM(listener: AMLoadMoreLayout.OnLoadMoreListenerAM?) {
        loadMoreModule.setOnLoadMoreListener(createLoadMoreListener(listener))
    }

    private fun createLoadMoreListener(listener: AMLoadMoreLayout.OnLoadMoreListenerAM?): OnLoadMoreListener? {
        if (listener == null)
            return null
        return OnLoadMoreListener { listener.onLoadMoreAM() }
    }

}