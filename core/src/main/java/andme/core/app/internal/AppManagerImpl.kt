package andme.core.app.internal

import andme.core.app.AMActivityStack
import andme.core.app.AMAppManager
import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.util.Log

/**
 * Created by Lucio on 18/3/16.
 * 确定App是前台还是后台状态
 */
@TargetApi(14)
internal object AppManagerImpl : AMAppManager {

    override var debuggable: Boolean
        set(value) {
            andme.core.isDebuggable = value
        }
        get() = andme.core.isDebuggable

    private const val TAG: String = "AppManagerImpl"

    //进入暂停状态时，延迟时间检测app的状态
    private const val PAUSE_STATE_CHECK_DELAY_TIME: Long = 300L

    /**
     * 是否正在暂停（用于确定A 启动 B ，A进入pause ，B进入create等中间这段时间能够更正确的表述app状态）
     */
    private var _isPausing = false

    private val _handler: Handler by lazy {
        Handler()
    }

    private var _checkRunnable: Runnable? = null

    private var _stateListeners: MutableList<AMAppManager.OnAppStateChangedListener>? = null

    /**
     * 当前app是否在前台运行
     *
     * @return
     */
    override var isForeground = false
        private set

    override val activityStack: AMActivityStack = ActivityStackImpl()

    /**
     * 初始化
     */
    fun init(app: Application) {
        //避免重复绑定
        app.unregisterActivityLifecycleCallbacks(_activityLifecycleCallbacks)
        //绑定回调
        app.registerActivityLifecycleCallbacks(_activityLifecycleCallbacks)
    }

    /**
     * 重置
     */
    fun reset(app: Application) {
        app.unregisterActivityLifecycleCallbacks(_activityLifecycleCallbacks)
        isForeground = false
        _isPausing = false
        activityStack.clear()
        _stateListeners?.clear()
        _checkRunnable = null
        _handler.removeCallbacksAndMessages(null)
    }

    /**
     * 绑定App运行状态改变监听
     */
    override fun registerAppStateChangedListener(listener: AMAppManager.OnAppStateChangedListener) {
        if (_stateListeners == null) {
            _stateListeners = mutableListOf()
        }
        _stateListeners!!.add(listener)
    }

    /**
     * 解绑App运行状态改变监听
     */
    override fun unregisterAppStateChangedListener(listener: AMAppManager.OnAppStateChangedListener) {
        _stateListeners?.remove(listener)
    }

    private val _activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks =
            object : Application.ActivityLifecycleCallbacks {

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    activityStack.add(activity)
                }

                override fun onActivityStarted(activity: Activity) {}

                override fun onActivityResumed(activity: Activity) {
                    _isPausing = false
                    val isBackground = !isForeground
                    isForeground = true

                    _checkRunnable?.let {
                        _handler.removeCallbacks(it)
                    }

                    if (isBackground) {
                        Log.d(TAG, "background became foreground")
                        _stateListeners?.forEach {
                            try {
                                it.onAppBecameForeground()
                            } catch (e: Exception) {
                                Log.d(TAG, "listener throw exception", e)
                            }
                        }
                    } else {
                        Log.d(TAG, "still foreground")
                    }
                }

                override fun onActivityPaused(activity: Activity) {
                    _isPausing = true
                    if (_checkRunnable != null) {
                        _handler.removeCallbacks(_checkRunnable)
                    }

                    _checkRunnable = Runnable {
                        if (isForeground && _isPausing) {
                            isForeground = false
                            Log.d(TAG, "became background")
                            if (_stateListeners == null)
                                return@Runnable
                            for (listener in _stateListeners!!) {
                                try {
                                    listener.onAppBecameBackground()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Log.e(TAG, "listener throw exception!", e)
                                }
                            }
                        } else {
                            Log.d(TAG, "still foreground")
                        }
                    }
                    _handler.postDelayed(
                            _checkRunnable,
                            PAUSE_STATE_CHECK_DELAY_TIME
                    )
                }

                override fun onActivityStopped(activity: Activity) {}

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

                override fun onActivityDestroyed(activity: Activity) {
                    activityStack.remove(activity)
                }
            }
}
