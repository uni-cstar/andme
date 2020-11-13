package andme.core.kt

import andme.core.isDebuggable

/**
 * Created by Lucio on 2020-10-29.
 */

inline fun Boolean?.orDefault(def: Boolean = false) = this ?: def

inline fun Int?.orDefault(def: Int = 0) = this ?: def
inline fun Float?.orDefault(def: Float = 0f) = this ?: def
inline fun Long?.orDefault(def: Long = 0) = this ?: def
inline fun Double?.orDefault(def: Double = 0.0) = this ?: def
inline fun <T> T?.orDefault(initializer: () -> T): T = this ?: initializer()

inline fun <T, R> T.map(transformer: T.() -> R): R {
    return transformer(this)
}

/**
 * 调试执行代码
 */
inline fun <T> T.runOnDebug(action: () -> Unit): T {
    if (isDebuggable) {
        action()
    }
    return this
}

inline fun <T> T.runOnTrue(condition: Boolean, action: T.() -> Unit): T {
    if (condition) {
        action(this)
    }
    return this
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