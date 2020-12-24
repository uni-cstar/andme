@file:JvmName("ToastAM")
package andme.core.support.ui

import andme.core.toastHandlerAM
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Created by Lucio on 2020-11-12.
 */

fun Context.toast(msg: String) {
    toastHandlerAM.showToast(this, msg)
}

fun Context.toast(msg: String, length: Int) {
    toastHandlerAM.showToast(this, msg, length)
}

fun Fragment.toast(msg: String) {
    activity?.let {
        toastHandlerAM.showToast(it, msg)
    }
}

fun Fragment.toast(msg: String, length: Int) {
    activity?.let {
        toastHandlerAM.showToast(it, msg, length)
    }
}

fun View.toast(msg: String) {
    toastHandlerAM.showToast(context, msg)
}

fun View.toast(msg: String, length: Int) {
    toastHandlerAM.showToast(context, msg, length)
}