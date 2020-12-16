package andme.arch.refresh

import andme.arch.app.AMViewModel
import andme.arch.app.AMViewModelOwnerDelegate

/**
 * Created by Lucio on 2020/12/16.
 * 支持刷新和加载更多的 所有者（Activity/Fragment） 代理
 */
class AMViewModelRefreshableOwnerDelegate<VM : AMRefreshableViewModel>(override val realOwner:AMRefreshableViewModelOwner) : AMViewModelOwnerDelegate<VM>(realOwner) {

    override fun registerViewModelEvent(viewModel: AMViewModel) {
        super.registerViewModelEvent(viewModel)
        if (viewModel is AMRefreshableViewModel) {
            registerRefreshEvent(viewModel)
        }
    }

    private val refreshLayout get() = realOwner.getRefreshLayout()

    protected open fun registerRefreshEvent(viewModel: AMRefreshableViewModel) {
        viewModel.refreshSuccessEvent.observe(this, {
            refreshLayout.onRefreshSuccess()
        })

        viewModel.refreshSuccessEvent2.observe(this, {
            refreshLayout.onRefreshSuccess(it)
        })

        viewModel.refreshFailEvent.observe(this, {
            refreshLayout.onRefreshFail(it)
        })

        viewModel.loadMoreSuccessEvent.observe(this, {
            refreshLayout.onLoadMoreSuccess(it)
        })

        viewModel.loadMoreFailEvent.observe(this, {
            refreshLayout.onLoadMoreFail(it)
        })

        viewModel.hasMoreEvent.observe(this, {
            refreshLayout.setHasMore(it)
        })
    }

}