package andme.core.exception

import andme.core.dialogHandlerAM
import andme.lang.orDefaultIfNullOrEmpty
import andme.lang.runOnDebug
import android.content.Context
import android.text.TextUtils
import retrofit2.HttpException
import java.lang.NullPointerException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.ExecutionException

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
        when (e) {
            is ConnectException -> {
                return "连接失败，请检查网络。"
            }
            is UnknownHostException -> {
                return "当前网络或服务器异常。"
            }
            is SocketTimeoutException -> {
                return "网络连接超时。"
            }
            is HttpException -> {
                return "请求失败：code=${e.code()} message=${e.message()}。"
            }
            is NullPointerException ->{
                return "空异常。"
            }
            is ExecutionException ->{
                if(e.cause?.javaClass == UnknownHostException::class.java){
                    return "当前网络或服务器异常。"
                }else{
                    return e.cause?.message.orDefaultIfNullOrEmpty(e.message ?: e.toString())
                }
            }
            else ->{
                return e.message
            }
        }

//        if (e is JsonSyntaxException) {
//            return "Json解析失败：${e.message}"
//        } else {
        return e.message.orEmpty()
//        }
    }

}

