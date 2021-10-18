package andme.core.support.ui

import android.content.Context

/**
 * Created by Lucio on 2020-11-12.
 */

interface ToastUI {

    fun showToast(ctx: Context, msg: String)

    fun showToast(ctx: Context, msg: String, length: Int)

}