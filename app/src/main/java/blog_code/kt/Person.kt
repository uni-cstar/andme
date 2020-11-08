package blog_code.kt

/**
 * Created by Lucio on 2020-11-05.
 */
abstract class Person {

    var name: String
    val age: Int

    //抽象属性
    abstract val sex: String

    constructor(name: String) {
        this.name = name
        //age 是val变量，赋值之后就不能再修改了
        this.age = 18
    }

    abstract fun getAddress(): String

    fun printInfo() {
        println("${name}今年${age}岁,性别${sex},住在${getAddress()}")
    }
}

class Male(name: String) : Person(name) {

    override val sex: String
        get() = "男"

    override fun getAddress(): String {
        return "火星"
    }

}

interface Gun {

    val power:Int

    fun printPower(){
        println("攻击力$power")
    }

    fun shoot()
}

class AK : Gun {
    override val power: Int = 800

    override fun shoot() {
        println("使用AK射击")
    }
}
