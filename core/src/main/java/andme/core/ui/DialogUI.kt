package andme.core.ui

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes

/**
 * Created by Lucio on 2020-11-02.
 */

interface DialogUI {

    fun showLoading(ctx: Context, message: CharSequence): AMDialog

    fun showAlertDialog(ctx: Context, @StringRes messageId: Int, @StringRes positiveTextId: Int): AMDialog {
        return showAlertDialog(ctx, ctx.getText(messageId), ctx.getText(positiveTextId))
    }

    fun showAlertDialog(ctx: Context, message: CharSequence, positiveBtnText: CharSequence): AMDialog

    fun showAlertDialog(ctx: Context, message: CharSequence, okPair: Pair<CharSequence, DialogInterface.OnClickListener?>, cancelable: Boolean): AMDialog {
        return showAlertDialog(ctx, message, okPair, null, cancelable)
    }

    fun showAlertDialog(ctx: Context, @StringRes messageId: Int, okPair: Pair<Int, DialogInterface.OnClickListener?>, cancelable: Boolean): AMDialog {
        return showAlertDialog(ctx, messageId, okPair, null, cancelable)
    }

    fun showAlertDialog(
            ctx: Context, message: CharSequence,
            okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
            cancelPair: Pair<CharSequence, DialogInterface.OnClickListener?>? = null,
            cancelable: Boolean = true
    ): AMDialog

    fun showAlertDialog(
            ctx: Context, @StringRes messageId: Int,
            okPair: Pair<Int, DialogInterface.OnClickListener?>,
            cancelPair: Pair<Int, DialogInterface.OnClickListener?>? = null,
            cancelable: Boolean = true
    ): AMDialog

}

interface AMDialog {
    fun dismiss()
}

