package andme.core.fragment

import andme.lang.runOnTrue
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Created by Lucio on 2021/5/13.
 */
fun FragmentManager.switchFragment(
    fromFragmentTag: String,
    toFragmentTag: String,
    containerViewId: Int,
    toFragmentCreator: () -> Fragment
) {
    val fromFragment = findFragmentByTag(fromFragmentTag)
    switchFragment(fromFragment, toFragmentTag, containerViewId, toFragmentCreator)
}
fun FragmentManager.switchFragment(
    fromFragment: Fragment?,
    toFragmentTag: String,
    containerViewId: Int,
    toFragmentCreator: () -> Fragment
) {
    var toFragment = findFragmentByTag(toFragmentTag)

    if (toFragment == null) {
        toFragment = toFragmentCreator.invoke()
    }
    switchFragment(fromFragment,containerViewId, toFragmentTag, toFragment)
}


/**
 * @param fromFragment 当前fragment
 * @param toFragment 要切换的Fragment
 */
@JvmOverloads
fun FragmentManager.switchFragment(
    fromFragment: Fragment?,
    containerViewId: Int,
    toFragmentTag: String? = null,
    toFragment: Fragment
) {
    switchFragment(fromFragment, containerViewId, toFragmentTag, toFragment, false, null)
}

/**
 * @param fromFragment 当前fragment
 * @param toFragment 要切换的Fragment
 */
fun FragmentManager.switchFragment(
    fromFragment: Fragment?,
    containerViewId: Int,
    toFragmentTag: String?,
    toFragment: Fragment,
    addToBackStack: Boolean,
    backStackTag: String?
) {
    if (fromFragment == toFragment)
        return

    val ft = beginTransaction()

    if (!toFragment.isAdded) {
        ft.add(containerViewId, toFragment, toFragmentTag)
    } else {
        ft.show(toFragment)
    }

    fromFragment?.let {
        ft.hide(fromFragment)
    }

    runOnTrue(addToBackStack) {
        ft.addToBackStack(backStackTag)
    }
    ft.commit()
}
