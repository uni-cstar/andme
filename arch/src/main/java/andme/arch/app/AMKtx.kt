package andme.arch.app

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Lucio on 2020/12/16.
 */

/**
 * 获取与自己生命周期相关的ViewModel
 */
inline fun <reified T : ViewModel> ComponentActivity.obtainViewModel(): T {
    if(this is AMActivity<*>){
        return this.obtainViewModel(T::class.java)
    }else{
        return ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(T::class.java)
    }
}



/**
 * 获取与Activity生命周期相关的ViewModel
 */
inline fun <reified T : ViewModel> Fragment.obtainActivityViewModel(): T {
    if(this is AMFragment<*>){
        return this.obtainActivityViewModel(T::class.java)
    }else{
        return ViewModelProvider(
                requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(T::class.java)
    }
}

/**
 * 获取与自己生命周期相关的ViewModel
 */
inline fun <reified T : ViewModel> Fragment.obtainViewModel(): T {
    if(this is AMFragment<*>){
        return this.obtainViewModel(T::class.java)
    }else{
        return ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(T::class.java)
    }
}