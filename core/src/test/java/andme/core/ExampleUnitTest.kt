package andme.core

import kotlinx.coroutines.*
import org.junit.Test
import kotlin.random.Random

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        val time = System.currentTimeMillis()
        runBlocking {
            val dispatcher = newFixedThreadPoolContext(4, "CoroutineThreadPoolDispatcher")
            repeat(100) {
                launch (dispatcher) {
//                    if (it % 2 == 0) {
//                        delay(Random.nextLong(0, 500))
//                    }else{
//                        delay(Random.nextLong(500, 1000))
//                    }
                    delay(100)
                    println("${Thread.currentThread().name}:coroutine println $it")

                }
            }
        }
        println("time=${System.currentTimeMillis() - time}")
    }

}
