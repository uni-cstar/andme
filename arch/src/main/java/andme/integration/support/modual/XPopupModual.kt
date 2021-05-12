package andme.integration.support.modual

import android.content.Context
import android.view.View
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.impl.FullScreenPopupView

typealias XPopupBuilder = XPopup.Builder


/**
 * 点击返回键或者点击透明区域是否关闭对话框
 */
inline fun XPopupBuilder.cancelable(cancelable: Boolean): XPopupBuilder {
    return this.dismissOnBackPressed(cancelable)
        .dismissOnTouchOutside(cancelable)
}

/**
 * 背景高斯模糊
 */
inline fun XPopupBuilder.blur(): XPopupBuilder {
    return this.hasBlurBg(true)
}


fun Context.newXPopupBuilder(initializer: XPopupBuilder.() -> Unit): XPopupBuilder {
    return XPopupBuilder(this).also {
        initializer(it)
    }
}

/**
 * 居中显示
 * @param layoutId 布局Id
 * @param onViewCreated 视图创建成功之后的回调，用于初始化View
 */
fun XPopupBuilder.showAtCenter(
    ctx: Context,
    layoutId: Int,
    onViewCreated: CenterPopupView.() -> Unit
) {
    this.asCustom(XPopupCenterView.create(ctx, layoutId, onViewCreated))
        .show()
}

/**
 * 居中显示
 * @param contentView 显示的布局视图
 * @param onViewCreated
 */
fun XPopupBuilder.showAtCenter(
    contentView: View,
    onViewCreated: CenterPopupView.() -> Unit
) {
    this.asCustom(XPopupCenterView.create(contentView, onViewCreated))
        .show()
}


fun XPopupBuilder.showAtCenter(ctx: Context, centerView: CenterPopupView) {
    this.asCustom(centerView)
        .show()
}




/**
 * 底部显示
 * @param layoutId 布局Id
 * @param onViewCreated 视图创建成功之后的回调，用于初始化View
 */
fun XPopupBuilder.showAtBottom(
    ctx: Context,
    layoutId: Int,
    onViewCreated: (BottomPopupView,View) -> Unit
): BasePopupView {
    return this.asCustom(XPopupBottomView.create(ctx, layoutId, onViewCreated))
        .show()
}

/**
 * 底部显示
 * @param contentView 显示的布局视图
 * @param onViewCreated
 */
fun XPopupBuilder.showAtBottom(
    contentView: View,
    onViewCreated: BottomPopupView.() -> Unit
) {
    this.asCustom(XPopupBottomView.create(contentView, onViewCreated))
        .show()
}


fun XPopupBuilder.showAtBottom(ctx: Context, centerView: BottomPopupView) {
    this.asCustom(centerView)
        .show()
}



/**
 * 全屏显示
 * @param layoutId 布局Id
 * @param onViewCreated 视图创建成功之后的回调，用于初始化View
 */
fun XPopupBuilder.showFullScreen(
    ctx: Context,
    layoutId: Int,
    onViewCreated: FullScreenPopupView.() -> Unit
) {
    this.asCustom(XPopupFullscreenView.create(ctx, layoutId, onViewCreated))
        .show()
}

/**
 * 全屏显示
 * @param contentView 显示的布局视图
 */
fun XPopupBuilder.showFullScreen(
    contentView: View,
    onViewCreated: FullScreenPopupView.() -> Unit
) {
    this.asCustom(XPopupFullscreenView.create(contentView, onViewCreated))
        .show()
}

/**
 * 全屏显示
 */
fun XPopupBuilder.showFullScreen(
    centerView: FullScreenPopupView
) {
    this.asCustom(centerView)
        .show()
}
