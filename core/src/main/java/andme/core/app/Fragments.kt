package ucux.tv.core

import andme.lang.runOnTrue
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * Created by Lucio on 2021/3/4.
 */

fun FragmentManager.showFragment(tag: String, containerId: Int, creator: () -> Fragment) {
    showFragment(tag, containerId, false, creator)
}

fun FragmentManager.showFragment(
    tag: String,
    containerId: Int,
    addToBackStack: Boolean,
    creator: () -> Fragment
) {
    val cacheFragment = findFragmentByTag(tag)
    if (cacheFragment != null) {
        if (cacheFragment.isVisible)
            return
        beginTransaction().show(cacheFragment)
            .runOnTrue(addToBackStack) {
                addToBackStack(tag)
            }.commit()
        return
    }

    beginTransaction().add(
        containerId, creator(), tag
    ).runOnTrue(addToBackStack) {
        addToBackStack(tag)
    }.commit()
}

fun FragmentManager.removeFragment(tag: String) {
    val fragment = findFragmentByTag(tag) ?: return
    beginTransaction().remove(fragment).commit()
}

fun FragmentManager.removeFragmentFromBackStack(tag: String) {
    val fragment = findFragmentByTag(tag) ?: return
    popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun FragmentActivity.showFragment(tag: String, containerId: Int, creator: () -> Fragment) {
    supportFragmentManager.showFragment(tag, containerId, creator)
}

fun FragmentActivity.removeFragment(tag: String) {
    supportFragmentManager.removeFragment(tag)
}

fun Fragment.showFragment(tag: String, containerId: Int, creator: () -> Fragment) {
    childFragmentManager.showFragment(tag, containerId, creator)
}

fun Fragment.removeFragment(tag: String) {
    childFragmentManager.removeFragment(tag)
}

