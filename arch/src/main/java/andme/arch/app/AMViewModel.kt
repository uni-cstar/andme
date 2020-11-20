package andme.arch.app

import andme.core.dialogHandlerAM
import andme.core.exceptionHandlerAM
import andme.core.lifecycle.SingleEvent
import andme.core.lifecycle.SingleLiveEvent
import andme.core.support.ui.AMDialog
import andme.core.support.ui.showAlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.*

/**
 * Created by Lucio on 2020-11-01.
 * ViewModel基础类：支持生命周期和常规与Activity、Fragment联动的方法
 */
open class AMViewModel(application: Application) : AndroidViewModel(application),
        LifecycleObserver {

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

    protected open fun log(msg: String) {
        Log.d("AMViewModel", msg)
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

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //nothing
    }
}
