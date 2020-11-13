package andme.core.support.ui

import android.content.Context
import android.widget.Toast

/**
 * 默认对话框实现
 */
object DefaultToastHandler : ToastUI {

    override fun showToast(ctx: Context, msg: String) {
        showToast(ctx,msg,Toast.LENGTH_SHORT)
    }

    override fun showToast(ctx: Context, msg: String, length: Int) {
        Toast.makeText(ctx,msg,length).show()
    }


}