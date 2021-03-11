package andme.tv.leanback.multistate

import andme.lang.orDefault
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

/**
 * Created by Lucio on 2021/3/3.
 */
class LoadingFragment : BaseMultiStateFragment() {

    private var mLoadingView: LoadingView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mLoadingView = LoadingView((requireContext()))
        return mLoadingView
    }

    companion object {

        @JvmStatic
        fun newInstance(): LoadingFragment {
            return LoadingFragment()
        }
    }
}