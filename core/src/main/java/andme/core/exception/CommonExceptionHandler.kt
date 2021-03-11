package andme.core.exception

import andme.core.dialogHandlerAM
import andme.lang.runOnDebug
import android.content.Context
import com.google.gson.JsonSyntaxException

open class CommonExceptionHandler : ExceptionHandler {

    override fun handleUncaughtException(e: Throwable) {
        //未捕获的异常默认将异常信息写入文件中
        //文件存储路径：/Android/data/{packagename}/file/crash/{yyyy-mm-dd}/{yyyy-mm-dd}
        //todo  保存异常信息到本地
    }

    override fun handleCatchException(e: Throwable) {
        runOnDebug {
            e.printStackTrace()
        }
    }

    override fun handleUIException(context: Context, e: Throwable) {
        dialogHandlerAM.showAlertDialog(context, getFriendlyMessage(e).orEmpty(), "确定")
    }

    override fun getFriendlyMessage(e: Throwable): String? {
        if (e is JsonSyntaxException) {
            return "Json解析失败：${e.message}"
        } else {
            return e.message.orEmpty()
        }
    }

}

