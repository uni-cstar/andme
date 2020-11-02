package andme.core.lifecycle

import android.os.Handler
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 用于在ViewModel中执行的事件：比如Snakbar，dialog，toast等
 * 注意：除非主动调用[MutableLiveData.setValue]或[MutableLiveData.postValue]，否则不会触发[Observer.onChanged]；
 * 与[MutableLiveData]不同之处在于[MutableLiveData]在绑定[Observer]的时候,如果当前有值，则会触发执行[Observer.onChanged]，而
 */
open class MutableLiveEvent<T> : MutableLiveData<T> {

    private val mPending = AtomicBoolean(false)

    private val mHandler = Handler()

    constructor(value: T) : super(value)
    constructor() : super()


    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            mHandler.removeCallbacks(resetRunnable)
            if (mPending.get()) {
                observer.onChanged(t)
            }
            mHandler.postDelayed(resetRunnable, 300)
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        //先移除重置方法
        mHandler.removeCallbacks(resetRunnable)
        mPending.set(true)
        //延迟重置方法，避免当前无可用Observer时进行状态重置；
        mHandler.postDelayed(resetRunnable, 300)
        super.setValue(t)
    }

    private val resetRunnable: Runnable = Runnable { mPending.set(false) }
    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}