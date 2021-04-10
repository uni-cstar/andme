package andme.integration.support.modualtv

import andme.integration.support.modual.XPopupFullscreenView
import android.content.Context
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

    companion object {

        @JvmStatic
        fun create(
            ctx: Context,
            layoutId: Int,
            onViewCreated: FullScreenPopupView.() -> Unit
        ): XPopupFullscreenTVView {
            return object : XPopupFullscreenTVView(ctx, layoutId) {
                override fun onViewCreated(view: XPopupFullscreenView, contentView: View) {
                    onViewCreated(view)
                }
            }
        }

        @JvmStatic
        fun create(
            contentView: View,
            onViewCreated: FullScreenPopupView.() -> Unit
        ): XPopupFullscreenTVView {
            return object : XPopupFullscreenTVView(contentView.context, contentView) {
                override fun onViewCreated(view: XPopupFullscreenView, contentView: View) {
                    onViewCreated(view)
                }
            }
        }

    }
}
