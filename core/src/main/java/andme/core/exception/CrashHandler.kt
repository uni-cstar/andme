package andme.core.exception

/**
 * Created by Lucio on 2020-11-13.
 */
class CrashHandler : Thread.UncaughtExceptionHandler {

    private val mDefaultHandler: Thread.UncaughtExceptionHandler

    init {
        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    }

    /**
     * 设置当前为未处理异常处理器
     */
    fun apply() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        e.printStackTrace()
    }

}