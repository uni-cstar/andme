@file:JvmName("AMDevice")
@file:JvmMultifileClass


package andme.core.util

import andme.core.content.connectivityManager
import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission

/**
 * Created by Lucio on 2020-03-10.
 */

@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
@NetworkType
internal fun Context.getNetworkTypeDefault(): Int {
    val ani = connectivityManager?.activeNetworkInfo ?: return NETWORK_TYPE_NONE
    if (!ani.isAvailable)
        return NETWORK_TYPE_NONE
    return when (ani.type) {
        ConnectivityManager.TYPE_WIFI ->
            NETWORK_TYPE_WIFI
        ConnectivityManager.TYPE_MOBILE -> {
            NETWORK_TYPE_MOBILE
        }
        ConnectivityManager.TYPE_ETHERNET ->{
            NETWORK_TYPE_ETHERNET
        }
        else -> NETWORK_TYPE_UNKNOWN
    }
}

@RequiresApi(23)
@NetworkType
@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
internal fun Context.getNetworkType23(): Int {
    val cm = connectivityManager ?: return NETWORK_TYPE_NONE
    val an = connectivityManager?.activeNetwork ?: return NETWORK_TYPE_NONE
    val nc = cm.getNetworkCapabilities(an) ?: return NETWORK_TYPE_NONE
    return if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
        NETWORK_TYPE_WIFI
    } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
        NETWORK_TYPE_MOBILE
    } else if(nc.hasTransport(NetworkCapabilities. TRANSPORT_ETHERNET)){
        NETWORK_TYPE_ETHERNET
    }else {
        NETWORK_TYPE_UNKNOWN
    }
}

/**
 * 转换常量值
 */
internal fun transformNetworkType(@NetworkType type: Int): Int {
    if (Build.VERSION.SDK_INT < 23) {
        return when (type) {
            NETWORK_TYPE_MOBILE -> {
                ConnectivityManager.TYPE_MOBILE
            }
            NETWORK_TYPE_WIFI -> {
                ConnectivityManager.TYPE_WIFI
            }
            else -> {
                throw IllegalArgumentException("不支持的NetworkType类型")
            }
        }
    } else {
        return when (type) {
            NETWORK_TYPE_MOBILE -> {
                NetworkCapabilities.TRANSPORT_CELLULAR
            }
            NETWORK_TYPE_WIFI -> {
                NetworkCapabilities.TRANSPORT_WIFI
            }
            else -> {
                throw IllegalArgumentException("不支持的NetworkType类型")
            }
        }
    }
}

@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
internal fun isNetworkConnected(ctx: Context, @NetworkType type: Int): Boolean {
    val cm = ctx.connectivityManager ?: return false
    val netType = transformNetworkType(type)

    if (Build.VERSION.SDK_INT < 23) {
        cm.allNetworks?.forEach {
            val networkInfo = cm.getNetworkInfo(it)
            if (networkInfo != null && networkInfo.isConnected && networkInfo.type == netType)
                return true
        }
        return false
    } else {
        val an = cm.activeNetwork ?: return false
        val nc = cm.getNetworkCapabilities(an) ?: return false
        return nc.hasTransport(netType)
    }
}

/**
 * 生成隐藏虚拟导航栏（华为手机底部虚拟导航栏）的标志
 */
@TargetApi(19)
internal fun generateHideNavigationBarFlag(): Int {
    var flag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //请求隐藏底部导航栏
            .or(View.SYSTEM_UI_FLAG_IMMERSIVE)  //这个flag只有当设置了SYSTEM_UI_FLAG_HIDE_NAVIGATION才起作用。如果没有设置这个flag，任意的View相互动作都退出SYSTEM_UI_FLAG_HIDE_NAVIGATION模式。如果设置就不会退出
            .or(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) //让View全屏显示，Layout会被拉伸到NavigationBar下面
    if (Build.VERSION.SDK_INT >= 19)
        flag = flag.or(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)//这个flag只有当设置了SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION 时才起作用。如果没有设置这个flag，任意的View相互动作都坏退出SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION模式。如果设置就不受影响
    return flag
}