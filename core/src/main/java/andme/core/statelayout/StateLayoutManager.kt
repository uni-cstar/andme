package andme.core.statelayout

import android.util.SparseArray
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes

/**
 * Created by Lucio on 2021/5/15.
 */
object StateLayoutManager {

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

    private val stateViewInfos = SparseArray<StateViewInfo>()


    @JvmStatic
    val emptyViewInfo: StateViewInfo
        get() = getOrPutStateInfo(STATE_EMPTY)

    @JvmStatic
    val loadingViewInfo: StateViewInfo
        get() = getOrPutStateInfo(STATE_LOADING)

    @JvmStatic
    val errorViewInfo: StateViewInfo
        get() = getOrPutStateInfo(STATE_ERROR)

    /**
     * 无网络连接信息
     */
    @JvmStatic
    val noNetworkViewInfo: StateViewInfo
        get() = getOrPutStateInfo(STATE_NO_NETWORK)

    /**
     * 设置空数据视图
     * @param layoutId:布局Id
     */
    @JvmStatic
    fun setEmptyViewInfo(@LayoutRes layoutId: Int) = addStateInfoInternal(STATE_EMPTY, layoutId)

    /**
     * 设置加载中视图
     * @param layoutId:布局Id
     */
    @JvmStatic
    fun setLoadingViewInfo(@LayoutRes layoutId: Int) = addStateInfoInternal(STATE_LOADING, layoutId)

    /**
     * 设置错误视图
     * @param layoutId:布局Id
     */
    @JvmStatic
    fun setErrorViewInfo(@LayoutRes layoutId: Int) = addStateInfoInternal(STATE_ERROR, layoutId)

    /**
     * 设置网络断开视图
     * @param layoutId:布局Id
     */
    @JvmStatic
    fun setNoNetworkViewInfo(@LayoutRes layoutId: Int) =
        addStateInfoInternal(STATE_NO_NETWORK, layoutId)

    private fun addStateInfoInternal(
        status: Int,
        @LayoutRes layoutId: Int
    ) = addStateInfo(status, layoutId)

    /**
     * 添加自定义布局信息
     */
    @JvmStatic
    fun addStateInfo(
        @IntRange(from = 5L) status: Int,
        @LayoutRes layoutId: Int
    ) = this.apply {
        stateViewInfos.put(status, StateViewInfo(status, layoutId))
    }


    @JvmStatic
    private fun getOrPutStateInfo(state: Int): StateViewInfo =
        stateViewInfos.get(state) ?: StateViewInfo(state).also {
            stateViewInfos.put(state, it)
        }

    @JvmStatic
    fun getStateInfo(state: Int): StateViewInfo? = stateViewInfos.get(state)

    /**
     * 获取状态信息；如果没找到，则创建一个新的
     */
    @JvmStatic
    fun getStateInfoOrCreate(state: Int): StateViewInfo =
        stateViewInfos.get(state) ?: StateViewInfo(state)


}