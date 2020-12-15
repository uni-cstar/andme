package andme.arch.app

import andme.core.dialogHandlerAM
import andme.core.lifecycle.SingleEvent
import andme.core.lifecycle.SingleLiveEvent
import andme.core.support.ui.AMDialog
import andme.lang.Note
import andme.lang.coroutines.ControlledRunner
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.lifecycle.*

/**
 * Created by Lucio on 2020-11-01.
 * ViewModel基础类：支持生命周期和常规与Activity、Fragment联动的方法
 */
open class AMViewModel(application: Application) : AndroidViewModel(application),
        LifecycleObserver {

    protected val logTag: String get() = this::class.java.name

    /**
     * ViewModel是否已绑定到所有者：即已经注册了事件
     */
    @Volatile
    internal var hasBindOwner:Boolean = false

    /**
     * 与Context操作相关的行为回调接口
     */
    interface ContextAction {
        fun onContextAction(ctx: Context)
    }

    protected var loadingDialog: AMDialog? = null
        private set

    val finishEvent: SingleEvent = SingleEvent()

    val backPressedEvent: SingleEvent = SingleEvent()

    val startActivityEvent: SingleLiveEvent<Intent> = SingleLiveEvent<Intent>()

    val startActivityForResultEvent: SingleLiveEvent<Pair<Intent, Int>> =
            SingleLiveEvent<Pair<Intent, Int>>()

    val toastEvent: SingleLiveEvent<Pair<String, Int>> =
            SingleLiveEvent<Pair<String, Int>>()

    val contextActionEvent: MutableLiveData<ContextAction> =
            MutableLiveData<ContextAction>()

    internal fun unregister(owner:AMViewModelOwner) {
        //移除生命周期监听
        owner.getLifecycle().removeObserver(this)
        //移除常规事件监听
        val lifecycleOwner = owner.getLifecycleOwner()
        finishEvent.removeObservers(lifecycleOwner)
        backPressedEvent.removeObservers(lifecycleOwner)
        startActivityEvent.removeObservers(lifecycleOwner)
        startActivityForResultEvent.removeObservers(lifecycleOwner)
        toastEvent.removeObservers(lifecycleOwner)
        contextActionEvent.removeObservers(lifecycleOwner)
        hasBindOwner = false
    }

    protected open fun log(msg: String) {
        Log.d(logTag, msg)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    open fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {
        log("onAny")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        log("onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        log("onDestroy")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
        log("onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        log("onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
        log("onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
        log("onPause")
    }

    @MainThread
    open fun finish() {
        finishEvent.call()
    }

    @MainThread
    open fun onBackPressed() {
        backPressedEvent.call()
    }

    @MainThread
    open fun startActivity(intent: Intent) {
        startActivityEvent.value = intent
    }

    @MainThread
    open fun startActivityForResult(intent: Intent, requestCode: Int) {
        startActivityForResultEvent.value = Pair(intent, requestCode)
    }

    @MainThread
    open fun toast(message: String, length: Int) {
        toastEvent.value = Pair(message, length)
    }

    @MainThread
    open fun toast(message: String) {
        toast(message, Toast.LENGTH_SHORT)
    }

    @MainThread
    fun showLoading(message: String) {
        hideLoading()
        invokeContextAction {
            loadingDialog = dialogHandlerAM.showLoading(it, message)
        }
    }

    @MainThread
    fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    /**
     * 执行与context相关的操作
     */
    @MainThread
    open fun invokeContextAction(ctxAction: ContextAction) {
        contextActionEvent.value = ctxAction
    }

    /**
     * 执行与context相关的操作
     */
    @MainThread
    open fun invokeContextAction(action: (Context) -> Unit) {
        invokeContextAction(object : ContextAction {
            override fun onContextAction(ctx: Context) {
                action.invoke(ctx)
            }
        })
    }

    @Deprecated("@See Note")
    @Note("此方法只会在主ViewModel中回调，其他ViewModel需要用户从Activity或者Fragment的onActivityResult方法中回调过来")
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //nothing
    }

    private val cancelPreviousRunner: ControlledRunner<Any?> by lazy {
        ControlledRunner<Any?>()
    }

    suspend fun <T> launchAndCancelPrevious(func: suspend () -> T): T {
        return cancelPreviousRunner.cancelPreviousThenRun(func) as T
    }
}
