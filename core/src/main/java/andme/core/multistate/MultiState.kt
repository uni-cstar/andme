package andme.core.multistate

import andme.core.multistate.MultiState.NONE_RESOURCE_ID
import andme.core.multistate.MultiState.STATE_KNOWN
import android.util.SparseArray
import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes

/**
 * Created by Lucio on 2021/3/30.
 * 多状态布局
 */
object MultiState {

    internal const val STATE_KNOWN = -1
    internal const val STATE_CONTENT = 0x000
    internal const val STATE_LOADING = 0x001
    internal const val STATE_EMPTY = 0x002
    internal const val STATE_ERROR = 0x003
    internal const val STATE_NO_NETWORK = 0x004

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(STATE_KNOWN, STATE_CONTENT, STATE_LOADING, STATE_EMPTY, STATE_ERROR, STATE_NO_NETWORK)
    internal annotation class State

    internal const val NONE_RESOURCE_ID = -1

    private val mStates = SparseArray<StateInfo>()

    /**
     * 空布局信息
     */
    @JvmStatic
    val emptyInfo: StateInfo
        get() = getOrPutStateInfo(STATE_EMPTY)

    /**
     * 设置空数据视图
     * @param layoutId:布局Id
     * @param hintTextId:用于显示提示文字的控件Id
     * @param hintText:提示文字
     * @param clickViewIds:需要设置点击事件的控件Id
     */
    @JvmStatic
    @JvmOverloads
    fun setEmptyInfo(
        @LayoutRes layoutId: Int,
        @IdRes hintTextId: Int = NONE_RESOURCE_ID,
        hintText: String? = null,
        @IdRes vararg clickViewIds: Int
    ) = addStateInfoInternal(STATE_EMPTY, layoutId, hintTextId, hintText, *clickViewIds)

    /**
     * 加载布局信息
     */
    @JvmStatic
    val loadingInfo: StateInfo
        get() = getOrPutStateInfo(STATE_LOADING)

    /**
     * 设置加载中视图
     * @param layoutId:布局Id
     * @param hintTextId:用于显示提示文字的控件Id
     * @param hintText:提示文字
     */
    @JvmStatic
    @JvmOverloads
    fun setLoadingInfo(
        @LayoutRes layoutId: Int,
        @IdRes hintTextId: Int = NONE_RESOURCE_ID,
        hintText: String? = null
    ) = addStateInfoInternal(STATE_LOADING, layoutId, hintTextId, hintText)

    /**
     * 错误布局信息
     */
    @JvmStatic
    val errorInfo: StateInfo
        get() = getOrPutStateInfo(STATE_ERROR)

    /**
     * 设置错误视图
     * @param layoutId:布局Id
     * @param hintTextId:用于显示提示文字的控件Id
     * @param hintText:提示文字
     * @param clickViewIds:需要设置点击事件的控件Id
     */
    @JvmStatic
    @JvmOverloads
    fun setErrorInfo(
        @LayoutRes layoutId: Int,
        @IdRes hintTextId: Int = NONE_RESOURCE_ID,
        hintText: String? = null,
        @IdRes vararg clickViewIds: Int
    ) = addStateInfoInternal(STATE_ERROR, layoutId, hintTextId, hintText, *clickViewIds)

    /**
     * 无网络连接信息
     */
    @JvmStatic
    val noNetworkInfo: StateInfo
        get() = getOrPutStateInfo(STATE_NO_NETWORK)

    /**
     * 设置网络断开视图
     * @param layoutId:布局Id
     * @param hintTextId:用于显示提示文字的控件Id
     * @param hintText:提示文字
     * @param clickViewIds:需要设置点击事件的控件Id
     */
    @JvmStatic
    @JvmOverloads
    fun setNoNetworkView(
        @LayoutRes layoutId: Int,
        @IdRes hintTextId: Int = NONE_RESOURCE_ID,
        hintText: String? = null,
        @IdRes vararg clickViewIds: Int
    ) = addStateInfoInternal(STATE_NO_NETWORK, layoutId, hintTextId, hintText, *clickViewIds)


    @JvmStatic
    private fun getOrPutStateInfo(state: Int): StateInfo =
        mStates.get(state) ?: StateInfo(state).also {
            mStates.put(state, it)
        }

    @JvmStatic
    fun getStateInfo(state: Int): StateInfo? = mStates.get(state)

    /**
     * 获取状态信息；如果没找到，则创建一个新的
     */
    @JvmStatic
    fun getStateInfoOrCreate(state: Int): StateInfo = mStates.get(state) ?: StateInfo(state)

    private fun addStateInfoInternal(
        status: Int,
        @LayoutRes layoutId: Int,
        @IdRes hintId: Int = NONE_RESOURCE_ID,
        hintText: String? = null,
        @IdRes vararg clickViewIds: Int
    ) = addStateInfo(status, layoutId, hintId, hintText, *clickViewIds)

    /**
     * 添加自定义布局信息
     * @param state 布局状态，用于关联布局信息（唯一键），>5，前几位值已经被占用
     * @param layoutId 布局Id
     * @param clickViewIds 需要设置点击事件的控件Id
     */
    @JvmStatic
    @JvmOverloads
    fun addStateInfo(
        @IntRange(from = 5L) status: Int,
        @LayoutRes layoutId: Int,
        @IdRes hintId: Int = NONE_RESOURCE_ID,
        hintText: String? = null,
        @IdRes vararg clickViewIds: Int
    ) = this.apply {
        mStates.put(
            status, StateInfo(
                status,
                layoutId,
                hintId,
                hintText,
                clickViewIds.toList()
            )
        )
    }

}

/**
 * 视图状态信息
 */
data class StateInfo(
    @MultiState.State
    val state: Int = STATE_KNOWN,   //视图状态
    @LayoutRes var layoutId: Int = NONE_RESOURCE_ID,   //视图布局Id
    @IdRes var hintId: Int = NONE_RESOURCE_ID,
    var hintText: String? = null,
    val clickViewIds: List<Int> = listOf()  //设置点击事件的控件Id
) {

    fun copy(): StateInfo {
        return StateInfo(
            this.state,
            this.layoutId,
            this.hintId,
            this.hintText,
            this.clickViewIds.toList()
        )
    }


}