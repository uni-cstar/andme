package andme.integration.support.modual

import android.content.Context
import android.view.View
import com.lxj.xpopup.XPopup

typealias XPopupBuilder = XPopup.Builder

val Context.xpopupBuilder get() = XPopup.Builder(this)

/**
 * 点击返回键或者点击透明区域是否关闭对话框
 */
inline fun XPopupBuilder.cancelable(cancelable: Boolean): XPopupBuilder {
    return this.dismissOnBackPressed(cancelable)
            .dismissOnTouchOutside(cancelable)
}

/**
 * 初始化参数
 */
inline fun XPopupBuilder.setup(initializer: XPopupBuilder.() -> Unit): XPopupBuilder {
    initializer(this)
    return this
}

/**
 * 背景高斯模糊
 */
inline fun XPopupBuilder.blur(): XPopupBuilder {
    return this.hasBlurBg(true)
}

/**
 * 居中显示
 * @param layoutId 布局Id
 * @param onViewCreated 视图创建成功之后的回调，用于初始化View
 */
fun XPopupBuilder.showAtCenter(ctx: Context, layoutId: Int, onViewCreated: com.lxj.xpopup.core.CenterPopupView.() -> Unit) {
    this.asCustom(CenterPopupViewWrapper.create(ctx, layoutId, onViewCreated))
            .show()
}

/**
 * 居中显示
 * @param contentView 显示的布局视图
 * @param onViewCreated
 */
fun XPopupBuilder.showAtCenter(contentView: View, onViewCreated: com.lxj.xpopup.core.CenterPopupView.() -> Unit) {
    this.asCustom(CenterPopupViewWrapper.create(contentView, onViewCreated))
            .show()
}

fun XPopupBuilder.showAtCenter(ctx: Context, centerView: CenterPopupViewAM) {
    this.asCustom(CenterPopupViewWrapper.create(ctx, centerView))
            .show()
}

