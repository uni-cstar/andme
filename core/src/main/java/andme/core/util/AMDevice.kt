@file:JvmName("AMDevice")
@file:JvmMultifileClass

package andme.core.util

import andme.core.content.*
import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.ResultReceiver
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IntDef
import androidx.annotation.RequiresPermission

/**
 * Created by Lucio on 2020-03-07.
 * 跟设备相关的工具函数
 */

const val NETWORK_TYPE_NONE = -1

const val NETWORK_TYPE_MOBILE = 1

const val NETWORK_TYPE_WIFI = 2

const val NETWORK_TYPE_UNKNOWN = 3

@Retention(AnnotationRetention.SOURCE)
@IntDef(flag = true, value = [NETWORK_TYPE_NONE, NETWORK_TYPE_WIFI, NETWORK_TYPE_MOBILE,NETWORK_TYPE_UNKNOWN])
annotation class NetworkType

@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
@NetworkType
fun Context.getNetworkType(): Int {
    val cm = connectivityManager ?: return NETWORK_TYPE_NONE
    if (Build.VERSION.SDK_INT >=23){
        return getNetworkType23()
    }else{
        return getNetworkTypeDefault()
    }
}

/**
 * 网络是否连接
 */
@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isNetworkConnected(): Boolean {
    val cm = connectivityManager ?: return false
    if (Build.VERSION.SDK_INT < 23) {
        val ani = cm.activeNetworkInfo ?: return false
        val type = ani.type
        return ani.isConnected && (type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_MOBILE)
    } else {
        val an = cm.activeNetwork ?: return false
        val nc = cm.getNetworkCapabilities(an) ?: return false
        return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
}

/**
 * wifi 是否连接
 */
@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isWifiConnected(): Boolean {
    return isNetworkConnected(this, NETWORK_TYPE_WIFI)
}

/**
 * 手机信号是否打开
 */
@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isGPRSConnected(): Boolean {
    return isNetworkConnected(this, NETWORK_TYPE_MOBILE)
}

/**
 * 获取Wifi SSID(wifi名字)
 */
@RequiresPermission(allOf = [Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE])
fun Context.getWifiSSID(): String? {
    if (!isWifiConnected())
        return null
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
        val connManager = connectivityManager ?: return null
        val networkInfo = connManager.activeNetworkInfo
        if (networkInfo.isConnected) {
            if (networkInfo.extraInfo != null) {
                return networkInfo.extraInfo.replace("\"", "")
            }
        }
        return null
    } else {
        val wifiMgr = applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
                ?: return null
        val ssid = wifiMgr.connectionInfo.ssid
        return if (ssid.isNullOrEmpty()) {
            null
        } else {
            if (ssid.contains("\"")) ssid.replace("\"", "") else ssid
        }
    }
}

/**
 * 隐藏底部虚拟按钮
 */
fun Activity.hideNavigationBar() {
    val sdkInt = Build.VERSION.SDK_INT
    if (sdkInt in 12..18) {
        window.decorView.systemUiVisibility = View.GONE
    } else if (sdkInt >= 19) {
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or generateHideNavigationBarFlag()
    }
}

/**
 * 显示虚拟按键
 */
fun Activity.showNavigationBar() {
    val sdkInt = Build.VERSION.SDK_INT
    if (sdkInt in 12..18) {
        window.decorView.systemUiVisibility = View.VISIBLE
    } else if (sdkInt >= 19) {
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and generateHideNavigationBarFlag().inv()
    }
}


/**
 * 全屏切换：参考地址 https://blog.csdn.net/qq_25034451/article/details/52313696
 * @param isFullScreen true:全屏
 * @param hideNavBar 是否隐藏（底部）虚拟按钮
 */
@TargetApi(16)
fun Activity.toggleFullScreen(isFullScreen: Boolean, hideNavBar: Boolean = true) {
    window?.decorView?.apply {
        var flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE//防止系统栏隐藏时内容区域大小发生变化
                .or(View.SYSTEM_UI_FLAG_FULLSCREEN)  //请求全屏显示，状态栏会被隐藏，底部导航栏不会被隐藏，效果和WindowManager.LayoutParams.FLAG_FULLSCREEN相同
//            flag = flag.or(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)//让View全屏显示，Layout会被拉伸到StatusBar下面，不包含NavigationBar
//            var flag = View.SYSTEM_UI_FLAG_FULLSCREEN
        if (hideNavBar) {
            flag = flag.or(generateHideNavigationBarFlag())
        }
        if (isFullScreen) {
            systemUiVisibility = systemUiVisibility or flag
        } else {
            systemUiVisibility = systemUiVisibility and flag.inv()
        }
    }
}

/**
 * 复制内容到剪切板
 * @param content
 * 复制的内容
 */
fun Context.copyToClipboard(content: String) {
    val clip = ClipData.newPlainText(null, content)
    clipboardManager.primaryClip = clip
}

/**
 * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
 *
 * @return 平板返回 True，手机返回 False
 */
fun Context.isTablet(): Boolean {
    return resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

/**
 *  定位服务是否可用
 */
fun Context.isLocationServiceEnable(): Boolean {
    val accessibleProviders = locationManager.getProviders(true)
    return accessibleProviders != null && accessibleProviders.size > 0
}

/**
 * 是否亮屏
 * @return true:亮屏 false:锁屏
 * 屏幕亮屏和锁屏的时候，系统会发出对应通知
 * @see android.content.Intent#ACTION_SCREEN_ON
 * @see android.content.Intent#ACTION_SCREEN_OFF
 */
fun Context.isScreenOn(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
        return powerManager.isScreenOn
    } else {
        return powerManager.isInteractive
    }
}

/**
 * 软键盘是否打开
 * @param context
 * @param target the given view whether is the currently active view
 * @return 如果target不为空，则判断当前软键盘是否打开，并且激活软键盘的view是对应的target
 * //此方法不是很有效，经常不管软键盘显示与否，都是返回的true
 */
@Deprecated("")
fun Context.isSoftInputShow(target: View? = null): Boolean {
    if (target == null) {
        return inputMethodManager.isActive
    } else {
        return inputMethodManager.isActive(target)
    }
}

/**
 * 切换软键盘
 */
fun Context.toggleSoftInput() {
    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * 强制隐藏输入法
 * @param receiver
 * receiver可能造成内存泄漏，因为它是一个长生命周期对象，因此receiver最好是使用弱引用，即WeakReference。" +
"原文：Caveat: ResultReceiver instance passed to this method can be a long-lived object, because it may not be garbage-collected until all the corresponding ResultReceiver objects transferred to different processes get garbage-collected. Follow the general patterns to avoid memory leaks in Android. Consider to use WeakReference so that application logic objects such as Activity and Context can be garbage collected regardless of the lifetime of ResultReceiver.
 */
@JvmOverloads
fun Context.hideSoftInput(receiver: ResultReceiver? = null) {
    if (this !is Activity)
        return
    val view = this.currentFocus ?: return
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0, receiver)
}

/**
 * 隐藏输入法
 * @param view 当前激活软键盘的view，通常是EditText
 */
fun View.hideSoftInput() {
    this.context.inputMethodManager.hideSoftInputFromInputMethod(this.windowToken, 0)
}

/**
 * 强制显示输入法
 * @param view 用于关联输入法的view
 */
fun Context.showSoftInput(view: View) {
    view.requestFocus()//先让view获取焦点
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

/**
 * 对话框显示时总是显示输入法
 */
fun Dialog.showSoftInputAlways(): Dialog {
    showSoftInputAlwaysImpl()
    return this
}

/**
 * 对话框显示时,总是显示输入法
 */
fun Dialog.showSoftInputAlwaysImpl() {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
}

/**
 * 对话框显示时,总是隐藏输入法
 */
fun Dialog.hideSoftInputAlwaysImpl() {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}