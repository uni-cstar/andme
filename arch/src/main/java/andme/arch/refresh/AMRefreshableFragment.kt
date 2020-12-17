package andme.arch.refresh

import andme.arch.app.AMFragment
import andme.arch.app.AMViewModelOwnerDelegate

/**
 * Created by Lucio on 2020/12/16.
 */
abstract class AMRefreshableFragment<VM : AMRefreshableViewModel> : AMFragment<VM>(), AMRefreshableViewModelOwner {

    override val viewModelDelegate: AMViewModelOwnerDelegate<VM> = AMViewModelRefreshableOwnerDelegate<VM>(this,this)

}