package andme.core.app

import android.app.Activity

/**
 * Created by Lucio on 2019/2/14.
 */
interface AMActivityStack {

    /**
     * 大小
     */
    val size: Int

    /**
     * 添加Activity
     */
    fun add(activity: Activity)

    /**
     * 移除某个activity
     */
    fun remove(activity: Activity)

    /**
     * 获取指定的Activity
     */
    fun get(cls: Class<out Activity>): Activity?

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun getCurrent(): Activity?

    /**
     * 结束指定的Activity
     */
    fun finish(activity: Activity)

    /**
     * 结束所有Activity
     */
    fun finishAll()

    /**
     * 清除所有
     */
    fun clear()

}