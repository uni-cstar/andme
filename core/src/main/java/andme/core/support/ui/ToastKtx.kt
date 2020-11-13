package andme.core.support.ui

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

fun Context.toast(msg: String, length: Int) {
    toastHandler.showToast(this, msg, length)
}


fun Fragment.toast(msg: String) {
    activity?.let {
        toastHandler.showToast(it, msg)
    }
}

fun Fragment.toast(msg: String, length: Int) {
    activity?.let {
        toastHandler.showToast(it, msg, length)
    }
}

fun View.toast(msg: String) {
    toastHandler.showToast(context, msg)
}

fun View.toast(msg: String, length: Int) {
    toastHandler.showToast(context, msg, length)
}