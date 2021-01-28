package andme.lang.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 * Created by Lucio on 2021/1/9.
 */

class AMCloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}

/**
 * io scope
 */
fun IOCoroutineScope(): AMCloseableCoroutineScope {
    return AMCloseableCoroutineScope(SupervisorJob() + Dispatchers.IO)
}

/**
 * main scope
 */
fun MainCoroutineScope():AMCloseableCoroutineScope{
    return AMCloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
}