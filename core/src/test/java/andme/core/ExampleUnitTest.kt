package andme.core

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import org.junit.Test

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
                launch(dispatcher) {
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

    @Test
    fun getSpanSize() {
        var x = 0
        while (true) {
            x++
            if (x % 6 == 2 && (2 * x) % 6 == 2 ) {
                println("x=$x")
                break
            }
        }
    }
}
