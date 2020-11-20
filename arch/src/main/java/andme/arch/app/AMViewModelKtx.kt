package andme.arch.app

import andme.core.exceptionHandlerAM
import andme.core.support.ui.showAlertDialog
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Created by Lucio on 2020-11-18.
 */

fun AMViewModel.launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
) {
    viewModelScope.launch(context, start, block)
}

inline fun AMViewModel.tryUi(func: AMViewModel.() -> Unit): Throwable? {
    return try {
        func(this)
        null
    } catch (e: Exception) {
        invokeContextAction {
            exceptionHandlerAM.handleUIException(it, e)
        }
        e
    }
}


fun AMViewModel.showAlertDialog(@StringRes messageId: Int, @StringRes positiveTextId: Int) {
    invokeContextAction {
        it.showAlertDialog(messageId, positiveTextId)
    }
}

fun AMViewModel.showAlertDialog(message: CharSequence, positiveBtnText: CharSequence) {
    invokeContextAction {
        it.showAlertDialog(message, positiveBtnText)
    }
}

fun AMViewModel.showAlertDialog(message: CharSequence, okPair: Pair<CharSequence, DialogInterface.OnClickListener?>, cancelable: Boolean = true) {
    invokeContextAction {
        it.showAlertDialog(message, okPair, cancelable)
    }
}

fun AMViewModel.showAlertDialog(@StringRes messageId: Int, okPair: Pair<Int, DialogInterface.OnClickListener?>, cancelable: Boolean = true) {
    invokeContextAction {
        it.showAlertDialog(messageId, okPair, cancelable)
    }
}

fun AMViewModel.showAlertDialog(
        message: CharSequence,
        okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
        cancelPair: Pair<CharSequence, DialogInterface.OnClickListener?>? = null,
        cancelable: Boolean = true
) {
    invokeContextAction {
        it.showAlertDialog(message, okPair, cancelPair, cancelable)
    }
}

fun AMViewModel.showAlertDialog(
        @StringRes messageId: Int,
        okPair: Pair<Int, DialogInterface.OnClickListener?>,
        cancelPair: Pair<Int, DialogInterface.OnClickListener?>? = null,
        cancelable: Boolean = true
) {
    invokeContextAction {
        it.showAlertDialog(messageId, okPair, cancelPair, cancelable)
    }
}