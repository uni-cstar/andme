package andme.integration.support.modual

import andme.core.ktx.removeSelf
import andme.lang.orDefault
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.lxj.xpopup.impl.FullScreenPopupView

/**
 * Created by Lucio on 2020/11/25.
 */

@SuppressLint("ViewConstructor")
open class XPopupFullscreenView private constructor(
    ctx: Context,
    val provider: XPopupViewProvider
) : FullScreenPopupView(ctx) {

    constructor(ctx: Context, layoutId: Int) : this(ctx, XPopupViewProvider(layoutId))

    constructor(ctx: Context, contentView: View) : this(ctx, XPopupViewProvider(contentView))

    override fun addInnerContent() {
        val childView = provider.onCreateView(fullPopupContainer)
        val lp = childView.layoutParams.orDefault {
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        childView.removeSelf()
        fullPopupContainer.addView(childView, LayoutParams(lp))
        onViewCreated(this, childView)
    }

    protected open fun onViewCreated(parent: XPopupFullscreenView, contentView: View) {

    }

    /**
     * 用于在电视端禁用view默认获取焦点
     */
//    override fun focusAndProcessBackPress() {
//        super.focusAndProcessBackPress()
//        this.isFocusable = false
//        this.isFocusableInTouchMode = false
//        this.isClickable = false
//        clearFocus()
//    }

    companion object {

        @JvmStatic
        fun create(
            ctx: Context,
            layoutId: Int,
            onViewCreated: XPopupFullscreenView.() -> Unit
        ): XPopupFullscreenView {
            return object : XPopupFullscreenView(ctx, layoutId) {
                override fun onViewCreated(parent: XPopupFullscreenView, contentView: View) {
                    super.onViewCreated(parent, contentView)
                    onViewCreated(parent)
                }
            }
        }

        @JvmStatic
        fun create(
            contentView: View,
            onViewCreated: XPopupFullscreenView.() -> Unit
        ): XPopupFullscreenView {
            return object : XPopupFullscreenView(contentView.context, contentView) {
                override fun onViewCreated(parent: XPopupFullscreenView, contentView: View) {
                    super.onViewCreated(parent, contentView)
                    onViewCreated(parent)
                }
            }
        }
    }
}