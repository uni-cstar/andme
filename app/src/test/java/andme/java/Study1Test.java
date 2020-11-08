package andme.java;

import android.view.View;

import org.junit.Test;

import blog_code.kt.Study1;

/**
 * Created by Lucio on 2020-11-04.
 */
public class Study1Test {

    @Test
    public void useStudyClassInJava() {
        //创建对象
        Study1 instance = new Study1("SuperLuo");
        //打印content变量值
        System.out.println("content=" + instance.getContent());
        //调用对象函数
        instance.sayHello();
        //调用对象带参数的函数
        int result = instance.sum(1, 2);
        System.out.println("1+2=" + result);

    }


}
