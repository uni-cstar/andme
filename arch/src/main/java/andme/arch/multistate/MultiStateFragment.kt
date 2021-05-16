package andme.arch.multistate

import andme.arch.app.AMFragment
import andme.core.statelayout.StateLayout
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

/**
 * Created by Lucio on 2021/2/25.
 */
abstract class MultiStateFragment<VM : MultiStateViewModel> : AMFragment<VM>() {

    protected lateinit var multiStateLayoutViewModel: MultiStateLayoutViewModel
    
    protected abstract val multiStateLayout:StateLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initMultiStateViewModel(savedInstanceState)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initMultiStateViewModel(savedInstanceState: Bundle?) {
        if (::multiStateLayoutViewModel.isInitialized)
            return
        multiStateLayoutViewModel = getOrCreateMultiStateViewModel(savedInstanceState)

        multiStateLayoutViewModel.stateLayoutActionEvent.let {
            it.removeObservers(this)
            it.observe(this, Observer {
                it.onStateLayoutActionEvent(multiStateLayout)
            })
        }
        viewModel.bindMultiStateLayoutViewModel(multiStateLayoutViewModel)
    }

    protected open fun getOrCreateMultiStateViewModel(savedInstanceState: Bundle?): MultiStateLayoutViewModel {
        return obtainViewModel(MultiStateLayoutViewModel::class.java)
    }
}