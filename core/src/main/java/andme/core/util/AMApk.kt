@file:JvmName("AMApk")

package andme.core.util

import andme.core.content.activityManager
import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.content.Intent.ACTION_DELETE
import android.net.Uri
import android.net.Uri.fromParts




/**
 * 当前应用是否处于前台
 * @Note 据百度上某些网友说在某些机型上，始终返回的是ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
 * @link [halo.android.content.AppState] 在api14上建议使用此方法
 * "Note: this method is only intended for debugging or building a user-facing process management UI."
 */
fun Context.isAppForeground(): Boolean {
    val processes = activityManager?.runningAppProcesses ?: return false
    val pkgName = packageName
    processes.forEach {
        if (it.processName == pkgName)
            return it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
    }
    return false
}

/**
 * 判断指定服务是否正在后台运行
 * @param
 */
fun Context.isServiceRunning(serviceClass: Class<out Service>): Boolean {
    return isServiceRunning(serviceClass.name)
}

/**
 * 判断指定className的service是否正在后台运行
 * @param className 服务的class name，可以使用Class.getName获取
 * @return
 */
fun Context.isServiceRunning(className: String): Boolean {
    val serviceList = activityManager?.getRunningServices(100) ?: return false
    if (serviceList.isNullOrEmpty())
        return false

    serviceList.forEach {
        if (it.service.className == className)
            return true
    }
    return false
}

/**
 * 获取清单文件中指定的meta-data
 * @param key     MetaData对应的Key
 * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
 */
inline fun Context.getMetaData(key: String): String? {
    val applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    return applicationInfo.metaData?.getString(key)
}

/**
 * 获得渠道号
 * @param channelKey 指定渠道Key
 * @return
 */
inline fun Context.getChannelNo(channelKey: String): String? = getMetaData(channelKey)

/**
 * 获取程序版本名称
 * @return
 */
inline fun Context.getVersionName(): String {
    val packInfo = packageManager.getPackageInfo(packageName, 0)//0代表是获取版本信息
    return packInfo.versionName
}

/**
 * 获取应用程序名称
 */
inline fun Context.getAppName(): String? {
    try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val labelRes = packageInfo.applicationInfo.labelRes
        return resources.getString(labelRes)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

/**
 * 获取版本号
 * @return
 */
inline fun Context.getVersionCode(): Long {
    val packInfo = packageManager.getPackageInfo(packageName, 0)
    if (Build.VERSION.SDK_INT >= 28) {
        return packInfo.longVersionCode
    } else {
        return packInfo.versionCode.toLong()
    }

}

/**
 * 获取程序第一次安装时间
 */
inline fun Context.getFirstInstallTime(): Long {
    val packInfo = packageManager.getPackageInfo(packageName, 0)//0代表是获取版本信息
    return packInfo.firstInstallTime
}

/**
 * 获取程序上次更新时间
 */
inline fun Context.getLastUpdateTime(): Long {
    val packInfo = packageManager.getPackageInfo(packageName, 0)//0代表是获取版本信息
    return packInfo.lastUpdateTime
}

/**
 * 获取目标版本
 */
inline fun getTargetSdkVersion(ctx: Context): Int {
    val packInfo = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
    return packInfo.applicationInfo.targetSdkVersion
}


/**
 * 是否是主进程
 */
inline val Context.isMainProcess: Boolean
    get() = Apps.isMainProcess(this)

/**
 * 获取当前进程名字
 */
inline val Context.processName: String?
    get() = Apps.getProcessName(this)


/**
 * 判断手机是否安装某个应用
 * @param ctx
 * @param appPkgName  应用包名
 * @return   true：安装，false：未安装
 */
fun Context.isApplicationInstall( appPkgName: String): Boolean {
    val packageManager = packageManager ?: return false
    val pinfos = packageManager.getInstalledPackages(0)
    if (pinfos.isNullOrEmpty())
        return false
    pinfos.forEach {
        if (it.packageName == appPkgName)
            return true
    }
    return false
}

/**
 * 删除某个应用
 * @Note: 清单文件中增加如下权限 <uses-permission android:name="android.permission.REQUEST_DELETE_PACKAGES" />

 */
fun Context.deleteApplication(appPkgName: String){
    val uri = Uri.fromParts("package", appPkgName, null)
    val intent = Intent(ACTION_DELETE, uri)
    startActivity(intent)
}
