package andme.lang.coroutines

import kotlinx.coroutines.*
import java.io.Closeable
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

/**
 * Created by Lucio on 2020-02-04.
 * 执行队列
 * @param threadCount 子线程数量
 */
abstract class ExecutorQueue<T>(val threadCount: Int = 3) {

    private val TAG = "AMExecutorQueue"

    //工作线程池
    private val executorPool: ExecutorService

    private val dispatcherScope: CloseableCoroutineScope

    //线程原子锁变量
    @Volatile
    private var requestCount = 0

    //请求队列
    private val requestTasks: Queue<T>

    //入队锁
    private val enqueueLock = Any()

    //执行锁
    private val executeLock = Any()

    //标识：是否正在执行
    private var isExecutingFlag = true

    init {
        requestTasks = LinkedList<T>()
        executorPool = if (threadCount > 1) {
            Executors.newFixedThreadPool(threadCount)
        } else {
            Executors.newSingleThreadExecutor()
        }
        dispatcherScope = CloseableCoroutineScope(SupervisorJob() + Dispatchers.IO)
        log("执行队列初始化完成")

    }

    /**
     * 入队任务
     *
     * @param task
     * @param immediately 是否执行队列任务,true执行，false只是入队
     */
    fun enqueueTask(task: T, immediately: Boolean = true) {
        synchronized(enqueueLock) {
            requestTasks.offer(task)
            log("入队任务")
            if (immediately) {
                startTask()
            }
        }
    }

    /**
     * 开始执行
     */
    private fun startTask() {
        log("准备开始执行")
        isExecutingFlag = true
        synchronized(executeLock) {
            if (requestCount > 0) {
                log("忽略，当前正在执行中")
                return
            }
            requestCount++
        }
        log("开始执行")

        dispatcherScope.launch {
            try {
                while (isExecutingFlag && requestTasks.size > 0) {
                    val task = requestTasks.poll()
                    log("执行任务$task 当前未执行任务数${requestTasks.size}")
                    executorPool.execute {
                        //执行工作
                        try {
                            executeTask(task)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                requestCount--
            }
            log("执行结束，当前未执行任务数${requestTasks.size}")
        }
    }

    /**
     * 停止执行
     */
    fun stopTasks() {
        isExecutingFlag = false
        executorPool.shutdownNow()
        dispatcherScope.close()
    }

    /**
     * 执行任务
     */
    protected abstract fun executeTask(task: T)

    protected fun log(msg: String) {
        println("$TAG: $msg")
    }

    internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
        override val coroutineContext: CoroutineContext = context

        override fun close() {
            coroutineContext.cancel()
        }
    }

}