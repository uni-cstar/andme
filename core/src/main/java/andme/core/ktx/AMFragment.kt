package andme.core.ktx

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

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
