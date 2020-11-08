package blog_code.kt

import java.util.*

/**
 * Created by Lucio on 2020-11-04.
 */

//Study1.kt
class Study1 {//定义一个类

    //定义一个String类型的只读变量
    val content: String = "Hello,World."

    //定义一个私有String类型的变量
    private var name: String = "SuperLuo"

    //定义一个protect类型的变量
    protected var age: Int = 18

    //构造函数
    constructor(name: String) : this(name, 18)

    constructor(name: String, age: Int) {
        this.name = name
        this.age = age
    }

    //定义一个方法
    fun sayHello() {
        println(this.name + "说道：\"" + content + "\"");
    }

    //定义一个带参数的方法，计算两者之和
    fun sum(a: Int, b: Int): Int {
        return a + b
    }

    //定义一个私有的方法打印当前时间
    private fun printCurrentDate() {
        println(Date().toString())
    }

}

