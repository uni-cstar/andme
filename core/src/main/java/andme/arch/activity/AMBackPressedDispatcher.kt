package andme.arch.activity

import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.*

/**
 * Created by Lucio on 2020-11-02.
 * 返回键事件分发者
 */
class AMBackPressedDispatcher constructor(val owner: AMBackPressedOwner) {

    internal val mOnBackPressedCallbacks = ArrayDeque<AMBackPressedCallback>()

    /**
     * 添加回调
     */
    @MainThread
    fun addCallback(onBackPressedCallback: AMBackPressedCallback) {
        addCancellableCallback(onBackPressedCallback)
    }

    /**
     * 内部方法，添加可取消的回调。经过此方法，调用者可以通过调用[AMBackPressedCallback.remove]方法，移除订阅。
     *
     * @param onBackPressedCallback 添加的回调
     * @return 返回一个可以调用[Cancellable.cancel]取消订阅的对象
     */
    @MainThread
    internal fun addCancellableCallback(onBackPressedCallback: AMBackPressedCallback): Cancellable {
        mOnBackPressedCallbacks.add(onBackPressedCallback)
        val cancellable = OnBackPressedCancellable(onBackPressedCallback)
        onBackPressedCallback.addCancellable(cancellable)
        return cancellable
    }

    /**
     * 添加与生命周期相关的回调
     * 调用此方法添加的回调，至少在生命周期处于[Lifecycle.State.STARTED]时才会执行回调,并且在[Lifecycle.State.DESTROYED]时自动移除回调
     * @param owner 回调执行相关的生命周期
     * @param onBackPressedCallback 添加的回调
     */
    @MainThread
    fun addCallback(
        owner: LifecycleOwner,
        onBackPressedCallback: AMBackPressedCallback
    ) {
        val lifecycle = owner.lifecycle
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            return
        }

        onBackPressedCallback.addCancellable(
            LifecycleOnBackPressedCancellable(lifecycle, onBackPressedCallback)
        )
    }


    /**
     * 返回键事件执行
     * @return true:消耗了事件 false：未消耗事件
     */
    @MainThread
    fun onBackPressed(): Boolean {
        if(!hasRegisteredCallbacks())
            return false

        val iterator = mOnBackPressedCallbacks.descendingIterator()
        while (iterator.hasNext()) {
            val callback = iterator.next()
            if (callback.onBackPressed(owner)) {
                return true
            }
        }
        return false
    }

    /**
     * 是否有已注册的回调
     */
    fun hasRegisteredCallbacks():Boolean{
        return mOnBackPressedCallbacks.isNotEmpty()
    }

    /**
     * 这是一个循环链设计：callback 被添加到到dispathcer中，callback添加了一个OnBackPressedCancellable对象，这个对象是dispatcher的内部对象，
     * 即callback->cancellable->dispatcher->callback
     * 当调用callback的remove方法是，通过callback中添加的cancellabe对象从dispatcher中将自己移除
     */
    private inner class OnBackPressedCancellable internal constructor(private val mOnBackPressedCallback: AMBackPressedCallback) :
        Cancellable {

        override fun cancel() {
            mOnBackPressedCallbacks.remove(mOnBackPressedCallback)
            mOnBackPressedCallback.removeCancellable(this)
        }
    }

    /**
     * 与生命周期相关的循环取消链设计
     */
    private inner class LifecycleOnBackPressedCancellable internal constructor(
        private val mLifecycle: Lifecycle,
        private val mOnBackPressedCallback: AMBackPressedCallback
    ) : LifecycleEventObserver, Cancellable {

        private var mCurrentCancellable: Cancellable? = null

        init {
            mLifecycle.addObserver(this)
        }

        override fun onStateChanged(
            source: LifecycleOwner,
            event: Lifecycle.Event
        ) {
            if (event == Lifecycle.Event.ON_START) {
                mCurrentCancellable = addCancellableCallback(mOnBackPressedCallback)
            } else if (event == Lifecycle.Event.ON_STOP) {
                mCurrentCancellable?.cancel()
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                cancel()
            }
        }

        override fun cancel() {
            mLifecycle.removeObserver(this)
            mOnBackPressedCallback.removeCancellable(this)
            mCurrentCancellable?.cancel()
            mCurrentCancellable = null
        }
    }
}