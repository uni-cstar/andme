package andme.arch.refresh

import andme.arch.app.AMViewModel
import andme.arch.app.AMViewModelOwner
import andme.arch.app.AMViewModelOwnerDelegate
import androidx.lifecycle.ViewModel

/**
 * Created by Lucio on 2020/12/16.
 * 支持刷新和加载更多的 所有者（Activity/Fragment） 代理
 */
class AMViewModelRefreshableOwnerDelegate<VM : ViewModel>(val refreshLayoutProvider: AMRefreshLayoutProvider, realOwner: AMViewModelOwner) : AMViewModelOwnerDelegate<VM>(realOwner) {

    override fun registerViewModelEvent(viewModel: AMViewModel) {
        super.registerViewModelEvent(viewModel)
        if (viewModel is AMRefreshableViewModel) {
            registerRefreshEvent(viewModel)
        }
    }

    private val refreshLayout get() = refreshLayoutProvider.getRefreshLayout()

    private val loadMoreLayout get() = refreshLayoutProvider.getLoadMoreLayout()

    protected open fun registerRefreshEvent(viewModel: AMRefreshableViewModel) {
        viewModel.refreshSuccessEvent.observe(this, {
            refreshLayout?.onRefreshSuccessAM()
        })

        viewModel.refreshFailEvent.observe(this, {
            refreshLayout?.onRefreshFailAM(it)
        })

        viewModel.loadMoreSuccessEvent.observe(this, {
            loadMoreLayout?.onLoadMoreSuccessAM(it)
        })

        viewModel.loadMoreFailEvent.observe(this, {
            loadMoreLayout?.onLoadMoreFailAM(it)
        })

        viewModel.hasMoreEvent.observe(this, {
            loadMoreLayout?.setHasMoreAM(it)
        })
    }

}