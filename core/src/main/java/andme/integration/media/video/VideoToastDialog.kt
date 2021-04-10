package andme.integration.media.video

import andme.core.R
import andme.core.exception.tryCatch
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes

/**
 * Created by Lucio on 2021/4/8.
 */
abstract class VideoToastDialog(ctx: Context) : Dialog(ctx, R.style.VideoPopupDialog) {

    private var mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentLayoutId())
        window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
            addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
            addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    protected val autoDismissRunnable = Runnable {
        tryCatch {
            onAutoDismissInvoke()
        }
    }

    /**
     * 执行自动小时
     */
    protected open fun onAutoDismissInvoke() {
        dismiss()
    }

    override fun show() {
        mHandler.removeCallbacks(autoDismissRunnable)
        super.show()
    }

    fun showWithDelayDismiss(autoDismissTime: Long = 2000) {
        show()
        if (autoDismissTime > 0) {
            mHandler.postDelayed(autoDismissRunnable, autoDismissTime)
        }
    }

    @LayoutRes
    abstract fun getContentLayoutId(): Int

}