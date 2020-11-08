package andme.core.interaction

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

/**
 * 默认对话框实现
 */
object DefaultDialogHandler : DialogInteraction {

    override fun showAlertDialog(
        ctx: Context,
        message: CharSequence,
        okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
        cancelPair: Pair<CharSequence, DialogInterface.OnClickListener?>?,
        cancelable: Boolean
    ) {

    }

    private fun wrapper(dialog: AlertDialog): AMDialog {
        return object : AMDialog {
            override fun dismiss() {
                dialog.dismiss()
            }
        }
    }
    override fun showAlertDialog(ctx: Context, message: CharSequence): AMDialog {
        val dialog = AlertDialog.Builder(ctx)
            .setMessage(message)
            .show()
        return wrapper(dialog)
    }

}