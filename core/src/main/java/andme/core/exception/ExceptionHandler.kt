/**
 * Created by Lucio on 2020-11-02.

 */
package andme.core.exception

import andme.core.exceptionHandler
import android.content.Context

/**
 * 异常处理器,用于处理程序中相关的各种类型的异常
 */
interface ExceptionHandler {

    /**
     * 处理未捕获的异常
     */
    fun handleUncaughtException(e: Throwable)

    /**
     * 处理被捕获的常规异常：即用户通过tryCatch函数捕获的异常
     */
    fun handleCatchException(e: Throwable)

    /**
     * 处理UI异常：即用户通过tryUi函数捕获的异常
     */
    fun handleUIException(context: Context, e: Throwable)


    /**
     * 获取异常友好的描述信息
     */
    fun getFriendlyMessage(e: Throwable): String?

}



/**
 * 捕获ui异常
 */
inline fun Context.tryUi(func: Context.() -> Unit): Throwable? {
    return try {
        func(this)
        null
    } catch (e: Exception) {
        exceptionHandler.handleUIException(this, e)
        e
    }
}

/**
 * 扩展友好消息字段，用于将异常转换成对用户比较容易理解的信息。
 */
inline val Throwable.friendlyMessage: String? get() = exceptionHandler.getFriendlyMessage(this)