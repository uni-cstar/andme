package andme.arch.activity

import androidx.annotation.MainThread
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by Lucio on 2020-11-02.
 * 返回键事件回调
 */

abstract class AMBackPressedCallback {

    private val mCancellables = CopyOnWriteArrayList<Cancellable>()

    /**
     * 实现此方法处理返回事件
     * @return true:消耗了返回事件 ，false：不消耗返回事件
     */
    @MainThread
    abstract fun handleOnBackPressed(owner: AMBackPressedOwner):Boolean

    /**
     * 从[AMBackPressedDispatcher]中移除自己
     */
    @MainThread
    fun remove(){
        mCancellables.forEach {
            it.cancel()
        }
    }

    internal fun addCancellable(cancellable: Cancellable) {
        mCancellables.add(cancellable)
    }

    internal fun removeCancellable(cancellable: Cancellable) {
        mCancellables.remove(cancellable)
    }

}