package andme.core.exception

import andme.core.kt.runOnDebug
import android.content.Context

open class  CommonExceptionHandler : ExceptionHandler {

    override fun handleUncaughtException(e: Throwable) {
        //未捕获的异常默认将异常信息写入文件中
        //文件存储路径：/Android/data/{packagename}/file/crash/{yyyy-mm-dd}/{yyyy-mm-dd}
        //todo  保存异常信息到本地
    }

    override fun handleCatchException( e: Throwable) {
        runOnDebug {
            e.printStackTrace()
        }
    }

    override fun handleUIException(context: Context, e: Throwable) {

    }

    override fun getFriendlyMessage(e: Throwable): String? {
        return e.message
    }

}