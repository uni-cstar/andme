package andme.integration.support.modualtv

import andme.integration.support.modual.XPopupBuilder
import andme.integration.support.modual.XPopupViewProvider
import android.content.Context
import android.view.View
import com.lxj.xpopup.impl.FullScreenPopupView


/**
 * 全屏显示
 * @param layoutId 布局Id
 * @param onViewCreated 视图创建成功之后的回调，用于初始化View
 */
fun XPopupBuilder.showFullScreenTV(
    ctx: Context,
    layoutId: Int,
    onViewCreated: FullScreenPopupView.() -> Unit
) {
    this.asCustom(XPopupFullscreenTVView.create(ctx, layoutId, onViewCreated))
        .show()
}

/**
 * 全屏显示
 * @param contentView 显示的布局视图
 */
fun XPopupBuilder.showFullScreenTV(
    contentView: View,
    onViewCreated: FullScreenPopupView.() -> Unit
) {
    this.asCustom(XPopupFullscreenTVView.create(contentView,onViewCreated))
        .show()
}
