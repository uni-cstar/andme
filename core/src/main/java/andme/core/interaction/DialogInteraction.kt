package andme.core.interaction

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes

/**
 * Created by Lucio on 2020-11-02.
 */

interface DialogInteraction {

    fun showAlertDialog(ctx: Context, message: CharSequence): AMDialog

    fun showAlertDialog(ctx: Context, @StringRes messageId: Int): AMDialog {
        return showAlertDialog(ctx, ctx.getText(messageId))
    }

    fun showAlertDialog(
        ctx: Context, message: CharSequence,
        okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
        cancelPair: Pair<CharSequence, DialogInterface.OnClickListener?>? = null,
        cancelable: Boolean = true
    )

}

interface AMDialog {
    fun dismiss()
}

