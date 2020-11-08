package andme.sample

import blog_code.kt.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun useStudyClassInKt() {
        //创建对象
        val instance = Study1("SuperLuo")
        //打印content变量值
        println("content=${instance.content}")
        //调用对象函数
        instance.sayHello()
        //调用对象带参数的函数
        val result = instance.sum(1, 2)
        println("1+2=$result")
    }

    @Test
    fun testAbstract() {
        val me = Male("小罗")
        me.printInfo()
    }

    @Test
    fun testAnonyClass() {
        val she = object : Person("Siri") {
            override val sex: String
                get() = "女"

            override fun getAddress(): String {
                return "太阳"
            }

        }
        she.printInfo()
    }

    @Test
    fun testInterface() {
        val ak = AK()
        ak.printPower()
        ak.shoot()

        val waterGun = object : Gun {
            override val power: Int
                get() = 300

            //重写了接口的默认实现逻辑
            override fun printPower() {
                println("水枪没有攻击力")
            }

            override fun shoot() {
                println("使用水枪攻击")
            }

        }
        waterGun.printPower()
        waterGun.shoot()
    }
//    @Test
//    fun testAbstract2() {
//        val me = object: Person("")
//        me.printInfo()
//    }
}
