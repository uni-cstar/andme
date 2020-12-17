package andme.arch.refresh

import andme.arch.app.AMActivity
import andme.arch.app.AMViewModelOwnerDelegate

/**
 * Created by Lucio on 2020/12/16.
 */
abstract class AMRefreshableActivity<VM : AMRefreshableViewModel> : AMActivity<VM>(), AMRefreshableViewModelOwner {

    override val viewModelDelegate: AMViewModelOwnerDelegate<VM> = AMViewModelRefreshableOwnerDelegate<VM>(this,this)
    
}