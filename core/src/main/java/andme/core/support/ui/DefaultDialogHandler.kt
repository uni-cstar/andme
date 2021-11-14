package andme.core.support.ui

import andme.lang.mapAs
import andme.lang.runOnTrue
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

/**
 * 默认对话框实现
 */
object DefaultDialogHandler : DialogUI {

    override fun showLoading(ctx: Context, message: CharSequence): AMProgressDialog {
        val dialog = ProgressDialog(ctx)
        dialog.setMessage(message)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        return wrapperProgressDialog(dialog)
    }

    override fun showAlertDialog(
        ctx: Context,
        message: CharSequence,
        positiveBtnText: CharSequence
    ): AMDialog {
        val dialog = AlertDialog.Builder(ctx)
            .setMessage(message)
            .setPositiveButton(positiveBtnText, null)
            .show()
        return wrapper(dialog)
    }

    override fun showAlertDialog(
        ctx: Context,
        messageId: Int,
        okPair: Pair<Int, DialogInterface.OnClickListener?>,
        cancelPair: Pair<Int, DialogInterface.OnClickListener?>?,
        cancelable: Boolean
    ): AMDialog {
        return showAlertDialog(ctx, ctx.getText(messageId), okPair.mapAs {
            Pair(ctx.getText(this.first), this.second)
        }, cancelPair?.mapAs {
            Pair(ctx.getText(this.first), this.second)
        }, cancelable)
    }

    override fun showAlertDialog(
        ctx: Context,
        message: CharSequence,
        okPair: Pair<CharSequence, DialogInterface.OnClickListener?>,
        cancelPair: Pair<CharSequence, DialogInterface.OnClickListener?>?,
        cancelable: Boolean
    ): AMDialog {
        val dialog = AlertDialog.Builder(ctx)
            .setMessage(message)
            .setPositiveButton(okPair.first, okPair.second)
            .runOnTrue(cancelPair != null) {
                setNegativeButton(cancelPair!!.first, cancelPair.second)
            }
            .setCancelable(cancelable)
            .show()

        return wrapper(dialog)
    }

    //包装对话框
    private fun wrapper(dialog: Dialog): AMDialog {
        return object : AMDialog {
            override fun dismiss() {
                dialog.dismiss()
            }
        }
    }

    private fun wrapperProgressDialog(dialog: ProgressDialog): AMProgressDialog {
        return object : AMProgressDialog {
            override fun setMessage(msg: String) {
                dialog.setMessage(msg)
            }

            override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
                dialog.setOnDismissListener(listener)
            }

            override fun dismiss() {
                dialog.dismiss()
            }
        }
    }
}