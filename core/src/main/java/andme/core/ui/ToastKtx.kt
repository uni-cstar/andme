package andme.core.ui

import andme.core.toastHandler
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Created by Lucio on 2020-11-12.
 */

fun Context.toast(msg: String) {
    toastHandler.showToast(this, msg)
}

fun Context.toast(msg: String,length:Int) {
    toastHandler.showToast(this, msg,length)
}


fun Fragment.toast(msg: String) {
    toastHandler.showToast(requireContext(), msg)
}

fun View.toast(msg: String) {
    toastHandler.showToast(context, msg)
}