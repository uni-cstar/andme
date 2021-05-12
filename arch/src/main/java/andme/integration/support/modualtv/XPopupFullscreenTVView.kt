package andme.integration.support.modualtv

import andme.integration.support.modual.XPopupFullscreenView
import andme.lang.orDefault
import android.content.Context
import android.view.KeyEvent
import android.view.View
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.impl.FullScreenPopupView

open class XPopupFullscreenTVView :
    XPopupFullscreenView {

    constructor(ctx: Context, layoutId: Int) : super(ctx, layoutId)

    constructor(ctx: Context, contentView: View) : super(ctx, contentView)

    override fun show(): BasePopupView {
        if (this.dialog == null)
            this.dialog = XPopupTVFullScreenDialog(this.context).setContent(this)
        return super.show()
    }

    /**
     * 用于在电视端禁用view默认获取焦点
     */
    override fun focusAndProcessBackPress() {
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event)
    }

    private val isDismissOnBackPressed: Boolean
        get() {
            val info = popupInfo
            return info != null && popupInfo.isDismissOnBackPressed
        }

    /**
     * 用于处理focusAndProcessBackPress禁止获取焦点之后，按返回键时关闭对话框的触发逻辑：如果不使用该方法也可以关闭对话框，但是这样子对话框关闭就没有动画和回调执行了
     */
    private fun executeKeyEvent(event: KeyEvent): Boolean {
        if (!isDismissOnBackPressed)
            return false

        if (event.keyCode == KeyEvent.KEYCODE_BACK || event.keyCode == KeyEvent.KEYCODE_ESCAPE) {
            if (event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) {
//                event.startTracking()
                return true
            } else if (event.action == KeyEvent.ACTION_UP){// && event.isTracking && !event.isCanceled) {
                val customHandled = popupInfo.xPopupCallback?.onBackPressed(this).orDefault(false)
                if (!customHandled) {
                    dismissOrHideSoftInput()
                }
                return true
            }
        }
        return false
    }

    companion object {

        @JvmStatic
        fun create(
            ctx: Context,
            layoutId: Int,
            onViewCreated: FullScreenPopupView.() -> Unit
        ): XPopupFullscreenTVView {
            return object : XPopupFullscreenTVView(ctx, layoutId) {
                override fun onViewCreated(parent: XPopupFullscreenView, contentView: View) {
                    onViewCreated(parent)
                }
            }
        }

        @JvmStatic
        fun create(
            contentView: View,
            onViewCreated: FullScreenPopupView.() -> Unit
        ): XPopupFullscreenTVView {
            return object : XPopupFullscreenTVView(contentView.context, contentView) {
                override fun onViewCreated(parent: XPopupFullscreenView, contentView: View) {
                    onViewCreated(parent)
                }
            }
        }

    }
}
