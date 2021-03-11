package andme.tv.leanback.multistate

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Lucio on 2021/3/3.
 */
open class BaseMultiStateFragment : Fragment() {

    protected val multiStateViewModel: MultiStateViewModel
        get() {
            return ViewModelProvider(requireActivity(),  ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(MultiStateViewModel::class.java)
        }
}