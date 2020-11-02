package andme.core.ktx

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Lucio on 2020-11-01.
 */

/**
 * 快速运行一个Activity
 */
fun Fragment.startActivity(clazz: Class<out Activity>) {
    val it = Intent(this.requireContext(), clazz)
    startActivity(it)
}

/**
 * 获取与Activity生命周期相关的ViewModel
 */
inline fun <reified T : ViewModel> Fragment.obtainActivityViewModel(): T {
    return ViewModelProvider(
        requireActivity(),
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    ).get(T::class.java)
}

/**
 * 获取与自己生命周期相关的ViewModel
 */
inline fun <reified T : ViewModel> Fragment.obtainViewModel(): T {
    return ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    ).get(T::class.java)
}