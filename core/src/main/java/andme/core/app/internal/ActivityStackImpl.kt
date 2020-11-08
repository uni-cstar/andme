package andme.core.app.internal

import andme.core.app.AMActivityStack
import android.app.Activity
import java.util.*

/**
 * Created by Lucio on 2019/1/28.
 * Activity堆栈
 */
internal class ActivityStackImpl : AMActivityStack {

    private val mStack: Stack<Activity> = Stack<Activity>()

    override val size: Int get() = mStack.size

    /**
     * 添加Activity到堆栈
     */
    @Synchronized
    override fun add(activity: Activity) {
        mStack.add(activity)
    }

    /**
     * 移除指定的Activity
     */
    @Synchronized
    override fun remove(activity: Activity) {
        mStack.remove(activity)
    }

    /**
     * 获取指定的Activity
     */
    override fun get(cls: Class<out Activity>): Activity? {
        return mStack.firstOrNull {
            it.javaClass == cls
        }
    }

    override fun getCurrent(): Activity? {
        return mStack.lastElement()
    }

    /**
     * 结束指定的Activity
     */
    override fun finish(activity: Activity) {
        finishInternal(activity)
        remove(activity)
    }

    /**
     * 结束所有Activity
     */
    override fun finishAll() {
        mStack.forEach {
            finishInternal(it)
        }
        clear()
    }

    override fun clear() {
        mStack.clear()
    }

    private fun finishInternal(activity: Activity) {
        if (!activity.isFinishing)
            activity.finish()
    }
}