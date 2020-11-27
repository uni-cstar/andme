package andme.arch

import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.CountDownLatch

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

//    @Test
//    fun main() {
//
//        val cdl = CountDownLatch(1)
//        runBlocking {
//            val startTime = System.currentTimeMillis()
//            val job = launch(Dispatchers.Default) {
//                var nextPrintTime = startTime
//                var i = 0
//                while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
//                    // 每秒打印消息两次
//                    if (System.currentTimeMillis() >= nextPrintTime) {
//                        println("job: I'm sleeping ${i++} ...")
//                        nextPrintTime += 500L
//                    }
//                }
//            }
//            delay(1300L) // 等待一段时间
//            println("main: I'm tired of waiting!")
//            job.cancelAndJoin() // 取消一个作业并且等待它结束
//            println("main: Now I can quit.")
//            cdl.countDown()
//        }
//        cdl.await()
//    }
}
