package andme.arch.refresh

import andme.arch.app.AMViewModel
import andme.arch.app.AMViewModelOwnerDelegate
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment

/**
 * Created by Lucio on 2020/12/16.
 * 支持刷新和加载更多的 所有者（Activity/Fragment） 代理
 */
class AMViewModelRefreshableOwnerDelegate<VM:AMRefreshableViewModel>  : AMViewModelOwnerDelegate<VM>{

    private val refreshLayout:AMRefreshLayout

    constructor(activity: ComponentActivity,refreshLayout:AMRefreshLayout) : super(activity){
        this.refreshLayout = refreshLayout
    }

    constructor(fragment: Fragment,refreshLayout:AMRefreshLayout) : super(fragment){
        this.refreshLayout = refreshLayout
    }

    override fun registerViewModelEvent(viewModel: AMViewModel) {
        super.registerViewModelEvent(viewModel)
        if(viewModel is AMRefreshableViewModel){
            registerRefreshEvent(viewModel)
        }
    }

    protected open fun registerRefreshEvent(viewModel: AMRefreshableViewModel){
        val lifeOwner = getLifecycleOwner()
        viewModel.refreshSuccessEvent.observe(lifeOwner, {
            refreshLayout.onRefreshSuccess()
        })

        viewModel.refreshSuccessEvent2.observe(lifeOwner,{
            refreshLayout.onRefreshSuccess(it)
        })

        viewModel.refreshFailEvent.observe(lifeOwner,{
            refreshLayout.onRefreshFail(it)
        })

        viewModel.loadMoreSuccessEvent.observe(lifeOwner,{
            refreshLayout.onLoadMoreSuccess(it)
        })

        viewModel.loadMoreFailEvent.observe(lifeOwner,{
            refreshLayout.onLoadMoreFail(it)
        })

        viewModel.hasMoreEvent.observe(lifeOwner,{
            refreshLayout.setHasMore(it)
        })
    }

}