package andme.core.statelayout

import andme.core.content.layoutInflater
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * Created by Lucio on 2021/5/15.
 *
 *
 */
data class StateViewInfo(
    @StateLayoutManager.State
    val state: Int = StateLayoutManager.STATE_KNOWN,   //视图状态
    @LayoutRes var layoutId: Int = StateLayoutManager.NONE_RESOURCE_ID,
    val factory: StateViewFactory = DefaultStateViewFactory
)

/**
 * StateView创建工厂
 */
fun interface StateViewFactory {
    fun createStateView(
        ctx: Context,
        container: ViewGroup?,
        stateViewInfo: StateViewInfo
    ): StateView
}

interface StateView {

    val view: View
}

class DefaultStateView(override val view: View) : StateView

object DefaultStateViewFactory : StateViewFactory {

    override fun createStateView(
        ctx: Context,
        container: ViewGroup?,
        stateViewInfo: StateViewInfo
    ): StateView {
        val view = ctx.layoutInflater.inflate(stateViewInfo.layoutId, container, false)
        return DefaultStateView(view)
    }
}
//
//data class StateViewInfo(
//    @MultiState.State
//    val state: Int = MultiState.STATE_KNOWN,   //视图状态
//    @LayoutRes var layoutId: Int = MultiState.NONE_RESOURCE_ID,   //视图布局Id
//    @IdRes var hintId: Int = MultiState.NONE_RESOURCE_ID,
//    var hintText: String? = null,
//    val clickViewIds: List<Int> = listOf()  //设置点击事件的控件Id
//) {
//
//    fun copy(): andme.core.multistate.StateViewInfo {
//        return StateViewInfo(
//            this.state,
//            this.layoutId,
//            this.hintId,
//            this.hintText,
//            this.clickViewIds.toList()
//        )
//    }
//
//
//}