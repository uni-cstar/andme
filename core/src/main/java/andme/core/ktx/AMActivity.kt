package andme.core.ktx

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Lucio on 2020-11-01.
 */


/**
 * 获取与自己生命周期相关的ViewModel
 */
fun <T : AndroidViewModel> FragmentActivity.obtainViewModel(clz: Class<T>): T {
    return ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
    ).get(clz)


}

/**
 * 快速运行一个Activity
 */
fun Context.startActivity(clazz: Class<out Activity>) {
    val it = Intent(this, clazz)
    if(this !is Activity)
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(it)
}

