package andme.core.kt


/**
 * Created by Lucio on 2020-10-29.
 */

inline fun Boolean?.orDefault(def: Boolean = false) = this ?: def

inline fun Int?.orDefault(def: Int = 0) = this ?: def
inline fun Float?.orDefault(def: Float = 0f) = this ?: def
inline fun Long?.orDefault(def: Long = 0) = this ?: def
inline fun Double?.orDefault(def: Double = 0.0) = this ?: def
inline fun <T> T?.orDefault(initializer: () -> T): T = this ?: initializer()

/**
 * 调试执行代码
 */
inline fun <T> T.runOnDebug(action: () -> Unit): T {
    if (Ktx.isDebuggable) {
        action()
    }
    return this
}

inline fun <T> T.runOnTrue(condition: Boolean, action: T.() -> Unit){
   if(condition){
       action(this)
   }
}

/**
 * 异常处理
 * @param printStack 异常时，是否调用printStackTrace方法打印日常
 */
inline fun <T> T.tryCatch(printStack: Boolean = Ktx.isDebuggable, action: T.() -> Unit) {
    try {
        action()
    } catch (e: Exception) {
        if (printStack) {
            e.printStackTrace()
        }
    }
}

var isTimeMonitorEnable: Boolean = true
/**
 * 监控方法运行时间
 */
inline fun runTimeMonitor(
    tag: String = Thread.currentThread().stackTrace[1].methodName,
    func: () -> Unit
) {
    val start = System.currentTimeMillis()
    func()
    if (!isTimeMonitorEnable)
        return
    val end = System.currentTimeMillis() - start
    if (end > 500) {
        println("[TimeMonitor]: $tag takes ${end / 1000.0} seconds")
    } else {
        println("[TimeMonitor]: $tag takes $end milliseconds")
    }
}