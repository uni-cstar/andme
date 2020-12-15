package andme.integration.support.modual

import andme.core.content.layoutInflater
import andme.lang.orDefault
import andme.core.ktx.removeSelf
import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.lxj.xpopup.core.CenterPopupView

/**
 * Created by Lucio on 2020/11/25.
 */

@SuppressLint("ViewConstructor")
class CenterPopupViewWrapper private constructor(ctx: Context, val view: CenterPopupViewAM) : CenterPopupView(ctx) {

    override fun onCreate() {
        view.onViewCreated(this)
    }

    override fun addInnerContent() {
        val childView = view.prepareView(centerPopupContainer)
        val lp = childView.layoutParams.orDefault {
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        }
        childView.removeSelf()
        centerPopupContainer.addView(childView, LayoutParams(lp).apply {
            gravity = Gravity.CENTER
        })
    }

    companion object {

        @JvmStatic
        fun create(ctx: Context, layoutId: Int, onViewCreated: CenterPopupView.() -> Unit): CenterPopupViewWrapper {
            return CenterPopupViewWrapper(ctx, object : CenterPopupViewAM(layoutId) {
                override fun onViewCreated(parent: CenterPopupView) {
                    onViewCreated(parent)
                }
            })
        }

        @JvmStatic
        fun create(contentView: View, onViewCreated: CenterPopupView.() -> Unit): CenterPopupViewWrapper {
            return CenterPopupViewWrapper(contentView.context, object : CenterPopupViewAM(contentView) {
                override fun onViewCreated(parent: CenterPopupView) {
                    onViewCreated(parent)
                }
            })
        }

        @JvmStatic
        fun create(ctx: Context, centerView: CenterPopupViewAM): CenterPopupViewWrapper {
            return CenterPopupViewWrapper(ctx, centerView)
        }
    }
}

abstract class BaseCenterPopupViewAM {

    abstract fun prepareView(parent: FrameLayout): View


    abstract fun onViewCreated(parent: CenterPopupView)
}


abstract class CenterPopupViewAM : BaseCenterPopupViewAM {

    private var layoutId: Int = 0

    private lateinit var contentView: View

    constructor(layoutId: Int) {
        this.layoutId = layoutId
    }

    constructor(contentView: View) {
        this.contentView = contentView
    }

    override fun prepareView(parent: FrameLayout): View {
        return if (layoutId > 0) {
            parent.context.layoutInflater.inflate(layoutId, parent, false)
        } else {
            return contentView
        }
    }

}