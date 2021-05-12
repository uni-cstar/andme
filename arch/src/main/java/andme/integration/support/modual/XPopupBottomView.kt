package andme.integration.support.modual

import andme.core.ktx.removeSelf
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.lxj.xpopup.core.BottomPopupView

/**
 * Created by Lucio on 2020/11/25.
 */
@SuppressLint("ViewConstructor")
open class XPopupBottomView private constructor(
    ctx: Context,
    val provider: XPopupViewProvider
) : BottomPopupView(ctx) {

    constructor(ctx: Context, layoutId: Int) : this(ctx, XPopupViewProvider(layoutId))

    constructor(ctx: Context, contentView: View) : this(ctx, XPopupViewProvider(contentView))

    override fun addInnerContent() {
        val childView = provider.onCreateView(bottomPopupContainer)
        childView.removeSelf()
        bottomPopupContainer.addView(childView)
        onViewCreated(this, childView)
    }

    protected open fun onViewCreated(parent: XPopupBottomView, contentView: View) {

    }

    companion object {

        @JvmStatic
        fun create(
            ctx: Context,
            layoutId: Int,
            onViewCreated: (BottomPopupView, View) -> Unit
        ): XPopupBottomView {
            return object : XPopupBottomView(ctx, layoutId) {
                override fun onViewCreated(parent: XPopupBottomView, contentView: View) {
                    super.onViewCreated(parent, contentView)
                    onViewCreated(parent, contentView)
                }
            }
        }

        @JvmStatic
        fun create(
            contentView: View,
            onViewCreated: BottomPopupView.() -> Unit
        ): XPopupBottomView {
            return object : XPopupBottomView(contentView.context, contentView) {
                override fun onViewCreated(parent: XPopupBottomView, contentView: View) {
                    super.onViewCreated(parent, contentView)
                    onViewCreated(parent)
                }
            }
        }
    }
}