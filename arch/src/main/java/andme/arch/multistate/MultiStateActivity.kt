package andme.arch.multistate

import andme.arch.app.AMActivity
import andme.core.statelayout.StateLayout
import android.os.Bundle

/**
 * Created by Lucio on 2021/2/25.
 */
abstract class MultiStateActivity<VM : MultiStateViewModel> :
    AMActivity<VM>() {

    protected lateinit var multiStateLayoutViewModel: MultiStateLayoutViewModel

    protected abstract val multiStateLayout: StateLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMultiStateLayoutViewModel(savedInstanceState)
    }

    private fun initMultiStateLayoutViewModel(savedInstanceState: Bundle?) {
        if (::multiStateLayoutViewModel.isInitialized)
            return
        multiStateLayoutViewModel = getOrCreateMultiStateViewModel(savedInstanceState)

        multiStateLayoutViewModel.stateLayoutActionEvent.let {
            it.removeObservers(this)
            it.observe(this, {
                it.onStateLayoutActionEvent(multiStateLayout)
            })
        }
        viewModel.bindMultiStateLayoutViewModel(multiStateLayoutViewModel)
    }

    protected open fun getOrCreateMultiStateViewModel(savedInstanceState: Bundle?): MultiStateLayoutViewModel {
        return obtainViewModel(MultiStateLayoutViewModel::class.java)
    }
}