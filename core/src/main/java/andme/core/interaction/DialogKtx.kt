package andme.core.interaction

import andme.core.dialogHandler
import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Created by Lucio on 2020-11-02.
 */

fun Context.showAlertDialog(message: CharSequence): Any {
    return dialogHandler.showAlertDialog(this, message)
}

fun Context.showAlertDialog(@StringRes messageId: Int): Any {
    return dialogHandler.showAlertDialog(this, messageId)
}

fun Fragment.showAlertDialog(message: CharSequence): Any {
    return requireContext().showAlertDialog(message)
}

fun Fragment.showAlertDialog(@StringRes messageId: Int): Any {
    return requireContext().showAlertDialog(messageId)
}