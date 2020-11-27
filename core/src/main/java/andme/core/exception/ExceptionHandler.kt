/**
 * Created by Lucio on 2020-11-02.

 */
package andme.core.exception

import andme.core.exceptionHandlerAM
import andme.core.isDebuggable
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

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
 * 扩展友好消息字段，用于将异常转换成对用户比较容易理解的信息。
 */
inline val Throwable.friendlyMessage: String? get() = exceptionHandlerAM.getFriendlyMessage(this)

/**
 * 捕获ui异常
 */
inline fun Context.tryUi(action: () -> Unit): Throwable? {
    return try {
        action()
        null
    } catch (e: Exception) {
        exceptionHandlerAM.handleUIException(this, e)
        e
    }
}

inline fun View.tryUi(action: () -> Unit): Throwable? {
    return context.tryUi(action)
}

inline fun Fragment.tryUi(action: () -> Unit): Throwable? {
    return context?.tryUi(action)
}

/**
 * 异常处理
 * @param printStack 异常时，是否调用printStackTrace方法打印日常
 */
inline fun <T> T.tryCatch(printStack: Boolean = isDebuggable, action: T.() -> Unit): Throwable? {
    try {
        action()
        return null
    } catch (e: Exception) {
        if (printStack) {
            e.printStackTrace()
        }
        exceptionHandlerAM.handleCatchException(e)
        return e
    }
}

inline fun <T> Throwable?.onCatch(block: (Throwable) -> T): T? {
    if (this == null)
        return null
    return block(this)
}