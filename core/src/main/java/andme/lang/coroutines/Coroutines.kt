package andme.lang.coroutines

import andme.lang.RetryException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Created by Lucio on 2020/12/14.
 */
/**
 * launch方法的增强版，支持重试，类似RxJava的retryWhen效果
 * 当捕获了[block]执行过程中抛出的[RetryException]时，将会重新执行[block]
 */
fun CoroutineScope.launchRetryable(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
) {
    launch(context, start) {
        var relaunch = true
        var timestamp = System.currentTimeMillis() - 50
        while (relaunch) {
            try {
                coroutineScope {
                    block.invoke(this)
                }
                relaunch = false
            } catch (e: RetryException) {
                //捕获重试异常，避免中断逻辑
                //极短时间内发生重试异常，认为流程出现异常，终止执行，以异常形式抛出
                if (System.currentTimeMillis() - timestamp < 30) {
                    relaunch = false
                }
                timestamp = System.currentTimeMillis()
            }
        }
    }
}