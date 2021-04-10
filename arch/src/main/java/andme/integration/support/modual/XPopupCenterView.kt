package andme.integration.support.modual

import andme.core.ktx.removeSelf
import andme.lang.orDefault
import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import com.lxj.xpopup.core.CenterPopupView

/**
 * Created by Lucio on 2020/11/25.
 */
@SuppressLint("ViewConstructor")
open class XPopupCenterView private constructor(
    ctx: Context,
    val provider: XPopupViewProvider
) : CenterPopupView(ctx) {

    constructor(ctx: Context, layoutId: Int) : this(ctx, XPopupViewProvider(layoutId))

    constructor(ctx: Context, contentView: View) : this(ctx, XPopupViewProvider(contentView))

    override fun addInnerContent() {
        val childView = provider.onCreateView(centerPopupContainer)
        val lp = childView.layoutParams.orDefault {
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        childView.removeSelf()
        centerPopupContainer.addView(childView, LayoutParams(lp).apply {
            gravity = Gravity.CENTER
        })
        onViewCreated(this, childView)
    }

    protected open fun onViewCreated(parent: XPopupCenterView, contentView:View){

    }

    companion object {

        @JvmStatic
        fun create(
            ctx: Context,
            layoutId: Int,
            onViewCreated: CenterPopupView.() -> Unit
        ): XPopupCenterView {
            return object :XPopupCenterView(ctx,layoutId){
                override fun onViewCreated(parent: XPopupCenterView, contentView: View) {
                    super.onViewCreated(parent, contentView)
                    onViewCreated(parent)
                }
            }
        }

        @JvmStatic
        fun create(
            contentView: View,
            onViewCreated: CenterPopupView.() -> Unit
        ): XPopupCenterView {
            return object :XPopupCenterView(contentView.context,contentView){
                override fun onViewCreated(parent: XPopupCenterView, contentView: View) {
                    super.onViewCreated(parent, contentView)
                    onViewCreated(parent)
                }
            }
        }
    }
}