package andme.core.util

import andme.core.content.activityManager
import andme.core.exception.tryCatch
import android.app.ActivityManager
import android.content.Context
import android.os.Process
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * Created by Lucio on 2021/7/9.
 */
object Apps {

    /**
     * 当前是否是主进程
     */
    @JvmStatic
    fun isMainProcess(ctx: Context):Boolean{
        val pkgName = ctx.packageName
        val processName = getProcessName(ctx)
        return pkgName == processName
    }

    /**
     * 获取当前进程名字
     */
    @JvmStatic
    fun getProcessName(ctx: Context): String? {
        val pid = Process.myPid()
        val name = getProcessNameFromFile(pid)
        if (!name.isNullOrEmpty())
            return name
        return getProcessNameFromRunningProcesses(ctx, pid)
    }

    /**
     * 获取当前进程名字（遍历方式）
     * @param pid 进程id
     */
    @JvmStatic
    fun getProcessNameFromRunningProcesses(ctx: Context, pid: Int): String? {
        val am = ctx.activityManager ?: return null
        val i = am.runningAppProcesses.iterator()
        tryCatch {
            while (i.hasNext()) {
                val info = i.next() as ActivityManager.RunningAppProcessInfo
                if (info.pid == pid) {
                    return info.processName
                }
            }
        }
        return null
    }

    /**
     * 获取进程号对应的进程名(文件读取方式-bugly推荐方式)
     *
     * @param pid 进程号
     * @return 进程名
     */
    @JvmStatic
    fun getProcessNameFromFile(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName: String = reader.readLine()
            if (processName.isNotEmpty()) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }
}